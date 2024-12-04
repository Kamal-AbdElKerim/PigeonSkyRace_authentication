package com.example.SpringSecurityDemo.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final   CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("/account/register").permitAll();
                    authorize.requestMatchers("/Api/pigeons/**", "/Api/breeders/{breederId}" , "/account/current-user").hasAnyRole("USER", "ADMIN");
                    authorize.requestMatchers("/Api/breeders/**","/account/users/{userId}/roles").hasRole("ADMIN");
                    authorize.requestMatchers("/Api/Competition/**" , "/Api/CompetitionPigeon/**" , "/account/current-user").hasRole("ORGANIZER");
                    authorize.anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // Custom 401 handler
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            customAuthenticationEntryPoint.handleAccessDeniedException(request, response, (AccessDeniedException) accessDeniedException);
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }




}
