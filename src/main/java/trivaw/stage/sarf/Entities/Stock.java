package trivaw.stage.sarf.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "Stock")
public class Stock implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idStock")
    private Integer idStock;
    private String devise;
    private Double quantite;
    @JsonIgnore
@ManyToOne
    private BureauDeChange bureauDeChange;
}
