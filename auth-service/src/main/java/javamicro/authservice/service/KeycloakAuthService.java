package javamicro.authservice.service;

import com.nimbusds.jose.shaded.gson.Gson;
import javamicro.authservice.config.KeycloakProperties;
import javamicro.authservice.dto.KeycloakTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeycloakAuthService {

    private final KeycloakProperties keycloakProperties;
    private final RestTemplate restTemplate;

    public KeycloakTokenResponse getToken(String username, String password) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", keycloakProperties.getClientId());
        formData.add("client_secret", keycloakProperties.getClientSecret());
        formData.add("username", username);
        formData.add("password", password);
        formData.add("grant_type", keycloakProperties.getGrantType());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(keycloakProperties.getAuthUrl(), request, String.class);
        Gson gson = new Gson();
        KeycloakTokenResponse keycloakTokenResponse = gson.fromJson(response.getBody(), KeycloakTokenResponse.class);
        return keycloakTokenResponse;
    }
}
