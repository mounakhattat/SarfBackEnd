package trivaw.stage.sarf.services;


import trivaw.stage.sarf.Entities.Reclamation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IReclamationServices {
    public List<Reclamation> getReclamationForAdmin();

    List<Reclamation> findReclamationByBureauIdReceived(Integer idUser);
    Map<String, List<Reclamation>> getReclamationsByTouristeIdGroupedByResponsable(Integer idUser);
    List<Reclamation> findReclamationByBureauIdSended(Integer idUser);
    Reclamation getReclamationById(Integer idReclamation);
    Reclamation createReclamation(Reclamation a);
    Reclamation createReclamationTouristeToBureau(Reclamation reclamation, Integer idUser, Integer idBureau);
    Reclamation createReclamationTouristeToAdmin(Reclamation reclamation, Integer idUser);
    Reclamation createReclamationBureauToAdmin(Reclamation reclamation, Integer idBureau);
    Reclamation updateReclamation(Integer idReclamation, Reclamation a)throws IOException;
    void deleteReclamation(Integer  idReclamation);
}
