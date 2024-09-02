package trivaw.stage.sarf.services;
import org.springframework.stereotype.Service;
import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.Entities.User;

import java.util.List;

@Service
public interface IAccountService {
    public List<Account> getAllAccount();
    Account getAccountById(Integer idAcc);
    Account createAccount(Account a);
    Account updateAccount(Integer idAcc, Account a);
    void deleteAccount(Integer  idAcc);

}
