package trivaw.stage.sarf.services;

import trivaw.stage.sarf.Entities.Evaluation;

import java.util.List;
import java.util.Map;

public interface IEvaluationService {
    Map<String, List<Evaluation>> getAllEvaluations();
    List<Evaluation> getEvaluationsByUserId(Integer idUser);
    List<Evaluation> getEvaluationsByBureau(Integer idUser);

    Evaluation saveEvaluation(Evaluation evaluation, Integer idUser, Integer idBureau) ;
    void deleteEvaluation(Integer idEvaluation);

}
