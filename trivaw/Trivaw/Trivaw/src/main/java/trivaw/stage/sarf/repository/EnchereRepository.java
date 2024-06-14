package trivaw.stage.sarf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import trivaw.stage.sarf.Entities.Enchere;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface EnchereRepository extends JpaRepository<Enchere,Integer> {
    @Query("SELECT e FROM Enchere e WHERE e.dateFin < :now")
    List<Enchere> findEncheresExpirees(LocalDateTime now);

    List<Enchere> findByUser_IdUser(Integer idUser);
    @Query("SELECT e.winner, e.devise, e.type, e.montant as montantMax, " +
            "(SELECT COUNT(e2.idEnchere) FROM Enchere e2 WHERE e2.winner = e.winner) as nbVictoires " +
            "FROM Enchere e " +
            "WHERE e.montant = (SELECT MAX(e2.montant) FROM Enchere e2 WHERE e2.winner = e.winner) " +
            "GROUP BY e.winner, e.devise, e.type, e.montant "
            )
    List<Object[]> findTopUsers();









}
