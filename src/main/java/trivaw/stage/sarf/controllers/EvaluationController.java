package trivaw.stage.sarf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trivaw.stage.sarf.Entities.Enchere;
import trivaw.stage.sarf.Entities.Evaluation;
import trivaw.stage.sarf.services.EvaluationService;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/evaluation")
public class EvaluationController {
    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/getAllEvaluations")
    public Map<String, List<Evaluation>> getAllEvaluations() {
        return evaluationService.getAllEvaluations();
    }

    @GetMapping("/getEvaluationsByUserId/{idUser}")
    public List<Evaluation> getEvaluationsByUserId(@PathVariable("idUser") Integer idUser) {
        return evaluationService.getEvaluationsByUserId(idUser);
    }
    @GetMapping("/getEvaluationsByBureau/{idUser}")
    public List<Evaluation> getEvaluationsByBureau(@PathVariable("idUser") Integer idUser) {
        return evaluationService.getEvaluationsByBureau(idUser);
    }

    @PostMapping("/saveEvaluation/{idUser}/{idBureau}")
    public ResponseEntity<Evaluation>  saveEvaluation(@PathVariable("idUser") Integer idUser ,@PathVariable("idBureau") Integer idBureau, @RequestBody Evaluation evaluation) {
      Evaluation avis=   evaluationService.saveEvaluation(evaluation, idUser ,idBureau);
        return new ResponseEntity<>(avis, HttpStatus.CREATED);

    }

    @DeleteMapping("/delete/{idEvaluation}")
    public void deleteEvaluation(@PathVariable("idEvaluation") Integer idEvaluation) {
        evaluationService.deleteEvaluation(idEvaluation);
    }


}
