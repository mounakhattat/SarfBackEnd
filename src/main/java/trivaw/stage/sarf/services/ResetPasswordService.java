package trivaw.stage.sarf.services;

import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResetPasswordService implements IResetPasswordService  {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender emailSender;
    private static final String SUBJECT = "Reset your password";
    private static final String TEXT = "To reset your password, click on this link: ";
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void resetPassword(String email, String newPassword) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
   log.info(user.getPassword());
       // if (user == null) {
      //      throw new UsernameNotFoundException("User not found for email: " + email);
      //  }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        String resetUrl = "http://localhost:8083/api/auth/reset-password" ;
        sendSimpleMessage(email, SUBJECT, TEXT+resetUrl);
    }


    @Override
    public void sendSimpleMessage(String to, String subject, String text) {         // mailing
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}






