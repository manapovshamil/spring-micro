package javamicro.authservice.service;

import javamicro.authservice.model.*;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    SimpleResponse register(RegisterRequest registerRequest);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
    void logout();
}
