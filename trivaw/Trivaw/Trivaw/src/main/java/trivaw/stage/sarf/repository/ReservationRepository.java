package trivaw.stage.sarf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trivaw.stage.sarf.Entities.Reservation;
import trivaw.stage.sarf.Entities.User;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    List<Reservation> findByBureauDeChangeId(Integer bureauDeChangeId);

}
