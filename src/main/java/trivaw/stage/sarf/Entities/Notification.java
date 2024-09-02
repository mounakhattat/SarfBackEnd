package trivaw.stage.sarf.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "Notification")
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idNotification")
    private Integer idNotification;
    private String message;
    private LocalDateTime date;
    private String type;
    @JsonIgnore
    @ManyToOne
    private User userr;


}
