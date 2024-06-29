package trivaw.stage.sarf.services;
import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class AccountService implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Integer idAcc) {
        return accountRepository.findById(idAcc).orElse(null);
    }

    @Override
    public Account createAccount(Account a) {
        return accountRepository.save(a);
    }

    @Override
    public Account updateAccount(Integer idAcc, Account a) {
        Account existingAccount = accountRepository.findById(idAcc).orElse(null);
        if (existingAccount != null) {

            return accountRepository.save(existingAccount);

        } else {
            return null;
        }
    }

    @Override
    public void deleteAccount(Integer idAcc) {
        accountRepository.deleteById(idAcc);
    }


}

  /*  @Override
    public     List<Account> filterAccount(Date dateCreation, Integer amountTrans, Integer Ageuser)
    {
        if (dateCreation != null && amountTrans == null && Ageuser != null) {
            return accountRepository.findBydateCreationGreaterThanOrdateCreationEquals(dateCreation, dateCreation);
       } else if (dateCreation != null && amountTrans == null && Ageuser == null) {
           return accountRepository.findByAgeBetween(Ageuser, Ageuser);
        }
            return null;
        }
    }

   */



