package com.movietime.movie_service.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) { super(msg); }
}
