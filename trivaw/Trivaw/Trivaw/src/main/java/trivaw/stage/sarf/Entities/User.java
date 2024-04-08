package trivaw.stage.sarf.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
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
    private Date birthDate;
    private String placeBirth;
    private String job;
    private String sex;
    private String housing;
    private Integer postalCode;
    private String email;
    private boolean actived;
    private String numPhone;
    private String password;
    private Date dateCreation;
    private Float Salary;

    private Integer KidsNumber;
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

}