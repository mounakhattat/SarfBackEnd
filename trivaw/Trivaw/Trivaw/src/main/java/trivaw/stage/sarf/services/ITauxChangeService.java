package trivaw.stage.sarf.services;

import trivaw.stage.sarf.Entities.TauxDeChange;

import java.util.List;

public interface ITauxChangeService {
    List<TauxDeChange> getAllTauxDeChange();

    TauxDeChange getTauxDeChangeById(Integer idTauxDeChange);

    TauxDeChange createTauxDeChange(TauxDeChange a);

    TauxDeChange updateTauxDeChange(Integer idTauxDeChange, TauxDeChange a);

    void deleteTauxDeChange(Integer idTauxDeChange);

    List<TauxDeChange> getTauxDeChangeByUser(Integer idUser);

    List<TauxDeChange> updateTauxDeChangeByUser(Integer idUser, TauxDeChange a, Integer idTauxDeChange);

    TauxDeChange createTauxDeChangeByUser(Integer idUser, TauxDeChange a);
    List<TauxDeChange> getTauxDeChangeByBureau(Integer idBureauDeChange);

}
