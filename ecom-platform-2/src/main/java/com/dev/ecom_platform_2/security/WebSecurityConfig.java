package com.dev.ecom_platform_2.security;

import com.dev.ecom_platform_2.domain.entities.AppRole;
import com.dev.ecom_platform_2.domain.entities.Role;
import com.dev.ecom_platform_2.domain.entities.User;
import com.dev.ecom_platform_2.repositories.RoleRepository;
import com.dev.ecom_platform_2.repositories.UserRepository;
import com.dev.ecom_platform_2.security.jwt.AuthEntryPointJwt;
import com.dev.ecom_platform_2.security.jwt.AuthTokenFilter;
import com.dev.ecom_platform_2.security.services.UserDetailsServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
//@EnableMethodSecurity enables method-level security annotations, such as @PreAuthorize, @Secured.
//
//@PreAuthorize: Used to express security rules before a method is executed.
//@PreAuthorize("hasRole('ADMIN')")
//public void adminMethod() {
//    // only accessible by users with the 'ADMIN' role
//}
//
//@Secured: Specifies a list of roles allowed to access the method.
//@Secured("ROLE_ADMIN")
//public void adminMethod() {
//    // only accessible by users with the 'ROLE_ADMIN' role
//}
//
//@RolesAllowed: Another way to restrict method access to specific roles (common in Java EE).
//@RolesAllowed("ROLE_USER")
//public void userMethod() {
//    // only accessible by users with the 'ROLE_USER' role
//}

public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    /**
     * Defines a bean for the JWT authentication token filter.
     * <p>
     * This filter intercepts incoming HTTP requests and checks for a valid
     * JWT token in the Authorization header. If a valid token is found,
     * it sets the authentication in the security context.
     *
     * @return an instance of {@link AuthTokenFilter}
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Configures and provides a {@link DaoAuthenticationProvider} bean.
     * <p>
     * The {@code DaoAuthenticationProvider} is used by Spring Security to retrieve
     * user details and validate credentials during authentication, to authenticate users.
     * <p>
     * Used when we what to use a custom UserDetailsServiceImpl.
     * It is configured with a custom {@code UserDetailsService} and a password encoder.
     * <p>
     * * @return a configured instance of {@link DaoAuthenticationProvider}
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Exposes the {@link AuthenticationManager} bean.
     * <p>
     * Retrieves the {@code AuthenticationManager} from the provided
     * {@link AuthenticationConfiguration}.
     * This manager is responsible for processing authentication requests.
     *
     * @param authConfig the authentication configuration from which to retrieve the manager
     * @return the {@link AuthenticationManager} used by Spring Security
     * @throws Exception if the authentication manager cannot be retrieved
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures and exposes the Spring Security filter chain.
     * <p>
     * This method defines the core security configuration for the application, including:
     *
     * <ul>
     *   <li><b>CSRF Disabled:</b> CSRF protection is disabled since the application is stateless and likely uses tokens for authentication.</li>
     *   <li><b>Exception Handling:</b> A custom {@code AuthenticationEntryPoint} is used to handle unauthorized access attempts.</li>
     *   <li><b>Stateless Sessions:</b> Session creation is disabled to enforce token-based authentication (e.g., JWT).</li>
     *   <li><b>Public Endpoints:</b> Specific endpoints (auth APIs, documentation, H2 console, images, etc.) are accessible without authentication.</li>
     *   <li><b>Protected Endpoints:</b> All other endpoints require authentication.</li>
     *   <li><b>Authentication Provider:</b> A custom {@link DaoAuthenticationProvider} is registered to handle authentication logic using a user details service and password encoder.</li>
     *   <li><b>JWT Filter:</b> A custom JWT token filter is added to the security filter chain before the default {@link UsernamePasswordAuthenticationFilter}.</li>
     *   <li><b>Frame Options:</b> Allows use of the H2 console by setting frame options to {@code SAMEORIGIN}.</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs while building the security configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // disable CSRF for stateless APIs
                .csrf(AbstractHttpConfigurer::disable)
//                .csrf(csrf -> csrf.ignoringRequestMatchers(new CustomCsrfIgnoreRequestMatcher()))

                // handle unauthorized access with a custom entry point
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(unauthorizedHandler)
                )

                // use stateless session management
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // define authorization rules
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(
                                        "/api/auth/**",
                                        "/api/public/**",
                                        "/api/test/**",
                                        "/images/**",
                                        "/h2-console/**"
                                        // Swagger-related paths are now handled by WebSecurityCustomizer and don't need to be here
                                ).permitAll()
                                .requestMatchers("/api/admin/**").permitAll() // need to be disabled for prod
                                .anyRequest().authenticated()
                )

                // register authentication provider
                .authenticationProvider(authenticationProvider())

                // add JWT authentication filter before the default username/password filter
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)

                // allow H2 console by permitting same-origin framing
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        return http.build();
    }

//    public static class CustomCsrfIgnoreRequestMatcher implements RequestMatcher {
    /// /        private final List<RequestMatcher> matchers = List.of(
    /// /                new AntPathRequestMatcher("/api/**")
    /// ///                new AntPathRequestMatcher("/webhook/**")
    /// /        );
//
//        private final List<RequestMatcher> matchers = List.of(
//                new AntPathRequestMatcher("/api/v1/admin/categories", HttpMethod.POST.name())
//        );
//
//        @Override
//        public boolean matches(HttpServletRequest request) {
//            return matchers.stream().anyMatch(matcher -> matcher.matches(request));
//        }
//    }

    /**
     * Configures Spring Security to ignore security filters at global level.
     * These paths are made publicly accessible
     * without requiring authentication or any security-related checks.
     * <p>
     * These exclusions ensure that users can freely access the Swagger UI and API documentation
     * without needing to authenticate or go through security filters.
     *
     * @return A WebSecurityCustomizer instance that configures the security to ignore
     * the specified Swagger-related paths.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web ->
                web.ignoring().requestMatchers(
                        "/v2/api-docs",              // Swagger API docs
                        "/configuration/ui",         // Swagger UI config
                        "/swagger-resources/**",     // Swagger resources
                        "/configuration/security",   // Swagger security config
                        "/swagger-ui.html",          // Swagger UI page
                        "/webjars/**"                // Swagger UI webjars
                ));
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role sellerRole = roleRepository.findByName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);

            // Create users if not already present
            if (userRepository.existsByUsername("user1")) {
                User user1 = User.builder()
                        .username("user1")
                        .email("user1@example.com")
                        .password(passwordEncoder.encode("test1234"))
                        .build();

                userRepository.save(user1);
            }

            if (userRepository.existsByUsername("seller1")) {
                User seller1 = User.builder()
                        .username("seller1")
                        .email("seller1@example.com")
                        .password(passwordEncoder.encode("test1234"))
                        .build();

                userRepository.save(seller1);
            }

            if (userRepository.existsByUsername("admin")) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("test1234"))
                        .build();

                userRepository.save(admin);
            }

            // Update roles for existing users
            userRepository.findByUsername("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }

}