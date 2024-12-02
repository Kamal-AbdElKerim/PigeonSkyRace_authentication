package com.example.SpringSecurityDemo.Service;


import com.example.SpringSecurityDemo.Entity.MapStruct.UserMapper;
import com.example.SpringSecurityDemo.Entity.User.AppUser;
import com.example.SpringSecurityDemo.Entity.User.UserResponseDto;
import com.example.SpringSecurityDemo.Exception.EntityNotFoundException;
import com.example.SpringSecurityDemo.Repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final UserMapper userMapper;

    public ResponseEntity<Object> AuthInfo(Authentication authentication) {
        // The principal is the authenticated user (of type UserDetails)
        String email = authentication.getName(); // Get the email from the Authentication object (it will be the username in Basic Auth)

        if (email == null) {
            throw new EntityNotFoundException("appUser", "appUser with email " + email + " not found");
        }

        // Fetch the user by email from the database
        AppUser appUser = userRepository.findByEmail(email) ;


        // Map the AppUser to UserResponseDto
        UserResponseDto userResponseDto = userMapper.appUserToUserResponseDto(appUser);

        return ResponseEntity.ok(userResponseDto);
    }



}
