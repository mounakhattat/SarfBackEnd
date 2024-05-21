package trivaw.stage.sarf.services;

import trivaw.stage.sarf.Entities.Enchere;
import trivaw.stage.sarf.Entities.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface IEnchereService {
    List<Enchere> getAllEnchere();
  //  Enchere creerEnchere(Enchere enchere);
  void deleteEnchere(Integer idEnchere);
    Enchere creerEnchere(Enchere enchere, Integer idUser);
    Enchere participerEnchere(Integer idEnchere, Double tauxPropose,  Integer idUser) throws MessagingException, IOException;
    List<Enchere> findEnchereByIdUser(Integer idUser);
    Enchere getEnchereById(Integer idEnchere);
}
