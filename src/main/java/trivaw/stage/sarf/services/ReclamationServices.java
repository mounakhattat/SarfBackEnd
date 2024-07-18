package trivaw.stage.sarf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import trivaw.stage.sarf.Configuration.WebSocketEndpoint;
import trivaw.stage.sarf.Entities.*;
import trivaw.stage.sarf.repository.BureauDeChangeRepository;
import trivaw.stage.sarf.repository.ReclamationRepository;
import trivaw.stage.sarf.repository.UserRepository;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class ReclamationServices implements IReclamationServices {
    @Autowired
    private ReclamationRepository reclamationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BureauDeChangeRepository bureauDeChangeRepository;
    @Autowired
    private UserService userService;

    @Override
    public List<Reclamation> getReclamationForAdmin() {
        return reclamationRepository.findByResponsable("Système");
    }


    @Override
    public Map<String, List<Reclamation>> getReclamationsByTouristeIdGroupedByResponsable(Integer idUser) {
        List<Reclamation> reclamations = reclamationRepository.findReclamationByTouristeIdGroupedByResponsable(idUser);
        return reclamations.stream().collect(Collectors.groupingBy(Reclamation::getResponsable));
    }


    //Pour Le Bureau affiche les reclamations Récues:
    @Override
    public List<Reclamation> findReclamationByBureauIdReceived(Integer idUser) {
        List<Reclamation> allReclamations = reclamationRepository.findReclamationByBureauId(idUser);
        return allReclamations.stream()
                .filter(reclamation -> reclamation.getResponsable().equals("BureauDeChange"))
                .collect(Collectors.toList());
    }

    //Pour Le Bureau affiche les reclamations envoyées:
    @Override
    public List<Reclamation> findReclamationByBureauIdSended(Integer idUser) {
        List<Reclamation> allReclamations = reclamationRepository.findReclamationByBureauId(idUser);
        return allReclamations.stream()
                .filter(reclamation -> reclamation.getResponsable().equals("Système"))
                .collect(Collectors.toList());
    }



    @Override
    public Reclamation getReclamationById(Integer idAcc) {
        return reclamationRepository.findById(idAcc).orElse(null);
    }

    @Override
    public Reclamation createReclamation(Reclamation a) {
        return reclamationRepository.save(a);
    }
    @Override
    public Reclamation createReclamationTouristeToBureau(Reclamation reclamation, Integer idUser, Integer idBureau) {
        User utilisateur = userRepository.findById(idUser).get();
        reclamation.setTouriste(utilisateur);
        User bureau = userRepository.findById(idBureau).get();

        reclamation.setBureauDeChange(bureau);
        reclamation.setReclameur(utilisateur.getUsername());
        reclamation.setStatus(Status.EN_COURS);
        reclamation.setResponsable("BureauDeChange");
        BureauDeChange bureauDeChange = bureauDeChangeRepository.getBureauDeChangeByUser(idBureau);
        reclamation.setDestinateur(bureauDeChange.getNom());
        LocalDateTime localDateTime = LocalDateTime.now(); // Convertit en Timestamp
        reclamation.setDate(localDateTime);
        return reclamationRepository.save(reclamation);
    }
    @Override
    public Reclamation createReclamationTouristeToAdmin(Reclamation reclamation, Integer idUser) {
        User utilisateur = userRepository.findById(idUser).get();
        reclamation.setTouriste(utilisateur);
        reclamation.setReclameur(utilisateur.getUsername());
        reclamation.setStatus(Status.EN_COURS);
        reclamation.setResponsable("Système");
        LocalDateTime localDateTime = LocalDateTime.now(); // Convertit en Timestamp
        reclamation.setDate(localDateTime);
        return reclamationRepository.save(reclamation);
    }
    @Override
    public Reclamation createReclamationBureauToAdmin(Reclamation reclamation, Integer idBureau) {

        User utilisateur =userRepository.findById(idBureau).get();
        reclamation.setBureauDeChange(utilisateur);
        reclamation.setReclameur(utilisateur.getUsername());
        reclamation.setStatus(Status.EN_COURS);
        reclamation.setResponsable("Système");
        LocalDateTime localDateTime = LocalDateTime.now(); // Convertit en Timestamp
        reclamation.setDate(localDateTime);
        return reclamationRepository.save(reclamation);
    }
    @Override
    public void deleteReclamation(Integer idReclamation) {
        reclamationRepository.deleteById(idReclamation);
    }


    @Override
    public Reclamation updateReclamation(Integer idReclamation, Reclamation a) throws IOException  {
        Reclamation existingReclamation = reclamationRepository.findById(idReclamation).orElse(null);
        if (existingReclamation != null) {
            existingReclamation.setStatus(a.getStatus());
            Reclamation updatedReclamation = reclamationRepository.save(existingReclamation);
            sendMessageToTouristeForStatusReclamation(updatedReclamation);
            return updatedReclamation;

        } else {
            return null;
        }
    }

    public void sendMessageToTouristeForStatusReclamation(Reclamation reclamation) throws IOException {
        Integer userId = reclamation.getTouriste().getIdUser();
        if (userId != null) {
            String reclamationMessageForTouriste = convertNotifToString(reclamation);
            CopyOnWriteArrayList<WebSocketSession> sessions = WebSocketEndpoint.getSessions();

            for (WebSocketSession session : sessions) {
                if (session != null && session.isOpen()) {
                    URI uri = session.getUri();
                    Integer userIdFromUri = extractUserIdFromUri(uri);

                    if (userIdFromUri != null && userIdFromUri.equals(userId)) {
                        session.sendMessage(new TextMessage(reclamationMessageForTouriste));
                    }
                }
            }
        } else {
            System.out.println("Erreur: l'ID de l'utilisateur est null.");
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

    private String convertNotifToString(Reclamation reclamation ) {
        BureauDeChange bureauDeChange = bureauDeChangeRepository.getBureauDeChangeByUser(reclamation.getBureauDeChange().getIdUser());

        String reclamationMessageForTouriste = "Reponse a votre demande de reclamation pour " + reclamation.getTitre() +" " + " chez " + bureauDeChange.getNom()+ " C'est: " + reclamation.getStatus()  ;


        return reclamationMessageForTouriste;
    }
}



