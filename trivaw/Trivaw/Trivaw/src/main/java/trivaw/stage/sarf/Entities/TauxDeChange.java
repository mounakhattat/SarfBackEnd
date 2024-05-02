package trivaw.stage.sarf.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "TauxDeChange")

public class TauxDeChange implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idTauxDeChange")
    private Integer idTauxDeChange;
    private String deviseCible;
    private String deviseSource;
    private Integer unite;
    private double tauxAchat;
    private double tauxVente;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;


    @ManyToOne
    private BureauDeChange bureauDeChange;





}
