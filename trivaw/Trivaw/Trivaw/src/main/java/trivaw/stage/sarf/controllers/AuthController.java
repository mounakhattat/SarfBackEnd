package trivaw.stage.sarf.controllers;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import trivaw.stage.sarf.Entities.*;
import trivaw.stage.sarf.Exception.UserNotFoundException;
import trivaw.stage.sarf.Jwt.JwtUtils;
import trivaw.stage.sarf.Request.SignUp;
import trivaw.stage.sarf.repository.BureauDeChangeRepository;
import trivaw.stage.sarf.repository.UserRepository;
import trivaw.stage.sarf.Request.LogIn;
import trivaw.stage.sarf.Responses.JwtResponse;
import trivaw.stage.sarf.Responses.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import trivaw.stage.sarf.services.*;
@CrossOrigin("*")
@RestController

@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IUserService userService;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    IResetPasswordService resetPasswordService;
    @Autowired
    ResetPasswordTokenService resetPasswordTokenService;
    @Autowired
    CodeConfirmationService codeConfirmationService ;
    @Autowired
    private HttpSession session;
    @Autowired
    TwilioService twilioService;
   // @Autowired
   // EmailVerificationTokenService emailVerificationTokenService;
    @Autowired
    JwtUtils jwtUtils;
    // generer et valider le info de token
    @Autowired
    BureauDeChangeRepository bureauDeChangeRepository;
    @Autowired
    BureauDeChangeServices bureauDeChangeServices;
    @Autowired
    private JavaMailSender emailSender;
    private final String FromAddress = "mouna.kh2018@gmail.com";
    private final String SenderName = "TRIVAW Team";

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LogIn loginRequest) {
        Optional<User> u = userRepository.findByUsername(loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!u.get().isActived()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Votre compte n'est pas activé."));
        }
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getIdUser(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
    }
    @Value("${app.twillio.toPhoneNo}")
    private String To;
    @Value("${app.twillio.fromPhoneNo}")
    private String From;
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUp signUpRequest) {
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
        if (userRoleEnum == null) {
            userRoleEnum = ERole.ROLE_VISITOR; // Assign a default role if none provided
        }

        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setUsername(signUpRequest.getUsername());
        user.setRoles(userRoleEnum); // Utilisation de setRoles pour définir le rôle de l'utilisateur
        user.setNumPhone(signUpRequest.getNumPhone());
      String code = codeConfirmationService.generateCode();
   twilioService.sendSms(To, From, code);
        user.setCode(code);
        userRepository.save(user);
        user.setActived(false);

        //  session.setAttribute("expectedCode", code);
        //  session.setAttribute("user", user);
        return ResponseEntity.ok(new MessageResponse("Merci de verifier votre code"));
    }

    @PostMapping("/signupAndAssign")
    public ResponseEntity<?> registerUserAndAssignBureau(@RequestBody SignUp signUpRequest) throws MessagingException, UnsupportedEncodingException {
        try {
            return bureauDeChangeServices.registerUserAndAssignBureau(signUpRequest);
        } catch (MessagingException ex) {
            System.out.print(ex.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }


    @GetMapping("/confirm-code/{code}")
    public ResponseEntity<?> confirmCode(@PathVariable("code") String code) {
        User user =userRepository.findByCode(code);
       String u= user.getCode();
        System.out.println(u+"lllllll");
        System.out.println(user);

        if (codeConfirmationService.verifyCode(code, u)){
            System.out.println(u);
            user.setActived(true);
            userRepository.save(user);
            return ResponseEntity.ok().body(new MessageResponse("Votre inscription a été confirmée avec succès !"));
        } else {

            return ResponseEntity.ok().body(new MessageResponse("Erreur, veuillez vérifier votre code svp"));
        }
    }

  // session  @GetMapping("/confirm-code/{code}")
   /* public String confirmCode(@PathVariable("code") String code) {
        String expectedCode = (String) session.getAttribute("expectedCode");
        System.out.println(expectedCode);
        System.out.println(code);
        if (codeConfirmationService.verifyCode(code, expectedCode) ){
            User user = (User) session.getAttribute("user");
            user.setActived(true);
            userRepository.save(user);

            return "Votre inscription a été confirmée avec succès !";
        } else {
            return "Erreur,Merci de verifier votre code svp";
        }
    }

    */
  @PutMapping("/reset-password/{email}/{newPassword}")
    public ResponseEntity<String> resetPassword(@PathVariable("email") String email, @PathVariable String newPassword) {   // objet body  de type user
        try {
            resetPasswordService.resetPassword(email, newPassword);
            return ResponseEntity.ok("Password reset successfully");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/resetPasswordRequest/{email}")

    public PasswordResetToken generatePassToken(@PathVariable("email") String email) {
        try {
            return resetPasswordTokenService.CreatePasswordToken(email);
        } catch (UnsupportedEncodingException | MessagingException e) {
            System.out.print(e.getMessage());
        } catch (UserNotFoundException ex) {
            System.out.print(ex.getMessage());
        }
        return null;
    }


    @PutMapping("/resetPassword/{token}/{pass}")
    public void resetPasswod(@PathVariable("token") String token, @PathVariable("pass") String pass) {
        resetPasswordTokenService.ConfirmPasswordReset(token, pass);
    }

    @GetMapping("/updatePassword/{newPassword}/{email}")
    public void updatePassword(@PathVariable("newPassword") String newPassword, @PathVariable("email") String email) {


        boolean passwordUpdated = resetPasswordTokenService.updatePassword(newPassword,email);

        if (passwordUpdated) {
            ResponseEntity.ok("Password updated successfully");
        } else {
             ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
    /*@GetMapping("/ConsultDeviceActivity/{idUser}/{username}")
    @ResponseBody
   public List<String> getDeviceActivity(@PathVariable("idUser") Integer idUser, @PathVariable("username") String username) {
       return activityService.getUserDeviceActivity(idUser, username);
   } */
}


