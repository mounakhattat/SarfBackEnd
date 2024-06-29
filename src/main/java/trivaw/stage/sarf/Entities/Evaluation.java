package trivaw.stage.sarf.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
    public class Evaluation implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer idEvaluation;
    @Column(length = 1000)
    private String commentaire;
        private Double note;
        private Double emplacement;
        private Double service;
        private Double qualiteOffre;
        private LocalDateTime date;
        private String evaluateur;
     @Transient // Indique que ce champ n'est pas mappé dans la base de données
        private String bureauDeChangeNom;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tourist_id")
        private User touriste;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bureau_de_change_id")
        private User bureauDeChange;
        public void calculateNote() {
        this.note = (this.emplacement + this.service + this.qualiteOffre) / 3;
    }
    public String getBureauDeChangeNom() {
        if (bureauDeChange != null) {
            return bureauDeChange.getUsername();
        } else {
            return null; // ou une autre valeur par défaut si nécessaire
        }
    }

}
