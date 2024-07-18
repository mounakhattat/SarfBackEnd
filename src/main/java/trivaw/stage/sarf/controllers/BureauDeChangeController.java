package trivaw.stage.sarf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.Entities.BureauDeChange;
import trivaw.stage.sarf.Entities.Reservation;
import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.services.IBureauDeChangeServices;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/BureauDeChange")
public class BureauDeChangeController {

    @Autowired
    IBureauDeChangeServices bureauDeChangeServices;


    @GetMapping("/retrieve-BureauDeChange")
    public List<BureauDeChange> getAllBureauDeChange() {
        return bureauDeChangeServices.getAllBureauDeChange();
    }

    @GetMapping("/getBureauDeChangeById/{idBureauDeChange}")
    public BureauDeChange getBureauDeChangeById(@PathVariable("idBureauDeChange") Integer idBureauDeChange) {
        return bureauDeChangeServices.getBureauDeChangeById(idBureauDeChange);
    }
    @PostMapping("/createBureauDeChange")
    public BureauDeChange createBureauDeChange(@RequestBody BureauDeChange a) {
        return bureauDeChangeServices.createBureauDeChange(a);
    }

    @PutMapping("/update/{idBureauDeChange}")
    public BureauDeChange updateBureauDeChange(@PathVariable("idBureauDeChange") Integer idBureauDeChange, @RequestBody BureauDeChange a) {
        return bureauDeChangeServices.updateBureauDeChange(idBureauDeChange, a);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBureauDeChange(@PathVariable("id") Integer idBureauDeChange) {
        bureauDeChangeServices.deleteBureauDeChange(idBureauDeChange);
    }
    @GetMapping("/getBureauDeChangeByUser/{idUser}")
    public BureauDeChange getBureauDeChangeByUser(@PathVariable("idUser") Integer idUser){
        return bureauDeChangeServices.getBureauDeChangeByUser(idUser);
    }
    @GetMapping("/getLocationByIdBureau/{idBureauDeChange}")
    public String getLocationByIdBureau(@PathVariable("idBureauDeChange") Integer idBureauDeChange){
        return bureauDeChangeServices.getLocationByIdBureau(idBureauDeChange);
    }

    @GetMapping("/getIdBureauWithNames/{nom}")
    public Integer getIdBureauWithNames(@PathVariable("nom") String nom){
        return bureauDeChangeServices.getIdBureauWithNames(nom);
    }
    @GetMapping("/getAllLocation")
    public List<String> getAllLocation(){
        return bureauDeChangeServices.getAllLocation();
    }
    @GetMapping("/getAllNames")
    public List<String> getAllNames(){
        return bureauDeChangeServices.getAllNames();
    }
    @PutMapping("/updatebyUser/{idUser}")
    public void updateBureauDeChangeByUser(@PathVariable("idUser") Integer idUser, @RequestBody BureauDeChange a) {
         bureauDeChangeServices.updateBureauDeChangeByUser(idUser, a);
    }
    @GetMapping("/filterBureauxDeChangeByLocalisation/{localisation}")
    public List<BureauDeChange> filterBureauxDeChangeByLocalisation(@PathVariable String localisation) {
        return bureauDeChangeServices.filterBureauxDeChangeByLocalisation(localisation);
    }








    @GetMapping("/{idUser}/reservations")
    public ResponseEntity<List<Reservation>> getReservationsByBureauDeChange(@PathVariable("idUser") Integer idUser) {
        List<Reservation> reservations = bureauDeChangeServices.getReservationsByUser( idUser);
        return ResponseEntity.ok(reservations);
    }
}
