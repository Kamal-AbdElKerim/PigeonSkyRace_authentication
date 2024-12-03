package com.example.SpringSecurityDemo.Controller;



import com.example.SpringSecurityDemo.Entity.User.UserDto;
import com.example.SpringSecurityDemo.Service.AccountService;
import com.example.SpringSecurityDemo.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/current-user")
    public ResponseEntity<Object> getCurrentUser() {
        return accountService.InfoAuth() ;
    }


    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @Valid @RequestBody UserDto userDto
           ) {
        return accountService.createUser(userDto) ;
    }




//    @PostMapping("/login")
//    public ResponseEntity<Object> login(
//            @Valid @RequestBody LoginDto loginDto
//           ) {
//        return accountService.Login(loginDto) ;
//    }
}
