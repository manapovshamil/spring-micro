package javamicro.authservice.service;

import javamicro.authservice.dto.KeycloakTokenResponse;
import javamicro.authservice.model.*;

public interface AuthService {
    KeycloakTokenResponse login(LoginRequest request);
    String register(RegisterRequest registerRequest);
}
