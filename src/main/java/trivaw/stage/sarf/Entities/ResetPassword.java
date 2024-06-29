package trivaw.stage.sarf.Entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name="ResetPassword")
public class ResetPassword implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IdPasswordToken")
    private Long IdPasswordToken;
    @Column(name="token")
    private String token;
    @Column(name="expireDate")
    private Instant expireDate;

    @OneToOne
    private User user;



    public ResetPassword() {

    }

    public Long getIdPasswordToken() {
        return IdPasswordToken;
    }

    public void setIdPasswordToken(Long idPasswordToken) {
        IdPasswordToken = idPasswordToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "ResetPassword{" +
                "IdPasswordToken=" + IdPasswordToken +
                ", token='" + token + '\'' +
                ", expireDate=" + expireDate +
                ", user=" + user +
                '}';
    }
}
