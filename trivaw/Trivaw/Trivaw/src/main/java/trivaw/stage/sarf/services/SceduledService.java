package trivaw.stage.sarf.services;

import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;



public class SceduledService {
    @Autowired
    AccountService accountService;
    AccountRepository accountRepository;

    @Scheduled(cron = "${cron.Token}")
    public void DisableUserBan() {
        for(Account agent:accountRepository.retrieveBannedAccount()) {
            if(agent.getBannedPeriode().after(new Date()) || agent.getBannedPeriode().compareTo(new Date())==0) {
                agent.setBanned(false);
                Integer idAcc= agent.getIdAcc();
                accountService.updateAccount(idAcc ,agent);
                System.out.println(agent.getBanned());
            }

        }}
}