package trivaw.stage.sarf.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CodeConfirmationService {

    private static final int CODE_LENGTH = 6;

    public String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomNum = random.nextInt(10);
            sb.append(randomNum);
        }
        return sb.toString();
    }

    public boolean verifyCode(String code, String u) {
        return code.equals(u);
    }
}
