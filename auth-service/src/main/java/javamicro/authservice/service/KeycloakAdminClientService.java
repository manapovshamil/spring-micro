package javamicro.authservice.service;

import jakarta.ws.rs.core.Response;
import javamicro.authservice.repository.UserRepository;
import javamicro.authservice.config.KeycloakProvider;
import javamicro.authservice.entity.User;
import javamicro.authservice.exception.AuthException;
import javamicro.authservice.model.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakAdminClientService {

    @Value("${keycloak.realm}")
    public String realm;
    private final KeycloakProvider keycloakProvider;
    private final UserRepository userRepository;

    public void createKeycloakUser(RegisterRequest registerRequest) {
        try {
            UsersResource usersResource = keycloakProvider.getInstance().realm(realm).users();
            CredentialRepresentation credentialRepresentation = createPasswordCredentials(registerRequest.getPassword());
            UserRepresentation kcUser = new UserRepresentation();
            kcUser.setUsername(registerRequest.getUsername());
            kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
            kcUser.setFirstName(registerRequest.getFirstName());
            kcUser.setLastName(registerRequest.getLastName());
            kcUser.setEmail(registerRequest.getEmail() );
            kcUser.setEnabled(true);
            kcUser.setEmailVerified(false);
            kcUser.setRequiredActions(Collections.emptyList());

            Response response = usersResource.create(kcUser);

            if (response.getStatus() != 201) {
                throw new AuthException("Failed : HTTP error code : " + response.getStatus());
            }

            String userId = CreatedResponseUtil.getCreatedId(response);
            ClientRepresentation client = keycloakProvider.getInstance()
                    .realm(realm)
                    .clients()
                    .findByClientId("security-micro")
                    .get(0);
            String clientUUID = client.getId();

            RoleRepresentation clientRole = keycloakProvider.getInstance()
                    .realm(realm)
                    .clients()
                    .get(clientUUID)
                    .roles()
                    .get("ROLE_USER")
                    .toRepresentation();

            usersResource.get(userId)
                    .roles()
                    .clientLevel(clientUUID)
                    .add(Collections.singletonList(clientRole));

            var user = User.builder()
                    .username(registerRequest.getUsername())
                    .password(registerRequest.getPassword())
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .email(registerRequest.getEmail())
                    .createDate(new Date())
                    .build();

            userRepository.save(user);

        }catch (Exception ex) {
            log.error("Error creating Keycloak user: {}", ex.getMessage());
            throw new AuthException(ex.getMessage());
        }

    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }


}
