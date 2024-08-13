package javamicro.authservice.events;

import javamicro.authservice.entity.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisExpirationEvent {

    public void handleRedisExpirationEvent(RedisKeyExpiredEvent<RefreshToken> event) {
        RefreshToken expiredRefreshToken = (RefreshToken) event.getValue();

        if (expiredRefreshToken == null) {
            throw new RuntimeException("Refresh token is null in handleRedisExpirationEvent function!");
        }
        log.info("Expired token with key={} has expired Refresh token is: {}", expiredRefreshToken.getId(), expiredRefreshToken.getToken());
    }
}
