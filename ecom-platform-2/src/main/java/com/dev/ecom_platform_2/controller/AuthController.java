package com.dev.ecom_platform_2.controller;

import com.dev.ecom_platform_2.domain.entities.AppRole;
import com.dev.ecom_platform_2.domain.entities.Role;
import com.dev.ecom_platform_2.domain.entities.User;
import com.dev.ecom_platform_2.exception.ApiResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
            var httpStatus = HttpStatus.UNAUTHORIZED.value();
            var response = ApiResponse.builder()
                    .status(httpStatus)
                    .message("Invalid username or password.")
                    .build();

            return ResponseEntity.status(httpStatus).body(response);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        var httpStatus = HttpStatus.OK.value();
        UserInfoResponse response = UserInfoResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(getRolesFromUserDetails(userDetails.getAuthorities()))
                .jwtToken(jwtUtils.generateTokenFromUsername(userDetails))
                .build();

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            var httpStatus = HttpStatus.CONFLICT.value();
            var response = ApiResponse.builder()
                    .status(httpStatus)
                    .message("Error: Username is already taken!")
                    .build();

            return ResponseEntity.status(httpStatus).body(response);

        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            var httpStatus = HttpStatus.CONFLICT.value();
            var response = ApiResponse.builder()
                    .status(httpStatus)
                    .message("Error: Email is already in use!")
                    .build();

            return ResponseEntity.status(httpStatus).body(response);
        }

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        Set<String> rolesFromTheRequest = signUpRequest.getRole();
        Set<Role> roles = getRolesFromRequest(rolesFromTheRequest, roleRepository);

        user.setRoles(roles);

        userRepository.save(user);

        var httpStatus = HttpStatus.OK.value();
        var response = ApiResponse.builder()
                .status(httpStatus)
                .message("User registered successfully!")
                .build();

        return ResponseEntity.status(httpStatus).body(response);
    }

    @GetMapping("/username")
    public ResponseEntity<String> currentUserName(Authentication authentication) {
        var httpStatus = HttpStatus.OK.value();
        System.out.println(authentication);
        if (authentication != null)
            return ResponseEntity.status(httpStatus).body(authentication.getName());
        else
            return ResponseEntity.status(httpStatus).body(null);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            var httpStatus = HttpStatus.UNAUTHORIZED.value();
            var response = ApiResponse.builder()
                    .status(httpStatus)
                    .message("Unauthorized: No user is authenticated.")
                    .build();

            return ResponseEntity.status(httpStatus).body(response);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = getRolesFromUserDetails(userDetails.getAuthorities());

        var httpStatus = HttpStatus.OK.value();
        UserInfoResponse response = UserInfoResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<ApiResponse> signoutUser() {
        var httpStatus = HttpStatus.CONFLICT.value();
        var response = ApiResponse.builder()
                .status(httpStatus)
                .message("Error: Username is already taken!")
                .build();

        return ResponseEntity.status(httpStatus).body(response);
    }

    // HELPERS
    private Set<Role> getRolesFromRequest(Set<String> rolesFromTheRequest, RoleRepository roleRepository) {
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

        return roles;
    }

    private List<String> getRolesFromUserDetails(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

    }
}
