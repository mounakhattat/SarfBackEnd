package trivaw.stage.sarf.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String emetteur;
    private String devise;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String status;
private Double tauxDeChange;
private Integer bureauDeChangeId; // Ajouter un champ pour stocker l'ID du bureau de change
    private String type;

    @ManyToOne
    private User user;

}
