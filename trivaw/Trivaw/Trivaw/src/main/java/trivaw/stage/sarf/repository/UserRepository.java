package trivaw.stage.sarf.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import trivaw.stage.sarf.Entities.ERole;
import trivaw.stage.sarf.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
   User findByEmail(String email);


User findByCode(String code);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
   // User findByConfirmationCode(String code);
   @Transactional
   @Modifying
   @Query("UPDATE User u SET u.password = :password WHERE u.idUser = :idUser")
   void changeUserPassword(@Param("password") String password, @Param("idUser") Integer idUser);
    @Query("SELECT u FROM User u WHERE u.roles = 2")
    List<User> findVisitors();
}
