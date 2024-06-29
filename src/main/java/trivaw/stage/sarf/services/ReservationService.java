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
    private  StockService stockService;
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

    @Override
    public List<Reservation> getReservationByUser(Integer idUser) {
        return reservationRepository.getReservationByUser(idUser);
    }



   //valider Reservation par bureau de change
   @Override
   public Reservation updateReservation(Integer idReservation, Reservation a) throws MessagingException, IOException {

       Reservation existingReservation = reservationRepository.findById(idReservation).orElse(null);
       System.out.println(existingReservation +"hjhuhkl");
       boolean deviseTrouvee = false;

       if (existingReservation != null) {
           String devise = existingReservation.getDevise();
           BigDecimal montant = BigDecimal.valueOf(existingReservation.getMontant());
           System.out.println(devise +"ssssssssssss");
           System.out.println(montant +"dezdedzdez");

        BigDecimal quantite =BigDecimal.valueOf(0.0);
            BureauDeChange bureauDeChange = bureauDeChangeRepository.findById(existingReservation.getBureauDeChangeId()).get();

            User user= userRepository.findById(bureauDeChange.getUser().getIdUser()).get();
           System.out.println( user.getIdUser() +"oooooooooooooooooooooooo");

           List<Stock> devises=   stockService.getDistinctDevisesByUserId(user.getIdUser());
           Stock stock = new Stock();

           for (Stock devisee : devises) {
               if (devisee.getDevise().equals(devise)) {
              stock=devisee;
                   // Si la devise existe, récupérer le stock pour cette devise
                   deviseTrouvee = true;

                    quantite =  BigDecimal.valueOf(devisee.getQuantite());
                   System.out.println(quantite +"hahhahhahhahha");


               }
           }


           if (deviseTrouvee = true) {
Boolean comparaison = montant.compareTo(quantite) <= 0;
if(!comparaison) {
    existingReservation.setStatus("Stock Epuisé");
    reservationRepository.save(existingReservation);

    // Envoi de l'email pour stock épuisé
    String subject = "Stock Épuisé";
    String toAddress = existingReservation.getUser().getEmail();
    String content = "<p>Bonjour,</p>"
            + "<p>Nous regrettons de vous informer que le stock pour la devise " + devise + " est actuellement épuisé.</p>"
            + "<p>Merci pour votre compréhension.</p>";
    sendEmail(toAddress, subject, content);

    throw new IllegalArgumentException("Le montant de la réservation est supérieur au stock disponible pour cette devise.");

}
               // Vérifier si le montant de la réservation est inférieur ou égal au stock
              else  {
                   System.out.println(comparaison +"jijijiji");

                   // Mettre à jour le statut de la réservation seulement s'il est vide
                   if (existingReservation.getStatus() == null  || existingReservation.getStatus().isEmpty() || "Stock Epuisé".equalsIgnoreCase(existingReservation.getStatus())) {
                       existingReservation.setStatus(a.getStatus());

                       // Mettre à jour le stock
                       if ("Confirmer".equalsIgnoreCase(a.getStatus())) {
                           if ("Achat".equalsIgnoreCase(existingReservation.getType())) {
                               // Soustraction pour le type achat
                               BigDecimal nouveauMontant = quantite.subtract(montant);
                               stock.setQuantite(nouveauMontant.doubleValue());
                           } else if ("Vente".equalsIgnoreCase(existingReservation.getType())) {
                               // Addition pour le type vente
                               BigDecimal nouveauMontant = quantite.add(montant);
                               stock.setQuantite(nouveauMontant.doubleValue());
                           }
                           stockRepository.save(stock);
                       }
                       else   {
                           reservationRepository.save(existingReservation);

                       }

                       reservationRepository.save(existingReservation);
                       sendMessageToUser(a, existingReservation.getUser().getIdUser());

                       // Envoi du mail de confirmation/refus
                       String subject;
                       String toAddress = existingReservation.getUser().getEmail();
                       String content;
                       if ("Confirmer".equalsIgnoreCase(a.getStatus())) {
                           subject = "Validation de la Réservation";
                           content = "<p>Bonjour,</p>"
                                   + "<p>Par le présent mail, nous avons l'honneur de vous annoncer la validation de votre demande de réservation.</p>";
                       } else   {
                           subject = "Mise à jour de la Réservation";
                           content = "<p>Bonjour,</p>"
                                   + "<p>Votre demande de réservation a été mise à jour. Le nouveau statut est : " + a.getStatus() + ".</p>";
                       }
                       sendEmail(toAddress, subject, content);
                   } else {
                       // Le statut de la réservation n'est pas vide
                       throw new IllegalArgumentException("Le statut de la réservation n'est pas vide, il ne peut pas être mis à jour.");
                   }
               }
           } else {
               // La devise n'est pas trouvée dans le stock
               throw new IllegalArgumentException("Devise non trouvée dans le stock");
           }

           return reservationRepository.save(existingReservation);
       } else {
                        throw new IllegalArgumentException("ooooooooooooooooooojjjjjjjj");

       }
   }

    private void sendEmail(String toAddress, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(FromAddress, SenderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);
        emailSender.send(message);
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
