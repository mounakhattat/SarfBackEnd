package trivaw.stage.sarf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import trivaw.stage.sarf.Entities.Evaluation;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation,Integer> {
    @Query("SELECT e FROM Evaluation e WHERE e.bureauDeChange.idUser = :bureauDeChangeId")
    List<Evaluation> findByBureauDeChangeId(Integer bureauDeChangeId);
    @Query("SELECT e FROM Evaluation e WHERE e.bureauDeChange.idUser = (SELECT b.user.idUser FROM BureauDeChange b WHERE b.idBureauDeChange = :bureauDeChangeId)")
    List<Evaluation> getEvaluationsByBureau(Integer bureauDeChangeId);


}
