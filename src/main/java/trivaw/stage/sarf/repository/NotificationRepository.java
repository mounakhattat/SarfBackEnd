package trivaw.stage.sarf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.Entities.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    @Query("SELECT a FROM Notification a WHERE a.userr.idUser=:idUser " )
    List<Notification> findNotificationByUser(@Param("idUser") Integer idUser);
}
