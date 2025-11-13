package com.movietime.user_service.Request;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}