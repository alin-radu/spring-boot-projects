package com.dev.ecom_platform_2.controller;

import com.dev.ecom_platform_2.domain.entities.AppRole;
import com.dev.ecom_platform_2.domain.entities.Role;
import com.dev.ecom_platform_2.domain.entities.User;
import com.dev.ecom_platform_2.exception.ApiErrorResponse;
import com.dev.ecom_platform_2.repositories.RoleRepository;
import com.dev.ecom_platform_2.repositories.UserRepository;
import com.dev.ecom_platform_2.security.jwt.JwtUtils;
import com.dev.ecom_platform_2.security.request.LoginRequest;
import com.dev.ecom_platform_2.security.request.SignupRequest;
import com.dev.ecom_platform_2.security.response.UserInfoResponse;
import com.dev.ecom_platform_2.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, PasswordEncoder encoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
                    );
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);

            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles, jwtToken);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            var response = ApiErrorResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Error: Username is already taken!");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            var response = ApiErrorResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Error: Email is already in use!");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        Set<String> rolesFromTheRequest = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (rolesFromTheRequest == null) {
            Role userRole = roleRepository.findByName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            rolesFromTheRequest.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;

                    case "seller":
                        Role modRole = roleRepository.findByName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;

                    default:
                        Role userRole = roleRepository.findByName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);

        userRepository.save(user);

        var response = ApiErrorResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User registered successfully!");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
