package com.jerdouj.secureCapita.resource;


import com.jerdouj.secureCapita.domain.HttpResponse;
import com.jerdouj.secureCapita.domain.User;
import com.jerdouj.secureCapita.dto.UserDTO;
import com.jerdouj.secureCapita.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

import static java.util.Map.*;
import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping(path= "/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;
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
}
