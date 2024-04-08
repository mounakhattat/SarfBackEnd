package trivaw.stage.sarf.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.relation.Role;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "Account")
public class Account implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="idAccount")

    private Integer idAcc;
    private Integer numAccount;
    private Date dateCreation;
    private Integer rib;
    private Integer amountTrans;

    private Boolean banned;

    private Date bannedPeriode;
    private Integer Ageuser;
    private Integer balance;
    @OneToOne(mappedBy ="account", cascade = CascadeType.ALL)
    User user;

    public Integer getIdAcc() {
        return idAcc;
    }

    public void setIdAcc(Integer idAcc) {
        this.idAcc = idAcc;
    }

    public Integer getNumAccount() {
        return numAccount;
    }

    public void setNumAccount(Integer numAccount) {
        numAccount = numAccount;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        dateCreation = dateCreation;
    }

    public Integer getRib() {
        return rib;
    }

    public void setRib(Integer rib) {
        rib = rib;
    }

    public Integer getAmountTrans() {
        return amountTrans;
    }

    public void setAmountTrans(Integer amountTrans) {
        amountTrans = amountTrans;

    }

}

