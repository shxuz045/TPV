package com.sonia.tpv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/error",
                                                                "/")
                                                .permitAll()
                                                .requestMatchers("/h2-console/**").permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/comandas/**", "/comanda/**", "/comanda",
                                                                "/principal", "/mesas", "/sala", "/recogida",
                                                                "/domicilio", "/domicilio/**", "/comanda-sala", "/api/**",
                                                                "/historial-comandas", "/historial-comandas/**")
                                                .hasAnyRole("CAJERO", "ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .successHandler((request, response, authentication) -> {
                                                        boolean isAdmin = authentication.getAuthorities().stream()
                                                                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                                                        if (isAdmin) {
                                                                response.sendRedirect("/admin/productos");
                                                        } else {
                                                                response.sendRedirect("/sala");
                                                        }
                                                })
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .exceptionHandling(ex -> ex
                                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                                                        if (auth != null && auth.getAuthorities().stream()
                                                                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                                                                response.sendRedirect("/admin/productos");
                                                        } else {
                                                                response.sendRedirect("/sala");
                                                        }
                                                }))
                                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/**"))
                                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

                return http.build();
        }
}
