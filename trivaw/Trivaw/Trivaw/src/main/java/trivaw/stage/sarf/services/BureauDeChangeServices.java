package trivaw.stage.sarf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import trivaw.stage.sarf.Entities.*;
import trivaw.stage.sarf.Request.SignUp;
import trivaw.stage.sarf.Responses.MessageResponse;
import trivaw.stage.sarf.repository.BureauDeChangeRepository;
import trivaw.stage.sarf.repository.TauxChangeRepository;
import trivaw.stage.sarf.repository.UserRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class BureauDeChangeServices implements IBureauDeChangeServices {

    @Autowired
    BureauDeChangeRepository bureauDeChangeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    TauxChangeRepository tauxChangeRepository;
    @Autowired
    private JavaMailSender emailSender;
    private final String FromAddress = "mouna.khattat@esprit.tn";
    private final String SenderName = "SARF Team";

    @Override
    public List<BureauDeChange> getAllBureauDeChange() {
        return bureauDeChangeRepository.findAll();
    }
    @Override
    public BureauDeChange getBureauDeChangeById(Integer idBureauDeChange) {
        return bureauDeChangeRepository.findById(idBureauDeChange).orElse(null);
    }

    @Override
    public BureauDeChange createBureauDeChange(BureauDeChange a) {
        return bureauDeChangeRepository.save(a);
    }

    @Override
    public BureauDeChange updateBureauDeChange(Integer idBureauDeChange, BureauDeChange a) {
        BureauDeChange existingBureauDeChange = bureauDeChangeRepository.findById(idBureauDeChange).orElse(null);
        if (existingBureauDeChange != null) {
            existingBureauDeChange.setNom(a.getNom());
            existingBureauDeChange.setLocalisation(a.getLocalisation());
            existingBureauDeChange.setHorrairesDetravail(a.getHorrairesDetravail());
            existingBureauDeChange.setEvaluation(a.getEvaluation());
            return    bureauDeChangeRepository.save(existingBureauDeChange);

        } else {
            return null;
        }
    }

    @Override
    public void deleteBureauDeChange(Integer idBureauDeChange) {
        bureauDeChangeRepository.deleteById(idBureauDeChange);
    }
    @Override
    public BureauDeChange getBureauDeChangeByUser(Integer idUser) {
     return    bureauDeChangeRepository.getBureauDeChangeByUser(idUser);
    }
    @Override
    public BureauDeChange  updateBureauDeChangeByUser (Integer idUser, BureauDeChange a){
        BureauDeChange bureau= bureauDeChangeRepository.getBureauDeChangeByUser(idUser);
        if (bureau != null) {
            bureau.setNom(a.getNom());
            bureau.setEmail(a.getEmail());
            bureau.setCountry(a.getCountry());
            bureau.setPhoneNumber(a.getPhoneNumber());
            bureau.setBannedPeriod(a.getBannedPeriod());
            bureau.setLocalisation(a.getLocalisation());
            bureau.setHorrairesDetravail(a.getHorrairesDetravail());
            bureau.setEvaluation(a.getEvaluation());
            return    bureauDeChangeRepository.save(bureau);

        } else {
            return null;
        }
    }

@Override
    public ResponseEntity<?> registerUserAndAssignBureau(SignUp signUpRequest) throws MessagingException, UnsupportedEncodingException {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

    ERole userRoleEnum = signUpRequest.getRole(); // Get the role directly

    userRoleEnum = ERole.ROLE_BUREAU_DE_CHANGE; // Assign a default role if none provided


    user.setEmail(signUpRequest.getEmail());
    user.setPassword(encoder.encode(signUpRequest.getPassword()));
    user.setUsername(signUpRequest.getUsername());
    user.setRoles(userRoleEnum); // Utilisation de setRoles pour définir le rôle de l'utilisateur
    user.setActived(true);

    userRepository.save(user);

    BureauDeChange bureauDeChange= new BureauDeChange();
    bureauDeChange.setUser(user);
    bureauDeChangeRepository.save(bureauDeChange);
    TauxDeChange tauxDeChange= new TauxDeChange();
    tauxDeChange.setBureauDeChange(bureauDeChange);
    tauxDeChange.setDeviseCible("TND");
    tauxChangeRepository.save(tauxDeChange);



    //  session.setAttribute("expectedCode", code);
    //  session.setAttribute("user", user);

    // Sending email to the user
    String toAddress = user.getEmail();
    System.out.println(toAddress);
    String content = "<p>Hello " + signUpRequest.getUsername() + ",</p>"
            + "<p>Welcome to SARF.tn ! Your account has been successfully created.</p>"
            + "<br>"
            + "<p>Here's your credantials "
            + "<p>Login : " +signUpRequest.getUsername() + "</p>"
            + "<p>Password : " +signUpRequest.getPassword() + "</p>"
            + "<br>"
            + "<p><a href=\"[[URL]]\" >Click to connect: </a></p>"

            + "<p>Thank you,</p>";
    String verifyURL = "http://localhost:4200/page-login" ;
    content = content.replace("[[URL]]", verifyURL);
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom(FromAddress, SenderName);
    helper.setTo(toAddress);
    helper.setSubject("Account Registration Confirmation");
    helper.setText(content, true);

    emailSender.send(message);
    System.out.println(message);
    return ResponseEntity.ok(new MessageResponse("successfully  "));
}
}
