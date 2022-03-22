package testwork.throttling.throttler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
public class ThrottlerException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ThrottlerException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ThrottlerException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
