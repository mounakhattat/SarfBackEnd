package trivaw.stage.sarf.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idReservation")
    private Integer idReservation;
    private Double montant;
    private String devise;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
private String status;







}
