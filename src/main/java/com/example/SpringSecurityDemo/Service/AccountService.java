package com.example.SpringSecurityDemo.Service;


import com.example.SpringSecurityDemo.Entity.MapStruct.UserMapper;
import com.example.SpringSecurityDemo.Entity.Role.Role;
import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.User.UserDto;
import com.example.SpringSecurityDemo.Entity.User.UserResponseDto;
import com.example.SpringSecurityDemo.Exception.EntityAlreadyExistsException;
import com.example.SpringSecurityDemo.Repository.BreederRepository;
import com.example.SpringSecurityDemo.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class AccountService {

    private final BreederRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;



    public ResponseEntity<Object> InfoAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Breeder> otherUser = Optional.ofNullable(userRepository.findByNomColombie(authentication.getName()));

        if (otherUser.isPresent()) {
            return generateUserResponseDto(otherUser.get());
        }

        return new ResponseEntity<>("Utilisateur non trouvé", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> createUser(UserDto userDto){
        var bCryptEncoder = new BCryptPasswordEncoder();

        Breeder appuser = userMapper.userDtoToAppUser(userDto);



        Optional<Breeder> otherUser = Optional.ofNullable(userRepository.findByNomColombie(userDto.getNomColombie()));

        if (otherUser.isPresent()) {
            throw new EntityAlreadyExistsException("NomColombie", "NomColombie  already used.");
        }

        Role ROLE_USER = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found!"));

        appuser.setUserID(generateUserID(appuser));
        appuser.setPassword(bCryptEncoder.encode(userDto.getPassword()));

        appuser.setRoles(Set.of(ROLE_USER));
    

        userRepository.save(appuser);

        return generateUserResponseDto(appuser);
    }

    private ResponseEntity<Object> generateUserResponseDto(Breeder breeder) {
        // Generate JWT token
     //   String jwtToken = securityConfiguration.createJwtToken(appUser);

        // Map AppUser to UserResponseDto
        UserResponseDto userResponseDto = userMapper.appUserToUserResponseDto(breeder);

        // Prepare response
        var response = new HashMap<String, Object>();
     //   response.put("token", jwtToken);
        response.put("user", userResponseDto);

        return ResponseEntity.ok(response);
    }

    public Breeder updateUserRoles(Long userID, Set<Long> roleIds) {
        Breeder breeder = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Role> newRoles = new HashSet<>(roleRepository.findAllById(roleIds));


        // Log to debug the roles being assigned
        if (newRoles.isEmpty()) {
            throw new RuntimeException("Roles not found");
        }

        // Log the roles being assigned
        System.out.println("Assigning roles: " + newRoles);

        breeder.setRoles(newRoles);

        return  userRepository.save(breeder);


    }



    private String generateUserID(Breeder breeder) {
        return UUID.randomUUID().toString().substring(0, 10) + breeder.getNomColombie().toLowerCase()  + UUID.randomUUID().toString().substring(0, 10);
    }





    //    public ResponseEntity<Object> Login(LoginDto loginDto){
//        // Authenticate the user
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
//        );
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        // You can access the roles here
//        for (GrantedAuthority authority : authorities) {
//            System.out.println("Role: " + authority.getAuthority());
//        }
//
//        AppUser user = userRepository.findByEmail(loginDto.getEmail())
//                .orElseThrow(() -> new EntityNotFoundException("User", "User not found with email: " + loginDto.getEmail()));
//        return generateUserResponseDto(user);
//
//    }

}
