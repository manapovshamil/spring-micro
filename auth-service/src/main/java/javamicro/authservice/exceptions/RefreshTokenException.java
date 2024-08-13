package javamicro.authservice.exceptions;

import java.text.MessageFormat;

public class RefreshTokenException extends RuntimeException{
    public RefreshTokenException(String message) {
        super(message);
    }
    public RefreshTokenException(String token, String message) {
        super(MessageFormat.format("Error trying to refresh token {0} : {1}", token, message));
    }
}
