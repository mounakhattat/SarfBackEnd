package trivaw.stage.sarf.services;

import trivaw.stage.sarf.Entities.Reservation;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IReservationService {
    List<Reservation> getAllReservation();
    Reservation getReservationById(Integer idReservation);
    Reservation saveReservation(Reservation a);
    Reservation updateReservation(Integer idReservation, Reservation a) throws MessagingException, IOException;
}
