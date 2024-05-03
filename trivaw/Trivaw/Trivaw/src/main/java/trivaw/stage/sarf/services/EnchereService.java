package trivaw.stage.sarf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.Entities.ERole;
import trivaw.stage.sarf.Entities.Enchere;
import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.repository.EnchereRepository;
import trivaw.stage.sarf.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EnchereService  implements  IEnchereService {
    @Autowired
    EnchereRepository enchereRepository;
    @Autowired
    UserRepository userRepository;
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




@Override
    public Enchere participerEnchere(Integer idEnchere, Double tauxPropose, Integer idUser) {
        // Rechercher l'enchère par son ID
        Optional<Enchere> optionalEnchere = enchereRepository.findById(idEnchere);
        System.out.println(optionalEnchere);
        if (optionalEnchere.isPresent()) {
            Enchere enchere = optionalEnchere.get();
            // Vérifier si l'enchère est en cours et non cloturée
            if (enchere.getStatus().equals("en cours") ) {
                // Rechercher l'utilisateur (bureau de change) par son nom d'utilisateur
                Optional<User> optionalUser = userRepository.findById(idUser);
                if (optionalUser.isPresent()) {
                    User bureauDeChange = optionalUser.get();
                    // Vérifier si l'utilisateur est un bureau de change
                    if (bureauDeChange.getRoles() == ERole.ROLE_BUREAU_DE_CHANGE) {
                        // Mettre à jour le taux proposé par le bureau de change
                        // Vérifier si le taux proposé est le plus bas
                        if (enchere.getTauxPropose() == null || tauxPropose < enchere.getTauxPropose()) {
                            // Mettre à jour le taux proposé par le bureau de change
                            enchere.setTauxPropose(tauxPropose);
                            System.out.println(bureauDeChange.getUsername());
                        enchere.setWinner(bureauDeChange.getUsername());
                            System.out.println(bureauDeChange.getUsername());
                            // Sauvegarder les modifications de l'enchère
                            return enchereRepository.save(enchere);
                        } else {
                            // Le taux proposé n'est pas le plus bas, ne pas mettre à jour
                            throw new RuntimeException("Le taux proposé doit être le plus interessant.");
                        }
                    } else {
                        // L'utilisateur n'est pas autorisé à participer car il n'est pas un bureau de change
                        throw new RuntimeException("Vous n'êtes pas autorisé à participer à cette enchère.");
                    }
                } else {
                    // L'utilisateur n'existe pas
                    throw new RuntimeException("Utilisateur non trouvé.");
                }
            } else {
                // L'enchère n'est pas en cours ou est déjà cloturée
                throw new RuntimeException("L'enchère n'est pas en cours ou est déjà cloturée.");
            }
        } else {
            // L'enchère n'existe pas
            throw new RuntimeException("Enchère non trouvée.");
        }}



}