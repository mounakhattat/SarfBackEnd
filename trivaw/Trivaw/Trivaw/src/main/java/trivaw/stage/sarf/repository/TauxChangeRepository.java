package trivaw.stage.sarf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import trivaw.stage.sarf.Entities.BureauDeChange;
import trivaw.stage.sarf.Entities.TauxDeChange;

import java.util.List;

public interface TauxChangeRepository extends JpaRepository<TauxDeChange,Integer> {
    @Query("SELECT b FROM TauxDeChange b WHERE b.bureauDeChange.user.idUser = :idUser")
    List<TauxDeChange> getTauxDeChangeByUser(@Param("idUser") Integer idUser);
    @Query("SELECT b FROM TauxDeChange b WHERE b.bureauDeChange.idBureauDeChange = :idBureauDeChange")
    List<TauxDeChange> findByIdBureauDeChange(@Param("idBureauDeChange") Integer idBureauDeChange);
    @Query("SELECT t, b.nom FROM TauxDeChange t JOIN t.bureauDeChange b WHERE t.deviseSource = :deviseSource ORDER BY t.tauxAchat ASC")
    List<Object[]> findTauxDeChangeAndBureauNomSortedByTauxAchatAscForDevise(@Param("deviseSource") String deviseSource);
    @Query("SELECT t, b.nom FROM TauxDeChange t JOIN t.bureauDeChange b WHERE t.deviseSource = :deviseSource ORDER BY t.tauxVente ASC")
    List<Object[]> findTauxDeChangeAndBureauNomSortedByTauxVenteAscForDevise(@Param("deviseSource") String deviseSource);

}
