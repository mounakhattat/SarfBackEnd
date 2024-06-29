package trivaw.stage.sarf.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ComfirmationEmailService {
    private static final int CODE_LENGTH = 10  ;

    public String generateCodeEmail() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomNum = random.nextInt(10);
            sb.append(randomNum);
        }
        return sb.toString();
    }
    public boolean verifyCodeEmail(String confirmationCode, String expectedCode) {
        return confirmationCode.equals(expectedCode);
    }
}

