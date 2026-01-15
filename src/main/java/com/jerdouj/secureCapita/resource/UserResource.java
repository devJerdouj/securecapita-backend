package com.jerdouj.secureCapita.resource;


import com.jerdouj.secureCapita.domain.HttpResponse;
import com.jerdouj.secureCapita.domain.User;
import com.jerdouj.secureCapita.dto.UserDTO;
import com.jerdouj.secureCapita.form.LoginForm;
import com.jerdouj.secureCapita.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping(path= "/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
        UserDTO user = userService.getUserByEmail(loginForm.getEmail());
        return user.isUsingMfa() ? sendVerificattionCode(user) : sendLoginResponse(user);

    }




    @PostMapping("/register")
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user){
        UserDTO userDTO = userService.createUser(user);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timestamp(Instant.now().toString())
                        .data(of("user", userDTO))
                        .message("User created successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }

    private ResponseEntity<HttpResponse> sendLoginResponse(UserDTO user) {
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timestamp(Instant.now().toString())
                        .data(of("user", user))
                        .message("Logged in successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    private ResponseEntity<HttpResponse> sendVerificattionCode(UserDTO user) {
        userService.sendMfaCode(user);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timestamp(Instant.now().toString())
                        .data(of("user", user))
                        .message("Verification code sent")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
