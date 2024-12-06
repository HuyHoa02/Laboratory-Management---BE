    package com.chris.LaboratoryManagement.configuration;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.AuthenticationServiceException;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
    import org.springframework.security.oauth2.jwt.Jwt;
    import org.springframework.security.oauth2.jwt.JwtDecoder;
    import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
    import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
    import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    import javax.crypto.spec.SecretKeySpec;
    import java.time.Instant;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Collections;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    @Slf4j
    public class SecurityConfig {

        private final String[] PUBLIC_ENDPOINTS = {
                "/auth/signin",
                "/auth/refreshToken",
                "/auth/verify-email",
                "/public/get-course",
                "/public/get-class",
                "/public/get-class/**",
                "/public/get-laboratory",
                "/public/get-agenda/**",
                "/public/get-timeset/**",
                "/public/get-all-timesets",
                "/public/get-session",
                "/public/get-semester",
                "/public/get-lecturers",
                "/public/get-agenda-by-input",
        };

        private final String[] LECTURER_ENDPOINTS = {
                "/lecturers/add-registrations",
                "/lecturers/add-registration",
                "/lecturers/update-password"
        };

        private final String[] ADMIN_ENDPOINTS = {
                "/admin/get-registration",
                "/update-lecturer/**",
                "/admin/add-class",
                "/admin/delete-class/**",
                "/admin/add-laboratory",
                "/admin/update-laboratory",
                "/admin/delete-laboratory/**",
                "/admin/add-course",
                "/admin/update-course",
                "/admin/delete-course/**",
                "/admin/add-lecturer",
                "/admin/delete-lecturer/**",
                "/admin/get-registration",
                "/admin/add-agenda",
                "/admin/reject-registration"
        };

        @Value("${jwt.signerKey}")
        private String signerKey;


        private final JwtAuthenticationEntryPoint customAuthenticationEntryPoint;

        // Constructor injection for JwtAuthenticationEntryPoint
        public SecurityConfig(JwtAuthenticationEntryPoint customAuthenticationEntryPoint) {
            this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        }


        @Bean
        JwtDecoder jwtDecoder() {
            SecretKeySpec secretKey = new SecretKeySpec(signerKey.getBytes(), "HS512");
            NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey)
                    .macAlgorithm(MacAlgorithm.HS512).build();

            return token -> {
                Jwt jwt = jwtDecoder.decode(token);
                var expiresAt = jwt.getExpiresAt();
                if (expiresAt != null && Instant.now().isAfter(expiresAt)) {
                    // Wrap JwtExpiredException in AuthenticationServiceException
                    throw new JwtExpiredException("Jwt expired");
                }
                return jwt;
            };
        }




        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .authorizeRequests(request -> request
                            .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                            .requestMatchers(LECTURER_ENDPOINTS).hasAuthority("ROLE_LECTURER")
                            .requestMatchers(ADMIN_ENDPOINTS).hasAuthority("ROLE_ADMIN")
                            .anyRequest().authenticated()
                    )
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwtConfigurer ->
                                    jwtConfigurer.decoder(jwtDecoder())  // Use your custom JWT decoder here
                                            .jwtAuthenticationConverter(jwtAuthenticationConverter()) // Use custom converter for authorities
                            )
                            .authenticationEntryPoint(new JwtAuthenticationEntryPoint())  // Handle errors
                    )
                    .cors(c -> c.configurationSource(corsConfigurationSource()))
                    .csrf(AbstractHttpConfigurer::disable);  // Disable CSRF for stateless JWT authentication

            return httpSecurity.build();
        }



        @Bean
        JwtAuthenticationConverter jwtAuthenticationConverter() {
            JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
            jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

            JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
            jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
            return  jwtAuthenticationConverter;
        };

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();

            // Allow only requests from your frontend (adjust as needed for production)
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));

            // Allow any headers
            configuration.addAllowedHeader("*");

            // Allow any HTTP method (GET, POST, etc.)
            configuration.addAllowedMethod("*");

            // Allow credentials (cookies, authorization headers)
            configuration.setAllowCredentials(true); // This is crucial for enabling credentials!

            // Cache preflight response for 1 hour
            configuration.setMaxAge(3600L);

            // Apply the CORS configuration to all endpoints
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);

            return source;
        }





        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(10);
        }
    }