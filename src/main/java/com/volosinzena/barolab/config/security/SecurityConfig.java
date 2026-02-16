package com.volosinzena.barolab.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

        private final TokenAuthFilter tokenAuthFilter;

        @Bean
        UserDetailsService emptyDetailsService() {
                return username -> {
                        throw new UsernameNotFoundException("No local users, only JWT tokens allowed!");
                };
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
                return httpSecurity
                                .securityContext(
                                                securityContext -> securityContext
                                                                .requireExplicitSave(true)
                                                                .securityContextRepository(
                                                                                new DelegatingSecurityContextRepository(
                                                                                                new RequestAttributeSecurityContextRepository(),
                                                                                                new HttpSessionSecurityContextRepository())))
                                .authorizeHttpRequests(
                                                requests -> requests
                                                                .requestMatchers(
                                                                                "/ping",
                                                                                "/login",
                                                                                "/sign-up",
                                                                                "/swagger-ui/**",
                                                                                "/v3/api-docs/**")
                                                                .permitAll()
                                                                // SUPER_ADMIN only
                                                                .requestMatchers(
                                                                                "/user/*/role",
                                                                                "/user/*/activate",
                                                                                "/user/*/block")
                                                                .hasAuthority("SUPER_ADMIN")
                                                                // ADMIN and above
                                                                .requestMatchers(
                                                                                "/post/*/activate",
                                                                                "/post/*/block",
                                                                                "/post/*/comment/*/activate",
                                                                                "/post/*/comment/*/block")
                                                                .hasAuthority("ADMIN")
                                                                // SUPERUSER and above
                                                                .requestMatchers(
                                                                                "/users")
                                                                .hasAuthority("SUPERUSER")
                                                                // USER and above (Authenticated)
                                                                .requestMatchers(
                                                                                "/user/**",
                                                                                "/post/**")
                                                                .hasAuthority("USER")
                                                                .anyRequest()
                                                                .authenticated())
                                .cors(
                                                cors -> cors.configurationSource(
                                                                request -> {
                                                                        CorsConfiguration configuration = new CorsConfiguration();
                                                                        configuration.setAllowedOrigins(List.of("*"));
                                                                        configuration.setAllowedMethods(List.of("*"));
                                                                        configuration.setAllowedHeaders(List.of("*"));
                                                                        return configuration;
                                                                }))
                                .csrf(AbstractHttpConfigurer::disable)
                                .addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(
                                                e -> e.authenticationEntryPoint(
                                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                                .build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
