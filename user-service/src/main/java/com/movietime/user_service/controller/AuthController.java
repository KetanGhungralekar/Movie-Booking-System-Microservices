package com.movietime.user_service.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.movietime.user_service.Config.JwtProvider;
import com.movietime.user_service.Model.USER_ROLE;
import com.movietime.user_service.Model.User;
import com.movietime.user_service.Repository.UserRepository;
import com.movietime.user_service.Request.LoginRequest;
import com.movietime.user_service.Response.AuthResponse;
import com.movietime.user_service.Service.CustomUserDetailService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    // ---------- SIGNUP ----------
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(USER_ROLE.USER); // default
        }

        User savedUser = userRepository.save(user);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(savedUser.getRole().name()));

        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        savedUser.getEmail(),
                        savedUser.getPassword(),
                        authorities
                );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setRole(savedUser.getRole());
        response.setMessage("Signup successful");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ---------- SIGNIN ----------
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest req) {

        String email = req.getEmail();
        String password = req.getPassword();

        try {
            Authentication authentication = authenticate(email, password);
            String jwt = jwtProvider.generateToken(authentication);

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

            AuthResponse response = new AuthResponse();
            response.setToken(jwt);
            response.setRole(USER_ROLE.valueOf(role));
            response.setMessage("Login successful");

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private Authentication authenticate(String email, String password) throws Exception {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("User not found");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
