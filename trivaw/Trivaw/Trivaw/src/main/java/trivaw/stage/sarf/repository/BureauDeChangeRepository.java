package trivaw.stage.sarf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trivaw.stage.sarf.Entities.BureauDeChange;

import java.util.List;

@Repository
public interface BureauDeChangeRepository extends JpaRepository<BureauDeChange,Integer> {

    @Query("SELECT b FROM BureauDeChange b WHERE b.user.idUser = :idUser")
    BureauDeChange getBureauDeChangeByUser(@Param("idUser") Integer idUser);
    List<BureauDeChange> findByLocalisation(String localisation);


}
