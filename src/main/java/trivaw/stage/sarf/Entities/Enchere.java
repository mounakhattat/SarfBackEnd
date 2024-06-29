package trivaw.stage.sarf.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "Enchere")
public class Enchere implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idEnchere")
    private Integer idEnchere;
    private  Double montant;
    private  String devise;

    private  String type;

    private LocalDateTime dateDebut;
  //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")

    private LocalDateTime dateFin;
    private String winner;
    private String status;
    private Double tauxPropose;

   @ManyToOne
    private User user;





}
