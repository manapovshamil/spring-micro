package javamicro.userservice.services;

import javamicro.userservice.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
}
