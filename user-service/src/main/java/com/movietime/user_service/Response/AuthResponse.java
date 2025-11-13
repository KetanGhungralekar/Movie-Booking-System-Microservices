package com.movietime.user_service.Response;
import com.movietime.user_service.Model.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private USER_ROLE role;
    private String message;
}