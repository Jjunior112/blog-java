package com.blog_java.application.controllers.v1;

import com.blog_java.application.services.TokenService;
import com.blog_java.application.services.UserService;
import com.blog_java.domain.dtos.user.*;
import com.blog_java.domain.models.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest httpServletRequest;

    public UserController(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager, HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.httpServletRequest = httpServletRequest;
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
        String baseUrl = httpServletRequest.getRequestURL().toString().replace("/users/register", "");

        var response = userService.createUser(userRegisterDto,baseUrl);

        return ResponseEntity.ok(new UserListDto(response));
    }

    @PostMapping("/registerAdmin")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<UserListDto> createUserAdmin(@RequestBody @Valid UserRegisterDto userRegisterDto)
    {
        var response = userService.createUserAdmin(userRegisterDto);

        return ResponseEntity.ok(new UserListDto(response));
    }

    @PostMapping("/registerModerator")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<UserListDto> createUserModerator(@RequestBody @Valid UserRegisterDto userRegisterDto)
    {
        String baseUrl = httpServletRequest.getRequestURL().toString().replace("/users/registerModerator", "");

        var response = userService.createUserModerator(userRegisterDto,baseUrl);

        return ResponseEntity.ok(new UserListDto(response));
    }

    @GetMapping("/confirm")
    public ResponseEntity<AccountConfirmDto> confirmUser(@RequestParam String token)
    {
        var user = userService.confirmUser(token);

        return ResponseEntity.ok(new AccountConfirmDto("Bem vindo," + user.getFirstName() + "! Conta confirmada com sucesso!"));
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<UserListDto> GetUserById(@PathVariable Long id)
    {
        var response = userService.findById(id);

        return ResponseEntity.ok(new UserListDto(response));
    }

}
