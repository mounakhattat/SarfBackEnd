package trivaw.stage.sarf.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "bureauDeChange")

public class BureauDeChange implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idBureauDeChange")
    private Integer idBureauDeChange;
    private String nom;
    private String email;
    private String phoneNumber;
    private String localisation;
    private String horrairesDetravail;
    private String evaluation;
    private Date bannedPeriod;
    private String country;
    private Double latitude ;
    private Double longitude ;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "idUser") // Spécifie la clé étrangère
    private User user;
    @JsonIgnore     // nrajja3ha l taux

    @OneToMany( cascade = CascadeType.ALL ,mappedBy = "bureauDeChange")
    private List<TauxDeChange> tauxDeChangeList;
@JsonIgnore
@OneToMany ( cascade = CascadeType.ALL ,mappedBy = "bureauDeChange")
    private List<Stock> stocks;


}
