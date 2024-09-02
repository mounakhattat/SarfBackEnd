package trivaw.stage.sarf.services;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import trivaw.stage.sarf.Configuration.WebSocketEndpoint;
import trivaw.stage.sarf.Entities.*;
import trivaw.stage.sarf.repository.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EnchereService  implements  IEnchereService {
    private Set<Integer> notifiedEncheres = new HashSet<>();

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    EnchereRepository enchereRepository;
    @Autowired
    BureauDeChangeRepository  bureauDeChangeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StockRepository stockRepository;
  @Autowired
    NotificationRepository notificationRepository;
    private final String FromAddress = "mouna.khattat@esprit.tn";
    private final String SenderName = "TRIVAW Team";

    @Override
    public List<Enchere> getAllEnchere() {
        return enchereRepository.findAll();
    }
    @Override
    public List<Enchere> findEnchereByIdUser(Integer idUser) {
        return enchereRepository.findByUser_IdUser(idUser);
    }

    /*public Enchere creerEnchere(Enchere enchere) {
        enchere.setStatus("En Cours");
        return enchereRepository.save(enchere);
    }
    */

    @Override
    public Enchere getEnchereById(Integer idEnchere) {
        return enchereRepository.findById(idEnchere).orElse(null);
    }
    @Override
    public void deleteEnchere(Integer idEnchere) {
        enchereRepository.deleteById(idEnchere);
    }




    @Override
    public Enchere creerEnchere(Enchere enchere, Integer idUser) {
        User utilisateur = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'ID : " + idUser));

        enchere.setStatus("en cours");
        enchere.setType("Vente");

        enchere.setUser(utilisateur);

        return enchereRepository.save(enchere);
    }


    public List<Enchere> detecterEncheresExpirees() {
        LocalDateTime localDateTime = LocalDateTime.now(); // Convertit en Timestamp
        return enchereRepository.findEncheresExpirees(localDateTime);
    }


    public void cloturerEnchere(Enchere enchere) {
        enchere.setStatus("cloturé");
        enchereRepository.save(enchere);
    }

 @Scheduled(fixedRate = 60000) // Vérifie toutes les minutes
    public void cloturerEncheresExpirees() {
        List<Enchere> encheresExpirees = detecterEncheresExpirees();
        if (!encheresExpirees.isEmpty()) {
            System.out.println("Enchères expirées détectées : " + encheresExpirees.size());
            for (Enchere enchere : encheresExpirees) {
                System.out.println(enchere);
                cloturerEnchere(enchere);
                System.out.println("Enchère expirée clôturée : " + enchere.getIdEnchere());
            }


        } else {
            System.out.println("Aucune enchère expirée détectée.");
        }



    }

    public List<Object[]> getTopUsers() {
        return enchereRepository.findTopUsers();
    }


@Override
    public ResponseEntity<Enchere> participerEnchere(Integer idEnchere, Double tauxPropose, Integer idUser)  throws MessagingException,IOException{
        Optional<Enchere> optionalEnchere = enchereRepository.findById(idEnchere);
        System.out.println(optionalEnchere);
        if (optionalEnchere.isPresent()) {
            Enchere enchere = optionalEnchere.get();
                Optional<User> optionalUser = userRepository.findById(idUser);
                if (optionalUser.isPresent()) {
                    if (enchere.getStatus().equals("en cours") ) {

                        User bureauDeChange = optionalUser.get();
                    if (bureauDeChange.getRoles() == ERole.ROLE_BUREAU_DE_CHANGE) {
                        if (enchere.getTauxPropose() == null || tauxPropose > enchere.getTauxPropose()) {
                            enchere.setTauxPropose(tauxPropose);
                            System.out.println(bureauDeChange.getUsername());
                            enchere.setWinner(bureauDeChange.getUsername());
                            sendMessageToTouristeForEnchere(enchere);
                            System.out.println(bureauDeChange.getUsername());
                            BigDecimal montant = BigDecimal.valueOf(enchere.getMontant()); // Convertir en BigDecimal
                            String devise = enchere.getDevise(); // Supposons que cette méthode existe
                            Stock stock = stockRepository.findStockDeviseByUser(devise,idUser);
                            if (stock != null) {
                                BigDecimal montantStock = BigDecimal.valueOf(stock.getQuantite()); // Convertir en BigDecimal
                                if ("Vente".equalsIgnoreCase(enchere.getType())  ) {
                                    BigDecimal nouveauMontant = montantStock.add(montant);
                                    stock.setQuantite(nouveauMontant.doubleValue()); // Convertir de nouveau en double si nécessaire
                                }
                            }
                            enchereRepository.save(enchere);
                            return ResponseEntity.ok().body(enchere);
                        } else {
                            throw new RuntimeException("Le taux proposé doit être le plus interessant.");
                        }
                    } else {
                        throw new RuntimeException("Vous n'êtes pas autorisé à participer à cette enchère.");
                    }
                } else if(enchere.getStatus().equals("cloturé")) {
                        sendMessageTobureauForEnchere(enchere,idUser);
                        // Envoi de l'email pour stock épuisé
                        String subject = "Résultat d'enchere";
                        String toAddress = enchere.getUser().getEmail();
                        String content = "<p>Bonjour,</p>"
                                + "<p>Félécitations, vous etes le meilleur encherisseur pour  " + enchere.getMontant() +" " +enchere.getDevise() + " avec un taux proposé égale à " +enchere.getTauxPropose()   + " lancée par "+ enchere.getUser().getUsername() + ". Vous allez recevoir trés prochainement un mail de la part de ce touriste  </p>"
                                + "<p>Merci pour votre compréhension.</p>";
                        sendEmail(toAddress, subject, content);
                        return ResponseEntity.ok().body(enchere);
                }
                    else {
                        throw new RuntimeException("enchere non status.");

                    }
            } else {

                    throw new RuntimeException("Utilisateur non trouvé.");

                }
        } else {
            throw new RuntimeException("Enchère non trouvée.");
        }}

   /* @Scheduled(fixedRate = 1000) // Vérifie toutes les secondes
    public void checkAndNotifyClosedEncheres() throws MessagingException, IOException {
        LocalDateTime currentDateTime = LocalDateTime.now(); // Obtient la date et l'heure actuelles

        System.out.println("Vérification à : " + currentDateTime);

        List<Enchere> closedEncheres = enchereRepository.findOngoingEncheresOnCurrentDate(currentDateTime); // Trouve les enchères en cours
        System.out.println("Enchères trouvées 1111111111: " + closedEncheres);

        if (!closedEncheres.isEmpty()) {
            for (Enchere enchere : closedEncheres) {

                // Assurez-vous que chaque enchère est unique
                Integer idBureauDeChange = bureauDeChangeRepository.getIdBureauWithNames(enchere.getWinner());

                if (idBureauDeChange != null) {
                    System.out.println("ID Bureau de Change : " + idBureauDeChange);

                    if (enchere.getUser() != null) {
                        // Envoyer le message
                        sendMessageTobureauForEnchere(enchere, idBureauDeChange);

                        // Envoyer l'email
                        String subject = "Résultat d'enchère";
                        String toAddress = enchere.getUser().getEmail();
                        String content = "<p>Bonjour,</p>"
                                + "<p>Félicitations, vous êtes le meilleur enchérisseur pour " + enchere.getMontant() + " " + enchere.getDevise() + " avec un taux proposé égal à " + enchere.getTauxPropose() + ".</p>"
                                + "<p>Merci pour votre compréhension.</p>";
                        sendEmail(toAddress, subject, content);
                    } else {
                        System.out.println("Erreur: L'utilisateur est null pour l'enchère " + enchere.getIdEnchere());
                    }
                } else {
                    System.out.println("Erreur: idBureauDeChange est null pour l'enchère " + enchere.getIdEnchere());
                }
            }
        } else {
            System.out.println("Aucune enchère en cours à ce moment.");
        }
    }

    */

    private void sendEmail(String toAddress, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(FromAddress, SenderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);
        emailSender.send(message);
    }

    public void sendMessageTobureauForEnchere(Enchere enchere, Integer idBureau ) throws IOException {
        User user = userRepository.findById(idBureau).get();
        String messageBureauForEnchere = messageBureauForEnchere(enchere); // Convert Reservation to String

        Notification notification=new Notification();
        notification.setMessage(messageBureauForEnchere);
        notification.setUserr(user);
        LocalDateTime localDateTime = LocalDateTime.now(); // Convertit en Timestamp
        notification.setDate(localDateTime);
        notification.setType("Enchere");
        notificationRepository.save(notification);
        if (idBureau != null) {
            CopyOnWriteArrayList<WebSocketSession> sessions = WebSocketEndpoint.getSessions();
            // Envoyer le message WebSocket à toutes les sessions
            for (WebSocketSession session : sessions) {
                System.out.println(session + "oooooooooooooooooooooooooojjjjjj");

                URI uri = session.getUri();
                Integer userIdFromUri = extractUserIdFromUri(uri);

                if (session != null && session.isOpen() && userIdFromUri != null && userIdFromUri.equals(user.getIdUser())) {
                    session.sendMessage(new TextMessage(messageBureauForEnchere));
                }
            }
        } else {
            // Gérer le cas où le bureau de change n'est pas trouvé
        }
    }

    public void sendMessageToTouristeForEnchere(Enchere enchere ) throws IOException {
       User user= enchere.getUser();
        String messageTouristeForEnchere = messageTouristeForEnchere(enchere); // Convert Reservation to String
        Notification notification=new Notification();
        notification.setMessage(messageTouristeForEnchere);
        notification.setUserr(user);
        LocalDateTime localDateTime = LocalDateTime.now(); // Convertit en Timestamp
        notification.setDate(localDateTime);
        notification.setType("Enchere");
        notificationRepository.save(notification);
        if (user != null) {
            CopyOnWriteArrayList<WebSocketSession> sessions = WebSocketEndpoint.getSessions();
            // Envoyer le message WebSocket à toutes les sessions
            for (WebSocketSession session : sessions) {
                System.out.println(session + "oooooooooooooooooooooooooojjjjjj");

                URI uri = session.getUri();
                Integer userIdFromUri = extractUserIdFromUri(uri);

                if (session != null && session.isOpen() && userIdFromUri != null && userIdFromUri.equals(user.getIdUser())) {
                    session.sendMessage(new TextMessage(messageTouristeForEnchere));
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

    private String messageBureauForEnchere(Enchere enchere  ) {
        String devise = enchere.getDevise();
        String lanceur =enchere.getUser().getUsername();
        Double taux = enchere.getTauxPropose();
        Double montant = enchere.getMontant();

        String messageBureauForEnchere = "Vous etes le meilleur encherisseur pour la vente de " + montant + " "+ devise +" lancée par " + lanceur + " avec un taux proposée = " + taux + " %"  ;
        return messageBureauForEnchere;
    }

    private String messageTouristeForEnchere(Enchere enchere  ) {
        String devise = enchere.getDevise();
        String winner =enchere.getWinner();
        Double taux = enchere.getTauxPropose();
        Double montant = enchere.getMontant();

        String messageBureauForEnchere = "Proposition actuelle d'un taux de vente à " + taux + " %" +" pour votre enchere: " +montant + " "+devise +" ,proposée par " + winner ;
        System.out.println(messageBureauForEnchere+"fzsyyyyyyyyyyyyyyyy");
        return messageBureauForEnchere;
    }

}