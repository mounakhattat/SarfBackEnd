package trivaw.stage.sarf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import trivaw.stage.sarf.Configuration.WebSocketEndpoint;
import trivaw.stage.sarf.Entities.*;
import trivaw.stage.sarf.Request.SignUp;
import trivaw.stage.sarf.Responses.MessageResponse;
import trivaw.stage.sarf.repository.BureauDeChangeRepository;
import trivaw.stage.sarf.repository.ReservationRepository;
import trivaw.stage.sarf.repository.TauxChangeRepository;
import trivaw.stage.sarf.repository.UserRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BureauDeChangeServices implements IBureauDeChangeServices {
    private WebSocketSession webSocketSession;

@Autowired
UserService userService;
WebSocketEndpoint webSocketEndpoint;
    @Autowired
    BureauDeChangeRepository bureauDeChangeRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    TauxChangeRepository tauxChangeRepository;
    @Autowired
    ReservationService reservationService;
    @Autowired
    private JavaMailSender emailSender;
    private final String FromAddress = "mouna.khattat@esprit.tn";
    private final String SenderName = "SARF Team";

    public BureauDeChangeServices() {
    }

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
                .body(new MessageResponse("Erreur: Nom Utilisateur déjà existe!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Erreur: Email déjà existe!"));
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
            + "<p>Bienvenue chez SARF.tn ! Votre Compte a été crée avec succés.</p>"
            + "<br>"
            + "<p>Voilà les coordonnées de votre compte:  "
            + "<p>Nom d'Utilisateur : " +signUpRequest.getUsername() + "</p>"
            + "<p>Mot De Passe : " +signUpRequest.getPassword() + "</p>"
            + "<br>"
            + "<p><a href=\"[[URL]]\" >Cliquer pour connecter: </a></p>"

            + "<p>Merci pour votre confiance,</p>";
    String verifyURL = "http://localhost:4200/page-login" ;
    content = content.replace("[[URL]]", verifyURL);
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom(FromAddress, SenderName);
    helper.setTo(toAddress);
    helper.setSubject("Confirmation d'inscription");
    helper.setText(content, true);

    emailSender.send(message);
    System.out.println(message);
    return ResponseEntity.ok(new MessageResponse("successfully  "));
}
    @Override
    public List<BureauDeChange> filterBureauxDeChangeByLocalisation(String localisation) {
        return bureauDeChangeRepository.findByLocalisation(localisation);
    }

    @Override
    public List<String> getAllLocation() {
        return bureauDeChangeRepository.getAllLocation();
    }
    @Override
    public List<String> getAllNames() {
        return bureauDeChangeRepository.getAllNames();
    }
    @Override
    public String getLocationByIdBureau(Integer idBureauDeChange) {
        return bureauDeChangeRepository.getLocationByIdBureau(idBureauDeChange);
    }



    @Override
    public  Integer getIdBureauWithNames(String nom) {
        return bureauDeChangeRepository.getIdBureauWithNames(nom);
    }
    public void sendMessageToExchange(Reservation reservation, Integer idBureauDeChange , Integer idUser) throws IOException {
        BureauDeChange bureauDeChange = getBureauDeChangeById(idBureauDeChange);
        Integer userId = bureauDeChange.getUser().getIdUser();
        String message = convertReservationToString(reservation,idUser); // Convert Reservation to String

        if (userId != null) {
            CopyOnWriteArrayList<WebSocketSession> sessions = WebSocketEndpoint.getSessions();
            // Envoyer le message WebSocket à toutes les sessions
            for (WebSocketSession session : sessions) {
                System.out.println(session + "CCCCCCCCCCCCCCCCC");

                URI uri = session.getUri();
                Integer userIdFromUri = extractUserIdFromUri(uri);

                if (session != null && session.isOpen() && userIdFromUri != null && userIdFromUri.equals(userId)) {
                    session.sendMessage(new TextMessage(message));
                }
            }
        } else {
            // Gérer le cas où le bureau de change n'est pas trouvé
        }
    }



    private Integer extractUserIdFromUri(URI uri) {
        String query = uri.getQuery();
        if (query != null) {
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                if (param.startsWith("userId=")) {
                    String userIdString = param.substring("userId=".length());
                    return Integer.parseInt(userIdString);
                }
            }
        }
        return null;
    }

    public void sendMessageToExchangeForReclamation(Reclamation reclamation, Integer idBureauDeChange , Integer idUser) throws IOException {
        User user = userRepository.findById(idBureauDeChange).get();


        BureauDeChange bureauDeChange = getBureauDeChangeByUser(user.getIdUser());

        Integer userId = bureauDeChange.getUser().getIdUser();
        String reclamationMessages = messageForReclamation(reclamation,idUser); // Convert Reservation to String

        if (userId != null) {
            CopyOnWriteArrayList<WebSocketSession> sessions = WebSocketEndpoint.getSessions();
            // Envoyer le message WebSocket à toutes les sessions
            for (WebSocketSession session : sessions) {
                System.out.println(session + "oooooooooooooooooooooooooojjjjjj");

                URI uri = session.getUri();
                Integer userIdFromUri = extractUserIdFromUri(uri);

                if (session != null && session.isOpen() && userIdFromUri != null && userIdFromUri.equals(userId)) {
                    session.sendMessage(new TextMessage(reclamationMessages));
                }
            }
        } else {
            // Gérer le cas où le bureau de change n'est pas trouvé
        }
    }

    private String convertReservationToString(Reservation reservation , Integer idUser) {
        User user = userService.getUserById(idUser);
        String userId = user.getUsername();

        String reservationString = "Vous avez une nouvelle reservation  de iji " +   reservation.getMontant() +" d après " + userId ;


        return reservationString;
    }

    private String messageForReclamation(Reclamation reclamation , Integer idUser) {
        User user = userService.getUserById(idUser);
        String userId = user.getUsername();
        String reclamationMessages = "Vous avez une nouvelle reclamation d après " + userId ;


        return reclamationMessages;
    }

    // Méthode pour récupérer les réservations associées à un bureau de change spécifique
    // Méthode pour récupérer les réservations associées à un bureau de change spécifique
    // Méthode pour récupérer les réservations associées à un bureau de change spécifique
  /*  public List<Reservation> getReservationsByBureauDeChange(Integer idBureauDeChange) {
        Optional<BureauDeChange> bureauDeChangeOptional = bureauDeChangeRepository.findById(idBureauDeChange);
        if (bureauDeChangeOptional.isPresent()) {
            BureauDeChange bureauDeChange = bureauDeChangeOptional.get();
            // Vérifiez si le bureau de change a le rôle "bureau de change"
            if (bureauDeChange.getUser().getRoles() == ERole.ROLE_BUREAU_DE_CHANGE) {
                return bureauDeChange.getUser().getReservations(); // Utilisez la relation pour récupérer les réservations
            } else {
                // Gérer le cas où le bureau de change n'a pas le rôle approprié
                return Collections.emptyList(); // ou lancer une exception
            }
        } else {
            // Gérer le cas où le bureau de change n'est pas trouvé
            return Collections.emptyList(); // ou lancer une exception
        }
    }

   */

    // Méthode pour récupérer les réservations associées à un bureau de change spécifique
    /*public List<Reservation> getReservationsByBureauDeChange(Integer idUser) {
            return reservationRepository.findByBureauDeChangeId(idUser);
        }

     */

    public List<Reservation> getReservationsByUser(Integer idUser) {
        BureauDeChange bureauDeChange= getBureauDeChangeByUser(idUser);
List<Reservation>  reservations= reservationRepository.findByBureauDeChangeId(bureauDeChange.getIdBureauDeChange());

        return reservations;
    }






}