package trivaw.stage.sarf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import trivaw.stage.sarf.Configuration.WebSocketEndpoint;
import trivaw.stage.sarf.Entities.*;
import trivaw.stage.sarf.repository.BureauDeChangeRepository;
import trivaw.stage.sarf.repository.ReservationRepository;
import trivaw.stage.sarf.repository.StockRepository;
import trivaw.stage.sarf.repository.UserRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ReservationService implements  IReservationService{
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    StockRepository stockRepository;
    @Autowired
    BureauDeChangeRepository bureauDeChangeRepository;
    private final String FromAddress = "mouna.khattat@esprit.tn";
    private final String SenderName = "TRIVAW Team";

    @Override
    public List<Reservation> getAllReservation() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Integer idReservation) {
        return reservationRepository.findById(idReservation).orElse(null);
    }
   //valider Reservation par bureau de change
    @Override
    public Reservation updateReservation(Integer idReservation, Reservation a ) throws MessagingException, IOException {
        Reservation existingReservation = reservationRepository.findById(idReservation).orElse(null);

        if (existingReservation != null) {
            existingReservation.setStatus(a.getStatus());

            reservationRepository.save(existingReservation);
            System.out.println(existingReservation.getUser().getIdUser()+"kkrkrkkrk");
            Integer idUser = existingReservation.getUser().getIdUser();
            System.out.println(idUser+"pffffffffff");
            sendMessageToUser(a, idUser);
            String subject;
        String toAddress = existingReservation.getUser().getEmail();
        System.out.println(toAddress+"aaaaaaaaaaaaaaaaaaaaaaaaaa");
            String content ;
            if ("Confirmer".equalsIgnoreCase(a.getStatus())) {
                subject = "Validation de la Réservation";
                content = "<p>Bonjour,</p>"
                        + "<p>Par le présent mail, nous avons l'honneur de vous annoncer la validation de votre demande de réservation.</p>";
                // Soustraction du montant de devises depuis la table stock
                BigDecimal montant = BigDecimal.valueOf(existingReservation.getMontant()); // Convertir en BigDecimal
                String devise = existingReservation.getDevise(); // Supposons que cette méthode existe

                Stock stock = stockRepository.findByDevise(devise);
                if (stock != null) {
                    BigDecimal montantStock = BigDecimal.valueOf(stock.getQuantite()); // Convertir en BigDecimal
                    if ("achat".equalsIgnoreCase(existingReservation.getType())) {
                        // Soustraction pour le type achat
                        BigDecimal nouveauMontant = montantStock.subtract(montant);
                        stock.setQuantite(nouveauMontant.doubleValue()); // Convertir de nouveau en double si nécessaire
                    } else if ("vente".equalsIgnoreCase(existingReservation.getType())) {
                        // Addition pour le type vente
                        BigDecimal nouveauMontant = montantStock.add(montant);
                        stock.setQuantite(nouveauMontant.doubleValue()); // Convertir de nouveau en double si nécessaire
                    }

                    stockRepository.save(stock);
                } else {
                    // Gérer le cas où la devise n'est pas trouvée dans le stock
                    throw new IllegalArgumentException("Devise non trouvée dans le stock");
                }
            } else {
                subject = "Mise à jour de la Réservation";
                content = "<p>Bonjour,</p>"
                        + "<p>Votre demande de réservation a été mise à jour. Le nouveau statut est : " + a.getStatus() + ".</p>";
            }
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            System.out.println(message);
            helper.setFrom(FromAddress, SenderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            helper.setText(content, true);

            emailSender.send(message);





            return    reservationRepository.save(existingReservation);

        } else {
            return null;
        }

    }

    @Override
    public Reservation saveReservation(Reservation reservation) {

        reservationRepository.save(reservation);
        return reservation;
    }

    public void sendMessageToUser(Reservation reservation , Integer idUser) throws IOException {
        Integer userId = reservation.getUser().getIdUser();
        String message = convertNotifToString(reservation,idUser); // Convert Reservation to String
        ;
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
           System.out.println("tqtqtqttqtqtaaaaaaaaaaaaaa");
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








    private String convertReservationToString(Reservation reservation , Integer idUser) {
        User user = userService.getUserById(idUser);
        String userId = user.getUsername();
        String reservationString = "Vous avez une nouvelle  reservation d'après " + userId  ;


        return reservationString;
    }
    private String convertNotifToString(Reservation reservation , Integer idUser) {
        User user = userService.getUserById(idUser);
        BureauDeChange bureauDeChange = bureauDeChangeRepository.findById(reservation.getBureauDeChangeId()).get();

        String userId = user.getUsername();
        String reservationString = "Reponse a votre demande de reservation pour " + reservation.getMontant() +" " + reservation.getDevise() +" chez " + bureauDeChange.getNom()+ " C'est: " + reservation.getStatus()  ;


        return reservationString;
    }
}
