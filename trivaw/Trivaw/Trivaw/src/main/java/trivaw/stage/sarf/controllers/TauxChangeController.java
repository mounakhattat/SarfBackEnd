package trivaw.stage.sarf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trivaw.stage.sarf.Entities.BureauDeChange;
import trivaw.stage.sarf.Entities.TauxDeChange;
import trivaw.stage.sarf.services.ITauxChangeService;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/TauxDeChange")
public class TauxChangeController {

    @Autowired
    ITauxChangeService iTauxChangeService;
    @GetMapping("/retrieve-TauxDeChange")
    public List<TauxDeChange> getAllTauxDeChange() {
        return iTauxChangeService.getAllTauxDeChange();
    }

    @GetMapping("/getTauxDeChangeById/{IdTauxDeChange}")
    public TauxDeChange getTauxDeChangeById(@PathVariable("IdTauxDeChange") Integer IdTauxDeChange) {
        return iTauxChangeService.getTauxDeChangeById(IdTauxDeChange);
    }
    @PostMapping("/createTauxDeChange")
    public TauxDeChange createTauxDeChange(@RequestBody TauxDeChange a) {
        return iTauxChangeService.createTauxDeChange(a);
    }

    @PutMapping("/update/{idTauxDeChange}")
    public TauxDeChange updateTauxDeChange(@PathVariable("idTauxDeChange") Integer idTauxDeChange, @RequestBody TauxDeChange a) {
        return iTauxChangeService.updateTauxDeChange(idTauxDeChange, a);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTauxDeChange(@PathVariable("id") Integer idTauxDeChange) {
        iTauxChangeService.deleteTauxDeChange(idTauxDeChange);
    }

    @GetMapping("/getTauxDeChangeByUser/{idUser}")
    public List<TauxDeChange> getTauxDeChangeByUser(@PathVariable("idUser") Integer idUser){
        return iTauxChangeService.getTauxDeChangeByUser(idUser);
    }
    @PutMapping("/updatebyUser/{idUser}/{idTauxDeChange}")
    public void updateTauxDeChangeByUser(@PathVariable("idUser") Integer idUser, @PathVariable("idTauxDeChange") Integer idTauxDeChange, @RequestBody TauxDeChange a) {
        iTauxChangeService.updateTauxDeChangeByUser(idUser, a, idTauxDeChange);
    }
    @PostMapping("/createTauxDeChangeByUser/{idUser}")
    public TauxDeChange createTauxDeChangeByUser(@PathVariable("idUser") Integer idUser, @RequestBody TauxDeChange a) {
        return iTauxChangeService.createTauxDeChangeByUser(idUser,a);
    }
    @GetMapping("/getTauxDeChangeByBureau/{idBureauDeChange}")
    public List<TauxDeChange> getTauxDeChangeByBureau(@PathVariable("idBureauDeChange") Integer idBureauDeChange) {
        return iTauxChangeService.getTauxDeChangeByBureau(idBureauDeChange);
    }

    @GetMapping("/TriAchatDevise/{deviseSource}")
    public List<Object[]> getTauxDeChangeSortedByTauxAchatAscForDevise(@PathVariable String deviseSource) {
        return iTauxChangeService.findTauxDeChangeAndBureauNomSortedByTauxAchatAscForDevise(deviseSource);
    }


    @GetMapping("/TriVenteDevise/{deviseSource}")
    public List<Object[]> getTauxDeChangeSortedByTauxVenteAscForDevise(@PathVariable String deviseSource) {
        return iTauxChangeService.findTauxDeChangeAndBureauNomSortedByTauxVenteAscForDevise(deviseSource);
    }

}
