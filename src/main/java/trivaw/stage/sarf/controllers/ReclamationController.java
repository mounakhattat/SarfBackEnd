package trivaw.stage.sarf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trivaw.stage.sarf.Entities.Reclamation;
import trivaw.stage.sarf.services.BureauDeChangeServices;
import trivaw.stage.sarf.services.IReclamationServices;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/Reclamation")
public class ReclamationController {
    @Autowired
    BureauDeChangeServices bureauDeChangeServices;
    @Autowired
    private IReclamationServices iReclamationServices;


    @GetMapping("/getReclamationForAdmin")
    public List<Reclamation> getReclamationForAdmin() {
        return iReclamationServices.getReclamationForAdmin();
    }

    @GetMapping("/getReclamationsByTouristeId/{idUser}")
    public Map<String, List<Reclamation>> getReclamationsByTouristeId(@PathVariable Integer idUser) {
        return iReclamationServices.getReclamationsByTouristeIdGroupedByResponsable(idUser);
    }


    //Pour Le Bureau affiche les reclamations Récues:
    @GetMapping("/getReclamationsByBureauIdReceived/{idUser}")
    public List<Reclamation>  getReclamationsByBureauIdReceived(@PathVariable Integer idUser) {
        return iReclamationServices.findReclamationByBureauIdReceived(idUser);
    }


    //Pour Le Bureau affiche les reclamations envoyées:
    @GetMapping("/getReclamationsByBureauIdSended/{idUser}")
    public List<Reclamation>  getReclamationsByBureauIdSended(@PathVariable Integer idUser) {
        return iReclamationServices.findReclamationByBureauIdSended(idUser);
    }
    @GetMapping("/getReclamationById/{idReclamation}")
    public Reclamation getReclamationById(@PathVariable("idReclamation") Integer idReclamation) {
        return iReclamationServices.getReclamationById(idReclamation);
    }

    @PostMapping("/createReclamation")
    public Reclamation createReclamation(@RequestBody Reclamation a) {
        return iReclamationServices.createReclamation(a);
    }
    @PostMapping("/createReclamationTouristeToBureau/{idUser}/{idBureau}")
    public ResponseEntity<Reclamation> createReclamationTouristeToBureau(@PathVariable("idUser") Integer idUser , @PathVariable("idBureau") Integer idBureau, @RequestBody Reclamation reclamation) throws IOException {
        Reclamation avis=   iReclamationServices.createReclamationTouristeToBureau(reclamation, idUser ,idBureau);
        bureauDeChangeServices.sendMessageToExchangeForReclamation(reclamation, idBureau, idUser);
        return new ResponseEntity<>(avis, HttpStatus.CREATED);
    }

    @PostMapping("/createReclamationBureauToAdmin/{idBureau}")
    public ResponseEntity<Reclamation> createReclamationBureauToAdmin( @PathVariable("idBureau") Integer idBureau, @RequestBody Reclamation reclamation)throws IOException {
        Reclamation avis=   iReclamationServices.createReclamationBureauToAdmin(reclamation, idBureau);
        bureauDeChangeServices.sendMessageToAdminForReclamation(reclamation);

        return new ResponseEntity<>(avis, HttpStatus.CREATED);
    }

    @PostMapping("/createReclamationTouristeToAdmin/{idUser}")
    public ResponseEntity<Reclamation> createReclamationTouristeToAdmin(@PathVariable("idUser") Integer idUser ,  @RequestBody Reclamation reclamation) throws IOException {
        Reclamation avis=   iReclamationServices.createReclamationTouristeToAdmin(reclamation, idUser );
        bureauDeChangeServices.sendMessageToAdminForReclamation(reclamation);

        return new ResponseEntity<>(avis, HttpStatus.CREATED);

    }
    @PutMapping("/updateReclamation/{idReclamation}")
    public Reclamation updateReclamation(@PathVariable("idReclamation") Integer idReclamation, @RequestBody Reclamation a)throws IOException  {
        return iReclamationServices.updateReclamation(idReclamation, a);
    }

    @DeleteMapping("/deleteReclamation/{idReclamation}")
    public void deleteReclamation(@PathVariable("idReclamation") Integer idReclamation) {
        iReclamationServices.deleteReclamation(idReclamation);
    }


}
