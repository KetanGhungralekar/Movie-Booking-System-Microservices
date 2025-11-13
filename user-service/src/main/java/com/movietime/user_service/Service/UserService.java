package com.movietime.user_service.Service;
import com.movietime.user_service.Model.User;

public interface UserService {
    public User FindUserByJwt(String jwt) throws Exception;

    public User FindUserByEmail(String email) throws Exception;
}
