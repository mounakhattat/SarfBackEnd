package trivaw.stage.sarf.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface IResetPasswordService {
        void resetPassword(String email, String newPassword) throws UsernameNotFoundException;
    void sendSimpleMessage(String to, String subject, String text);

    }

