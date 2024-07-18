package trivaw.stage.sarf.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
public class Reclamation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idReclamation")
    private Integer idReclamation;
    private String sujet;
    private String titre;
    private String reclameur;
    private Status status;
    private String responsable;
    private LocalDateTime date;
    private String destinateur;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "touriste_id")
    private User touriste;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bureau_de_change_id")
    private User bureauDeChange;

}
