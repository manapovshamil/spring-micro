package javamicro.authservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String url;
    private String authUrl;
    private String realm;
    private String clientId;
    private String username;
    private String password;
    private String grantType;
    private String clientSecret;

}
