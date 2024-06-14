package trivaw.stage.sarf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trivaw.stage.sarf.Entities.BureauDeChange;
import trivaw.stage.sarf.Entities.TauxDeChange;
import trivaw.stage.sarf.repository.BureauDeChangeRepository;
import trivaw.stage.sarf.repository.TauxChangeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TauxChangeService implements ITauxChangeService{
    @Autowired
    TauxChangeRepository tauxChangeRepository;
    @Autowired
    BureauDeChangeRepository bureauDeChangeRepository;
    @Override
    public List<TauxDeChange> getAllTauxDeChange() {
        return tauxChangeRepository.findAll();
    }
    @Override
    public TauxDeChange getTauxDeChangeById(Integer idTauxDeChange) {
        return tauxChangeRepository.findById(idTauxDeChange).orElse(null);
    }

    @Override
    public TauxDeChange createTauxDeChange(TauxDeChange a) {
        return tauxChangeRepository.save(a);
    }

    @Override
    public TauxDeChange updateTauxDeChange(Integer idTauxDeChange, TauxDeChange a) {
        TauxDeChange existingTauxDeChange = tauxChangeRepository.findById(idTauxDeChange).orElse(null);
        if (existingTauxDeChange != null) {
            existingTauxDeChange.setTauxAchat(a.getTauxAchat());
            existingTauxDeChange.setTauxVente(a.getTauxVente());
            existingTauxDeChange.setDeviseCible(a.getDeviseCible());
            existingTauxDeChange.setDeviseSource(a.getDeviseSource());
            existingTauxDeChange.setDate(a.getDate());


            return    tauxChangeRepository.save(existingTauxDeChange);

        } else {
            return null;
        }
    }

    @Override
    public void deleteTauxDeChange(Integer idTauxDeChange) {
        tauxChangeRepository.deleteById(idTauxDeChange);
    }
    @Override
    public  List<TauxDeChange> getTauxDeChangeByUser(Integer idUser) {
        return    tauxChangeRepository.getTauxDeChangeByUser(idUser);
    }
    @Override
    public List<TauxDeChange> updateTauxDeChangeByUser(Integer idUser, TauxDeChange a, Integer idTauxDeChange) {
        List<TauxDeChange> tauxDeChangee = tauxChangeRepository.getTauxDeChangeByUser(idUser);
        if (tauxDeChangee != null && !tauxDeChangee.isEmpty()) {
            for (TauxDeChange tauxDeChange : tauxDeChangee) {
                if (tauxDeChange.getIdTauxDeChange().equals(idTauxDeChange)) {
                    tauxDeChange.setTauxAchat(a.getTauxAchat());
                    tauxDeChange.setTauxVente(a.getTauxVente());
                    tauxDeChange.setDeviseCible(a.getDeviseCible());
                    tauxDeChange.setDeviseSource(a.getDeviseSource());
                    // Formater la date en utilisant le motif "yyyy-MM-dd"
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = currentDate.format(formatter);
                    tauxDeChange.setDate(LocalDate.parse(formattedDate, formatter));
                }
            }
                   return tauxChangeRepository.saveAll(tauxDeChangee);
        } else {
            return null; // Ou une autre logique de gestion d'erreur
        }
    }
    @Override
    public TauxDeChange createTauxDeChangeByUser(Integer idUser,TauxDeChange a) {
        BureauDeChange bureauDeChange=bureauDeChangeRepository.getBureauDeChangeByUser(idUser);
        TauxDeChange tauxDeChange=new TauxDeChange();
        tauxDeChange.setTauxAchat(a.getTauxAchat());
        tauxDeChange.setTauxVente(a.getTauxVente());
        tauxDeChange.setDeviseSource(a.getDeviseSource());
        tauxDeChange.setDeviseCible(a.getDeviseCible());
        tauxDeChange.setBureauDeChange(bureauDeChange);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        tauxDeChange.setDate(LocalDate.parse(formattedDate, formatter));
        tauxChangeRepository.save(tauxDeChange);

        return tauxChangeRepository.save(tauxDeChange);
    }
    @Override
    public List<TauxDeChange> getTauxDeChangeByBureau(Integer idBureauDeChange) {
        // Implémentez ici la logique pour récupérer les taux de change associés à un bureau de change spécifique
        // Par exemple, vous pouvez appeler votre repository pour récupérer les taux de change en fonction de l'ID du bureau
        return tauxChangeRepository.findByIdBureauDeChange(idBureauDeChange);
    }
    public List<Object[]> findTauxDeChangeAndBureauNomSortedByTauxAchatAscForDevise(String deviseSource) {
        return tauxChangeRepository.findTauxDeChangeAndBureauNomSortedByTauxAchatAscForDevise(deviseSource);
    }

    public List<Object[]> getTauxDeChangeSortedByTauxVenteDESCForDevise(String deviseSource) {
        return tauxChangeRepository.getTauxDeChangeSortedByTauxVenteDESCForDevise(deviseSource);
    }




}
