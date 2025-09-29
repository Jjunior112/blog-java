package com.blog_java.infra.security;

import com.blog_java.domain.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityFilter securityFilter) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->

                        auth
                                .requestMatchers(HttpMethod.POST, "/users/login","/users/register").permitAll()
                                .requestMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**","/users/confirm").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/registerAdmin","/users/registerModerator").hasRole(UserRole.ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/users").hasRole(UserRole.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/post/pending/**").hasAnyRole(UserRole.ADMIN.name(),UserRole.MODERATOR.name())
                                .requestMatchers(HttpMethod.GET, "/post/pending/**").hasAnyRole(UserRole.ADMIN.name(),UserRole.MODERATOR.name())
                                .requestMatchers(HttpMethod.DELETE, "/users").hasRole(UserRole.ADMIN.name())
                                .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

}