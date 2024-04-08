package trivaw.stage.sarf.services;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import trivaw.stage.sarf.Entities.PasswordResetToken;
import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.Exception.TokenEmailException;
import trivaw.stage.sarf.Exception.UserNotFoundException;
import trivaw.stage.sarf.repository.ResetPasswordRepository;
import trivaw.stage.sarf.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ResetPasswordTokenService implements IResetPasswordToken {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResetPasswordRepository resetPasswordRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;
    @Value("${pass.PassTokenDurationMs}")
    private long passTokenDurationMs;
    private final String FromAddress = "mouna.khattat@esprit.tn";
    private final String SenderName = "SARF Team";


    @Override
    public PasswordResetToken CreatePasswordToken(String email) throws UserNotFoundException, UnsupportedEncodingException, MessagingException {

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        User user = userRepository.findByEmail(email);
        session.setAttribute("userId", user.getIdUser());
        System.out.print(user.getIdUser()+"jjjjjjjjjjjj");

        passwordResetToken.setExpireDate(Instant.now().plusSeconds(passTokenDurationMs));
        passwordResetToken.setToken(UUID.randomUUID().toString());
        String toAddress;
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"[[URL]]\" >Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        if (user != null) {
            passwordResetToken.setUserPass(user);
            resetPasswordRepository.save(passwordResetToken);
            toAddress = user.getEmail();
            String verifyURL = "http://localhost:4200/newpass" ;
            content = content.replace("[[URL]]", verifyURL);


        } else
            throw new UserNotFoundException("Could not find any user with the email" + email);

        resetPasswordRepository.save(passwordResetToken);
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
System.out.println(message);
        helper.setFrom(FromAddress, SenderName);
        helper.setTo(toAddress);
        helper.setSubject("Password Reset");
        helper.setText(content, true);

        emailSender.send(message);

        return passwordResetToken;

    }

    @Override
    public boolean VerifyExpiration(String token) {
        PasswordResetToken passwordResetToken = resetPasswordRepository.findByToken(token);
        if (passwordResetToken != null) {
            if (passwordResetToken.getExpireDate().isBefore(Instant.now())) {
                resetPasswordRepository.delete(passwordResetToken);

                throw new TokenEmailException(token, "this token was expired");
            }
            return true;
        } else
            throw new TokenEmailException(token, "this token is not in the database");
    }
    @Override
    public void ConfirmPasswordReset(String token, String password) {
        PasswordResetToken passwordResetToken = resetPasswordRepository.findByToken(token);
        System.out.print(passwordResetToken);
        VerifyExpiration(token);
        User user = passwordResetToken.getUsertPass();
        if (user != null) {
            userService.UpdatePassword(user, password);
            System.out.print(password);
        }
        else {
            System.out.print(password);
        }
    }
    @Override
    public boolean updatePassword(String newPassword,String email) {
        // Récupérer l'ID de l'utilisateur à partir de la session
        Integer userId = getUserIdFromSession();
        System.out.println(userId + "pfffffff");

        // Vérifier si l'ID de l'utilisateur est null


        // Recherche de l'utilisateur dans la base de données
        User user = userRepository.findByEmail(email);
        System.out.println(email + "pfffffff");
        System.out.println(newPassword + "oooooo");
        // Encoder le nouveau mot de passe
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(newPassword);

        // Mettre à jour le mot de passe de l'utilisateur dans la base de données
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // Retourner true pour indiquer que le mot de passe a été mis à jour avec succès
        return true;
    }
    @Override
    public Integer getUserIdFromSession() {
        return (Integer) session.getAttribute("userId");
    }
    @Override
    public List<PasswordResetToken> getExpireToken() {

        return resetPasswordRepository.findExpireToken(Instant.now());
    }

    @Override
    public void deleteToken(Long id) {
        resetPasswordRepository.deleteById(id);

    }
}