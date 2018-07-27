package ftg.ps.project.ms.paiements.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A RemboursementCredit.
 */
@Entity
@Table(name = "remboursement_credit")
public class RemboursementCredit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_remboursement")
    private Long idRemboursement;

    @Column(name = "date_remboursement")
    private LocalDate dateRemboursement;

    @Column(name = "id_owner")
    private Long idOwner;

    @OneToOne
    @JoinColumn(unique = true)
    private Pret remboursementCredit;

    @OneToMany(mappedBy = "remboursementCredit")
    private Set<DirectPaiement> remboursementCredits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdRemboursement() {
        return idRemboursement;
    }

    public RemboursementCredit idRemboursement(Long idRemboursement) {
        this.idRemboursement = idRemboursement;
        return this;
    }

    public void setIdRemboursement(Long idRemboursement) {
        this.idRemboursement = idRemboursement;
    }

    public LocalDate getDateRemboursement() {
        return dateRemboursement;
    }

    public RemboursementCredit dateRemboursement(LocalDate dateRemboursement) {
        this.dateRemboursement = dateRemboursement;
        return this;
    }

    public void setDateRemboursement(LocalDate dateRemboursement) {
        this.dateRemboursement = dateRemboursement;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public RemboursementCredit idOwner(Long idOwner) {
        this.idOwner = idOwner;
        return this;
    }

    public void setIdOwner(Long idOwner) {
        this.idOwner = idOwner;
    }

    public Pret getRemboursementCredit() {
        return remboursementCredit;
    }

    public RemboursementCredit remboursementCredit(Pret pret) {
        this.remboursementCredit = pret;
        return this;
    }

    public void setRemboursementCredit(Pret pret) {
        this.remboursementCredit = pret;
    }

    public Set<DirectPaiement> getRemboursementCredits() {
        return remboursementCredits;
    }

    public RemboursementCredit remboursementCredits(Set<DirectPaiement> directPaiements) {
        this.remboursementCredits = directPaiements;
        return this;
    }

    public RemboursementCredit addRemboursementCredit(DirectPaiement directPaiement) {
        this.remboursementCredits.add(directPaiement);
        directPaiement.setRemboursementCredit(this);
        return this;
    }

    public RemboursementCredit removeRemboursementCredit(DirectPaiement directPaiement) {
        this.remboursementCredits.remove(directPaiement);
        directPaiement.setRemboursementCredit(null);
        return this;
    }

    public void setRemboursementCredits(Set<DirectPaiement> directPaiements) {
        this.remboursementCredits = directPaiements;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RemboursementCredit remboursementCredit = (RemboursementCredit) o;
        if (remboursementCredit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), remboursementCredit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RemboursementCredit{" +
            "id=" + getId() +
            ", idRemboursement=" + getIdRemboursement() +
            ", dateRemboursement='" + getDateRemboursement() + "'" +
            ", idOwner=" + getIdOwner() +
            "}";
    }
}
