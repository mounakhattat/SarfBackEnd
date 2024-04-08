package trivaw.stage.sarf.services;

import org.springframework.http.ResponseEntity;
import trivaw.stage.sarf.Entities.BureauDeChange;
import trivaw.stage.sarf.Request.SignUp;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IBureauDeChangeServices {
    List<BureauDeChange> getAllBureauDeChange();
    BureauDeChange getBureauDeChangeById(Integer idBureauDeChange);
    BureauDeChange createBureauDeChange(BureauDeChange a);
    BureauDeChange updateBureauDeChange(Integer idBureauDeChange, BureauDeChange a);
    void deleteBureauDeChange(Integer idBureauDeChange);
    BureauDeChange   getBureauDeChangeByUser(Integer idUser) ;
    BureauDeChange  updateBureauDeChangeByUser (Integer idUser, BureauDeChange bureauDeChange);
    ResponseEntity<?> registerUserAndAssignBureau(SignUp signUpRequest) throws MessagingException, UnsupportedEncodingException;
}
