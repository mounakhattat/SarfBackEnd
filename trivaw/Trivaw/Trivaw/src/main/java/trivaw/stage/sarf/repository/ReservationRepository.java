package trivaw.stage.sarf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import trivaw.stage.sarf.Entities.Reservation;
import trivaw.stage.sarf.Entities.User;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    List<Reservation> findByBureauDeChangeId(Integer bureauDeChangeId);
  @Query("SELECT b FROM Reservation b WHERE b.user.idUser = :idUser")
  List<Reservation>  getReservationByUser(@Param("idUser") Integer idUser);


}
