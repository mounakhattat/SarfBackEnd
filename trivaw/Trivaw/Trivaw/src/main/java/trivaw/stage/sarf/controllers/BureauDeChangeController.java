package trivaw.stage.sarf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.Entities.BureauDeChange;
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

    @GetMapping("/getBureauDeChangeById/{IdBureauDeChange}")
    public BureauDeChange getBureauDeChangeById(@PathVariable("IdBureauDeChange") Integer IdBureauDeChange) {
        return bureauDeChangeServices.getBureauDeChangeById(IdBureauDeChange);
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
    @PutMapping("/updatebyUser/{idUser}")
    public void updateBureauDeChangeByUser(@PathVariable("idUser") Integer idUser, @RequestBody BureauDeChange a) {
         bureauDeChangeServices.updateBureauDeChangeByUser(idUser, a);
    }
}
