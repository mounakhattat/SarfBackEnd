package trivaw.stage.sarf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.Entities.Reservation;
import trivaw.stage.sarf.repository.ReservationRepository;

import java.util.List;
@Service
public class ReservationService implements  IReservationService{
    @Autowired
    ReservationRepository reservationRepository;
    @Override
    public List<Reservation> getAllReservation() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Integer idReservation) {
        return reservationRepository.findById(idReservation).orElse(null);
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {

        reservationRepository.save(reservation);
        return reservation;
    }
}
