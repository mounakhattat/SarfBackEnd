package trivaw.stage.sarf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;
import trivaw.stage.sarf.Configuration.WebSocketEndpoint;
import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.Entities.Reservation;
import trivaw.stage.sarf.services.BureauDeChangeServices;
import trivaw.stage.sarf.services.ReservationService;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin("*")
@RestController

public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @Autowired
    BureauDeChangeServices bureauDeChangeServices;
    @PostMapping("/reservation")
    public ResponseEntity<Reservation> addReservationAndSendToExchange(@RequestBody Reservation reservation) {
        Reservation savedReservation = reservationService.saveReservation(reservation);
        CopyOnWriteArrayList<WebSocketSession> sessions = WebSocketEndpoint.getSessions();
        for (WebSocketSession session : sessions) {
            try {
                bureauDeChangeServices.sendMessageToExchange(savedReservation, session);
            } catch (IOException e) {
                e.printStackTrace(); // Gérer l'erreur
            }
        }
        return ResponseEntity.ok(savedReservation);
    }

}
