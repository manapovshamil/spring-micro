package javamicro.userservice.services.impl;

import javamicro.userservice.entities.User;
import javamicro.userservice.repositories.UserRepository;
import javamicro.userservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
