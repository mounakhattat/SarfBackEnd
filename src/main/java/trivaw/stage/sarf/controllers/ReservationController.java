package trivaw.stage.sarf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;
import trivaw.stage.sarf.Configuration.WebSocketEndpoint;
import trivaw.stage.sarf.Entities.BureauDeChange;
import trivaw.stage.sarf.Entities.Reservation;
import trivaw.stage.sarf.Entities.TauxDeChange;
import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.services.BureauDeChangeServices;
import trivaw.stage.sarf.services.ReservationService;
import trivaw.stage.sarf.services.TauxChangeService;
import trivaw.stage.sarf.services.UserService;

import javax.mail.MessagingException;
import javax.websocket.Session;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin("*")
@RestController
@RequestMapping("/reservation")

public class ReservationController {
    @Autowired
    ReservationService reservationService;
    @Autowired
    TauxChangeService tauxChangeService;
@Autowired
    UserService userService;
    @Autowired
    BureauDeChangeServices bureauDeChangeServices;

    @GetMapping("/getAllReservation")
    public List<Reservation> getAllReservation() {
        return reservationService.getAllReservation();
    }

    //http://localhost:8083/Account/get-Account/{id}
    @GetMapping("/getReservationById/{idReservation}")
    public Reservation getReservationById(@PathVariable("idReservation") Integer idReservation) {
        return reservationService.getReservationById(idReservation);
    }


    @GetMapping("/getReservationByUser/{idUser}")
    public List<Reservation>  getReservationByUser(@PathVariable("idUser") Integer idUser) {
        return reservationService.getReservationByUser(idUser);
    }
    //validation Reservation by bureau de change
    @PutMapping("/updateReservation/{idReservation}")
    public Reservation updateReservation(@PathVariable("idReservation") Integer idReservation, @RequestBody Reservation a) throws MessagingException,  IOException {

        return reservationService.updateReservation(idReservation, a);

    }
    @PostMapping("/reservation/{idUser}/{idBureauDeChange}/{idTauxDeChange}/{device}")
    public ResponseEntity<Reservation> addReservationAndSendToExchange(@RequestBody Reservation reservation,
                                                                       @PathVariable("idUser") Integer idUser,
                                                                       @PathVariable("idBureauDeChange") Integer idBureauDeChange,
                                                                       @PathVariable("idTauxDeChange") Integer idTauxDeChange

                                                                       ) {
        // Récupérer l'utilisateur et attribuer à la réservation
        User user = userService.getUserById(idUser);
        reservation.setUser(user);

        // Récupérer le bureau de change correspondant à idBureauDeChange
        BureauDeChange bureauDeChange = bureauDeChangeServices.getBureauDeChangeById(idBureauDeChange);


        // Assigner l'ID du bureau de change à la réservation
        reservation.setBureauDeChangeId(bureauDeChange.getIdBureauDeChange());
        // Récupérer le taux de change correspondant à idTauxDeChange
        TauxDeChange tauxDeChange = tauxChangeService.getTauxDeChangeById(idTauxDeChange);
        if (reservation.getType().equals("Achat")) {
            reservation.setTauxDeChange(tauxDeChange.getTauxAchat());
            System.out.println(reservation.getTauxDeChange()+"bhxbjhbxsbxbs");
        } else if (reservation.getType().equals("Vente")) {
            reservation.setTauxDeChange(tauxDeChange.getTauxVente());
            System.out.println(reservation.getTauxDeChange()+"cddddddddddddddddd");

        }
        // Assigner le taux de change à la réservation
        reservation.setDevise(tauxDeChange.getDeviseSource());
        reservation.setEmetteur(user.getUsername());


            // Enregistrer la réservation
            Reservation savedReservation = reservationService.saveReservation(reservation);

            // Envoyer la réservation au bureau de change
            try {
                bureauDeChangeServices.sendMessageToExchange(savedReservation, idBureauDeChange, idUser);
            } catch (IOException e) {
                e.printStackTrace(); // Gérer l'erreur
            }

            return ResponseEntity.ok(savedReservation);
        }


}
