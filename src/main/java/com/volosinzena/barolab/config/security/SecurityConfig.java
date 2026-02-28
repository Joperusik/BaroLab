package com.volosinzena.barolab.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
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
                                                                // Public endpoints (no auth required)
                                                                .requestMatchers(
                                                                                "/ping",
                                                                                "/login",
                                                                                "/sign-up",
                                                                                "/swagger-ui/**",
                                                                                "/v3/api-docs/**",
                                                                                "/mod/*/transition")
                                                                .permitAll()

                                                                // Guest Access: GET requests for viewing posts and
                                                                // comments
                                                                .requestMatchers(HttpMethod.GET,
                                                                                "/posts",
                                                                                "/post/*",
                                                                                "/post/*/comment",
                                                                                "/post/*/comment/*",
                                                                                "/mods",
                                                                                "/mod/*",
                                                                                "/mod/*/guide",
                                                                                "/mod/*/comment",
                                                                                "/mod/*/comment/*")
                                                                .permitAll()

                                                                // SUPER_ADMIN — user management (role, activate, block)
                                                                .requestMatchers(HttpMethod.PUT,
                                                                                "/user/*/role",
                                                                                "/user/*/activate",
                                                                                "/user/*/block")
                                                                .hasAuthority("SUPER_ADMIN")

                                                                // ADMIN — content moderation (activate/block posts &
                                                                // comments, edit guides)
                                                                .requestMatchers(HttpMethod.PUT,
                                                                                "/post/*/activate",
                                                                                "/post/*/block",
                                                                                "/post/*/comment/*/activate",
                                                                                "/post/*/comment/*/block",
                                                                                "/mod/*/activate",
                                                                                "/mod/*/block",
                                                                                "/mod/*/comment/*/activate",
                                                                                "/mod/*/comment/*/block",
                                                                                "/guides/*/activate",
                                                                                "/guides/*/block")
                                                                .hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                                                                .requestMatchers(HttpMethod.POST,
                                                                                "/mod/*/guide")
                                                                .hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                                                                .requestMatchers(HttpMethod.PUT,
                                                                                "/mod/*/guide/*")
                                                                .hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                                                                .requestMatchers(HttpMethod.DELETE,
                                                                                "/mod/*/guide/*")
                                                                .hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                                                                .requestMatchers(HttpMethod.GET,
                                                                                "/guides")
                                                                .hasAnyAuthority("ADMIN", "SUPER_ADMIN")

                                                                // USER — read access to users + create
                                                                // posts and comments
                                                                .requestMatchers(HttpMethod.GET,
                                                                                "/users",
                                                                                "/user/*")
                                                                .hasAnyAuthority("USER", "SUPERUSER", "ADMIN",
                                                                                "SUPER_ADMIN")
                                                                .requestMatchers(HttpMethod.POST,
                                                                                "/posts",
                                                                                "/mods",
                                                                                "/post/*/comment",
                                                                                "/mod/*/comment",
                                                                                "/post/*/like",
                                                                                "/post/*/dislike")
                                                                .hasAnyAuthority("USER", "SUPERUSER", "ADMIN",
                                                                                "SUPER_ADMIN")

                                                                .anyRequest()
                                                                .authenticated())
                                .cors(
                                                cors -> cors.configurationSource(
                                                                request -> {
                                                                        CorsConfiguration configuration = new CorsConfiguration();
                                                                        configuration.setAllowedOriginPatterns(
                                                                                        List.of("*"));
                                                                        configuration.setAllowedMethods(List.of("*"));
                                                                        configuration.setAllowedHeaders(List.of("*"));
                                                                        configuration.setAllowCredentials(true);
                                                                        return configuration;
                                                                }))
                                .csrf(AbstractHttpConfigurer::disable)
                                .addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(
                                                e -> e.authenticationEntryPoint(new LoggingAuthEntryPoint())
                                                                .accessDeniedHandler(new LoggingAccessDeniedHandler()))
                                .build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
