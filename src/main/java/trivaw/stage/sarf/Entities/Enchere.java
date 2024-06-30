package trivaw.stage.sarf.Entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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
