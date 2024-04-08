package trivaw.stage.sarf.repository;
import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<User> findByBanned(Boolean isBanned);
    Account findByIdAcc(Integer idAcc);
    @Query("SELECT a FROM Account a WHERE a.banned=:true " )
    List<Account> retrieveBannedAccount();

   // List<Account> findBydateCreationGreaterThanOrdateCreationEquals(Date dateCreation1, Date dateCreation);
  //  List<Account> findByAgeBetween(Integer AgeFrom, Integer AgeTo) ;


}
