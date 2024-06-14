package trivaw.stage.sarf.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Transactional
@Entity
@Table(name = "userapp")

public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idUser")
    private Integer idUser;
    private String firstName;
    private String lastName;
    private ERole roles ;
    private String username;
    private Integer age;
    private String pays;

    private String ville;

    private String sexe;

    private String email;
    private boolean actived;
    private String numPhone;
    private String password;
    private Date dateCreation;


    private String newPassword;
private String code;
private Boolean banned;
private Date bannedPeriode;

    public User(String username, String email, String encode) {
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    @OneToOne
    private PasswordResetToken passwordResetToken;

    public String getUsername() {
        return username;
    }

    public boolean isActived() {
        return actived;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Date getBannedPeriode() {
        return bannedPeriode;
    }

    public void setBannedPeriode(Date bannedPeriode) {
        this.bannedPeriode = bannedPeriode;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +

                ", Email='" + email + '\'' +

                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +

                ", username='" + username + '\'' +
                ", Account=" + account +
                ", roles=" + roles +
                '}';
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    public void setRoles(ERole role) {
        this.roles = role;
    }
    @OneToOne(mappedBy = "user")
    private BureauDeChange bureauDeChange;
@JsonIgnore
    @OneToMany( cascade = CascadeType.ALL ,mappedBy = "user")
    private List<Enchere> encheres;
    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL ,mappedBy = "user")
    private List<Reservation> reservations;
    @JsonIgnore
    @OneToMany(mappedBy = "touriste", cascade = CascadeType.ALL)
    private Set<Evaluation> evaluationsAsTouriste;
    @JsonIgnore
    @OneToMany(mappedBy = "bureauDeChange", cascade = CascadeType.ALL)
    private Set<Evaluation> evaluationsAsBureauDeChange;


}