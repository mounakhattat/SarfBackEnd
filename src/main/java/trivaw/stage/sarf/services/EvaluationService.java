package trivaw.stage.sarf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trivaw.stage.sarf.Entities.BureauDeChange;
import trivaw.stage.sarf.Entities.Evaluation;
import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.repository.BureauDeChangeRepository;
import trivaw.stage.sarf.repository.EvaluationRepository;
import trivaw.stage.sarf.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EvaluationService implements IEvaluationService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BureauDeChangeRepository bureauDeChangeRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
        @Override
        public Map<String, List<Evaluation>> getAllEvaluations () {
            return evaluationRepository.findAll().stream()
                    .collect(Collectors.groupingBy(evaluation -> {
                        User bureauDeChange = evaluation.getBureauDeChange();
                        if (bureauDeChange != null) {
                          BureauDeChange bureauDeChangeOptional = bureauDeChangeRepository.getBureauDeChangeByUser(bureauDeChange.getIdUser());
                            if (bureauDeChangeOptional!=null) {
                                return bureauDeChangeOptional.getNom();
                            }
                        }
                        return "Bureau de change inconnu";
                    }));
        }



        //pour le bureau il cherche avec son idUser depuis local storge
    @Override
    public List<Evaluation> getEvaluationsByUserId(Integer idUser) {
        return evaluationRepository.findByBureauDeChangeId(idUser);
    }

    //pour le touriste il cherche avec idBureau depuis params
    @Override
    public List<Evaluation> getEvaluationsByBureau(Integer idUser) {
        return evaluationRepository.getEvaluationsByBureau(idUser);
    }
    @Override
    public Evaluation saveEvaluation(Evaluation evaluation, Integer idUser, Integer idBureau) {
            User utilisateur = userRepository.findById(idUser).get();
            evaluation.setTouriste(utilisateur);
            BureauDeChange bureau = bureauDeChangeRepository.findById(idBureau).get();
            evaluation.setBureauDeChange(bureau.getUser());
            LocalDateTime localDateTime = LocalDateTime.now(); // Convertit en Timestamp
            evaluation.setDate(localDateTime);
            evaluation.setEvaluateur(utilisateur.getUsername());
            evaluation.calculateNote();
        // Censure des mots interdits
        evaluation.setCommentaire(censurerCommentaire(evaluation.getCommentaire()));

        return evaluationRepository.save(evaluation);
    }
    private String censurerCommentaire(String commentaire) {
        List<String> badwords = Arrays.asList("mal", "mauvais", "connar"); // Remplacer par les mots interdits
        for (String badword : badwords) {
            commentaire = commentaire.replaceAll("(?i)" + badword, "****"); // "(?i)" pour ignorer la casse
        }
        return commentaire;
    }



    @Override
    public void deleteEvaluation(Integer idEvaluation) {
        evaluationRepository.deleteById(idEvaluation);
    }

@Override
    public List<Object[]> findTopBureausWithNotes() {
        return evaluationRepository.findTopBureausWithNotes();
    }
}
