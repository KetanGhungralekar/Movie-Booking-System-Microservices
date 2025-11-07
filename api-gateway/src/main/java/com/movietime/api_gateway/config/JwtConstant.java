package com.movietime.api_gateway.config;

public class JwtConstant {
    // Note: This mirrors the secret currently used in user-service.
    // Long-term: move this to an environment property (e.g. jwt.secret) and share via docker-compose.
    public static final String SECRET_KEY = "fdjlkjflafjldjlnrrrrfdshihfiaghivvniafduhivabiughviafbviarsga";
    public static final String JWT_HEADER = "Authorization";
}
