package javamicro.authservice.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@RequiredArgsConstructor
public class KeycloakProvider {

    private final KeycloakProperties keycloakProperties;
    private static Keycloak keycloak = null;

    public Keycloak getInstance() {
        if (keycloak == null) {
            return KeycloakBuilder.builder()
                    .serverUrl(keycloakProperties.getUrl())
                    .realm(keycloakProperties.getRealm())
                    .clientId(keycloakProperties.getClientId())
                    .clientSecret(keycloakProperties.getClientSecret())
                    .grantType(keycloakProperties.getGrantType())
                    .username(keycloakProperties.getUsername())
                    .password(keycloakProperties.getPassword())
                    .build();
        }
        return keycloak;
    }

    /*
    public KeycloakBuilder newKeycloakBuilderWithPasswordCredentials(String username, String password) {
        return KeycloakBuilder.builder()
                .realm(realm)
                .serverUrl(serverURL)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password);
    }

    public JsonNode refreshToken(String refreshToken) throws UnirestException {
        String url = serverURL + "/realms/" + realm + "/protocol/openid-connect/token";
        return Unirest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("client_id", clientID)
                .field("client_secret", clientSecret)
                .field("refresh_token", refreshToken)
                .field("grant_type", "refresh_token")
                .asJson().getBody();
    }*/
}
