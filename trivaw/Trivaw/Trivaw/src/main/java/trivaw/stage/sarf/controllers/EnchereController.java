package trivaw.stage.sarf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.Entities.Enchere;
import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.services.EnchereService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/enchere")
public class EnchereController {


    @Autowired
    EnchereService enchereService;


    @GetMapping("/getAllEnchere")
    public List<Enchere> getAllEnchere() {
        return enchereService.getAllEnchere();

    }
    @DeleteMapping("/delete/{idEnchere}")
    public void deleteEnchere(@PathVariable("idEnchere") Integer idEnchere) {
        enchereService.deleteEnchere(idEnchere);
    }


    @GetMapping("/findEncherByUser/{idUser}")
    public ResponseEntity<List<Enchere>> getEncheresByUserId(@PathVariable Integer idUser) {
        List<Enchere> encheres = enchereService.findEnchereByIdUser(idUser);
        return ResponseEntity.ok(encheres);
    }

    @GetMapping("/getEnchereById/{idEnchere}")
    public Enchere getEnchereById(@PathVariable("idEnchere") Integer idEnchere) {
        return enchereService.getEnchereById(idEnchere);
    }
   /* @PostMapping("/creerEnchere")
    public Enchere creerEnchere(@RequestBody Enchere enchere) {
        return enchereService.creerEnchere(enchere);
    }

    */
    @PostMapping("/creerEnchere/{idUser}")
    public ResponseEntity<Enchere> creerEnchere(@RequestBody Enchere enchere, @PathVariable Integer idUser) {
        Enchere nouvelleEnchere = enchereService.creerEnchere(enchere, idUser);
        return new ResponseEntity<>(nouvelleEnchere, HttpStatus.CREATED);
    }

    @PostMapping("/participer/{idEnchere}/{tauxPropose}/{idUser}")
    public ResponseEntity<?> participerEnchere(@PathVariable("idEnchere") Integer idEnchere, @PathVariable("tauxPropose") Double tauxPropose, @PathVariable("idUser") Integer idUser ) {
        try {
            Enchere enchere = enchereService.participerEnchere(idEnchere, tauxPropose, idUser);

            return ResponseEntity.ok().body(enchere);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
