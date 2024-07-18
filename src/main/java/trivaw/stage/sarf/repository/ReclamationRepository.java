package trivaw.stage.sarf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import trivaw.stage.sarf.Entities.Reclamation;

import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation,Integer> {
    List<Reclamation> findByResponsable(String responsable);

    @Query("SELECT r FROM Reclamation r WHERE r.touriste.idUser = :idUser")
    List<Reclamation> findReclamationByTouristeIdGroupedByResponsable(@Param("idUser") Integer idUser);


    @Query("SELECT r FROM Reclamation r WHERE r.bureauDeChange.idUser = :idUser")
    List<Reclamation> findReclamationByBureauId(@Param("idUser") Integer idUser);
}
