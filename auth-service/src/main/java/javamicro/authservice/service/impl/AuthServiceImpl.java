package javamicro.authservice.service.impl;

import javamicro.authservice.repository.UserRepository;
import javamicro.authservice.dto.KeycloakTokenResponse;
import javamicro.authservice.entity.User;
import javamicro.authservice.exception.AuthException;
import javamicro.authservice.model.*;
import javamicro.authservice.service.AuthService;
import javamicro.authservice.service.KeycloakAdminClientService;
import javamicro.authservice.service.KeycloakAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KeycloakAdminClientService keycloakAdminClientService;
    private final KeycloakAuthService keycloakAuthService;
    private final UserRepository userRepository;

    @Override
    public KeycloakTokenResponse login(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
        if (user.isEmpty()) {
            throw new AuthException("User not found!");
        }
        return keycloakAuthService.getToken(user.get().getUsername(), loginRequest.getPassword());

    }

    @Override
    public String register(RegisterRequest registerRequest) {
        Optional<User> user = userRepository.findByUsername(registerRequest.getUsername());
        if (user.isPresent()) {
            throw new AuthException("User already exists!");
        }
        keycloakAdminClientService.createKeycloakUser(registerRequest);
        return "User registered successfully!";
    }

}
