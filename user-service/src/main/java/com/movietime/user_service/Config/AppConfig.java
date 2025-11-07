package com.movietime.user_service.Config;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class AppConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // http.sessionManagement(management->management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(authorize->authorize.requestMatchers("/api/admin/**").hasAnyRole("ADMIN","RESTAURANT_OWNER").requestMatchers("/api/**").authenticated().anyRequest().permitAll()).csrf(csrf->csrf.disable());
    http
    .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN")
        .requestMatchers("/api/**").authenticated()
        .anyRequest().permitAll()
    )
    .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class).csrf(csrf->csrf.disable()).cors(cors->cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }
    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource(){
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request){
                CorsConfiguration cors = new CorsConfiguration();
                cors.setAllowedOriginPatterns(Collections.singletonList("*"));// which origins are allowed // * means all origins are allowed
                cors.setAllowedMethods(Collections.singletonList("*")); //
                cors.setAllowedHeaders(Collections.singletonList("*"));// which headers are allowed in the request // * means all headers are allowed like authorization, content-type etc
                cors.setExposedHeaders(Arrays.asList("Authorization")); // which headers are allowed in the response
                cors.setAllowCredentials(true); 
                cors.setMaxAge(3600L);
                return cors;
            }
        };
    }
    @Bean
    PasswordEncoder passwordEncoder(){ 
        return new BCryptPasswordEncoder(); //whenever we want to encode password we will use this object of BCryptPasswordEncoder
    }
}