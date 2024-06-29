package trivaw.stage.sarf.services;

import trivaw.stage.sarf.Entities.PasswordResetToken;
import trivaw.stage.sarf.Exception.UserNotFoundException;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IResetPasswordToken {
    PasswordResetToken CreatePasswordToken(String email) throws UserNotFoundException, UnsupportedEncodingException, MessagingException;
    boolean VerifyExpiration(String token);
    void ConfirmPasswordReset(String token,String password);
    Integer getUserIdFromSession();
    List<PasswordResetToken> getExpireToken();

    void deleteToken(Long id);
     boolean updatePassword( String newPassword,String email);
}
