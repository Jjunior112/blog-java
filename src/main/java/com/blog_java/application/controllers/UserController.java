package com.blog_java.application.controllers;

import com.blog_java.application.services.TokenService;
import com.blog_java.application.services.UserService;
import com.blog_java.domain.dtos.user.UserRegisterDto;
import com.blog_java.domain.dtos.user.JwtDto;
import com.blog_java.domain.dtos.user.LoginDto;
import com.blog_java.domain.dtos.user.UserListDto;
import com.blog_java.domain.models.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody @Valid LoginDto login) {

        var authToken = new UsernamePasswordAuthenticationToken(login.email(), login.password());

        var authentication = authenticationManager.authenticate(authToken);

        var user = (User) authentication.getPrincipal();

        var tokenJwt = tokenService.generateToken(user);

        return ResponseEntity.ok(
                new JwtDto(user.getId(),tokenJwt, user.getRole().name())
        );
    }

    @PostMapping("/register")
    public ResponseEntity<UserListDto> createCommonUser(@RequestBody @Valid UserRegisterDto userRegisterDto)
    {
        var response = userService.createUser(userRegisterDto);

        return ResponseEntity.ok(new UserListDto(response));
    }

    @PostMapping("/registerAdmin")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<UserListDto> createCommonUserAdmin(@RequestBody @Valid UserRegisterDto userRegisterDto)
    {
        var response = userService.createUserAdmin(userRegisterDto);

        return ResponseEntity.ok(new UserListDto(response));
    }

    @PostMapping("/registerModerator")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<UserListDto> createCommonUserModerator(@RequestBody @Valid UserRegisterDto userRegisterDto)
    {
        var response = userService.createUserModerator(userRegisterDto);

        return ResponseEntity.ok(new UserListDto(response));
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<UserListDto> GetUserById(@PathVariable Long id)
    {
        var response = userService.findById(id);

        return ResponseEntity.ok(new UserListDto(response));
    }

}
