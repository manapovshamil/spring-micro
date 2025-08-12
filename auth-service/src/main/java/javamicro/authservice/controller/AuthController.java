package javamicro.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javamicro.authservice.dto.KeycloakTokenResponse;
import javamicro.authservice.exception.ErrorResponse;
import javamicro.authservice.model.*;
import javamicro.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auth", description = "Авторизация и регистрация")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Авторизация пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = KeycloakTokenResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Ошибка", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping(value = "/login")
    public ResponseEntity<KeycloakTokenResponse> login(@RequestBody LoginRequest request) {
        log.info("login request: {}", request);
        return ResponseEntity.ok( authService.login(request));
    }

    @Operation(summary = "Регистрация нового пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "500", description = "Ошибка", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        log.info("register request: {}", request);
        return ResponseEntity.ok(authService.register(request));
    }


}


