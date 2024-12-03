package com.example.SpringSecurityDemo.Service;


import com.example.SpringSecurityDemo.Entity.MapStruct.UserMapper;
import com.example.SpringSecurityDemo.Entity.Role.Role;
import com.example.SpringSecurityDemo.Entity.User.AppUser;
import com.example.SpringSecurityDemo.Entity.User.UserDto;
import com.example.SpringSecurityDemo.Entity.User.UserResponseDto;
import com.example.SpringSecurityDemo.Exception.EntityAlreadyExistsException;
import com.example.SpringSecurityDemo.Repository.AppUserRepository;
import com.example.SpringSecurityDemo.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;


@RequiredArgsConstructor
@Service
public class AccountService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;



    public ResponseEntity<Object> InfoAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<AppUser> otherUser = Optional.ofNullable(userRepository.findByEmail(authentication.getName()));

        if (otherUser.isPresent()) {
            return generateUserResponseDto(otherUser.get());
        }

        return new ResponseEntity<>("Utilisateur non trouvé", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> createUser(UserDto userDto){
        var bCryptEncoder = new BCryptPasswordEncoder();

        AppUser appuser = userMapper.userDtoToAppUser(userDto);

        if (userDto.getEmail().equalsIgnoreCase(userDto.getPassword())) {
            throw new IllegalArgumentException("Password cannot be the same as the email.");
        }

        Optional<AppUser> otherUser = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));

        if (otherUser.isPresent()) {
            throw new EntityAlreadyExistsException("Email", "Email address already used.");
        }

        Role ROLE_USER = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found!"));

        appuser.setUserID(generateUserID(appuser));
        appuser.setPassword(bCryptEncoder.encode(userDto.getPassword()));

        appuser.setRoles(Set.of(ROLE_USER));
    

        userRepository.save(appuser);

        return generateUserResponseDto(appuser);
    }

    private ResponseEntity<Object> generateUserResponseDto(AppUser appUser) {
        // Generate JWT token
     //   String jwtToken = securityConfiguration.createJwtToken(appUser);

        // Map AppUser to UserResponseDto
        UserResponseDto userResponseDto = userMapper.appUserToUserResponseDto(appUser);

        // Prepare response
        var response = new HashMap<String, Object>();
     //   response.put("token", jwtToken);
        response.put("user", userResponseDto);

        return ResponseEntity.ok(response);
    }

    private String generateUserID(AppUser appUser) {
        return UUID.randomUUID().toString().substring(0, 10) + appUser.getEmail().toLowerCase()  + UUID.randomUUID().toString().substring(0, 10);
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
