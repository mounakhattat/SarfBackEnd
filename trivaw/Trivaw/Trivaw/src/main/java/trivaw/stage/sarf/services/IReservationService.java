package trivaw.stage.sarf.services;

import trivaw.stage.sarf.Entities.Reservation;

import java.util.List;

public interface IReservationService {
    List<Reservation> getAllReservation();
    Reservation getReservationById(Integer idReservation);
    Reservation saveReservation(Reservation a);
}
