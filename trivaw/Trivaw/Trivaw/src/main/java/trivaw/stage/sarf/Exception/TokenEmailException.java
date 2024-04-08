package trivaw.stage.sarf.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenEmailException extends RuntimeException{

    public TokenEmailException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}


