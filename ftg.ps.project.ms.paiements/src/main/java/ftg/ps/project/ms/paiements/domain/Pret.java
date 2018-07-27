package ftg.ps.project.ms.paiements.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Pret.
 */
@Entity
@Table(name = "pret")
public class Pret implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "num_pret")
    private Long numPret;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_mis_ajour")
    private LocalDate dateMisAjour;

    @Column(name = "capital_restant", precision = 10, scale = 2)
    private BigDecimal capitalRestant;

    @Column(name = "date_dernier_remb")
    private LocalDate dateDernierRemb;

    @Column(name = "id_owner")
    private Long idOwner;

    @ManyToOne
    @JsonIgnoreProperties("ligneCredits")
    private LigneCredit ligneCredit;

    @OneToOne
    @JoinColumn(unique = true)
    private EtatPret pret;

    @ManyToOne
    @JsonIgnoreProperties("reglementCommandePrets")
    private ReglementCommande reglementCommande;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumPret() {
        return numPret;
    }

    public Pret numPret(Long numPret) {
        this.numPret = numPret;
        return this;
    }

    public void setNumPret(Long numPret) {
        this.numPret = numPret;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public Pret dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateMisAjour() {
        return dateMisAjour;
    }

    public Pret dateMisAjour(LocalDate dateMisAjour) {
        this.dateMisAjour = dateMisAjour;
        return this;
    }

    public void setDateMisAjour(LocalDate dateMisAjour) {
        this.dateMisAjour = dateMisAjour;
    }

    public BigDecimal getCapitalRestant() {
        return capitalRestant;
    }

    public Pret capitalRestant(BigDecimal capitalRestant) {
        this.capitalRestant = capitalRestant;
        return this;
    }

    public void setCapitalRestant(BigDecimal capitalRestant) {
        this.capitalRestant = capitalRestant;
    }

    public LocalDate getDateDernierRemb() {
        return dateDernierRemb;
    }

    public Pret dateDernierRemb(LocalDate dateDernierRemb) {
        this.dateDernierRemb = dateDernierRemb;
        return this;
    }

    public void setDateDernierRemb(LocalDate dateDernierRemb) {
        this.dateDernierRemb = dateDernierRemb;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public Pret idOwner(Long idOwner) {
        this.idOwner = idOwner;
        return this;
    }

    public void setIdOwner(Long idOwner) {
        this.idOwner = idOwner;
    }

    public LigneCredit getLigneCredit() {
        return ligneCredit;
    }

    public Pret ligneCredit(LigneCredit ligneCredit) {
        this.ligneCredit = ligneCredit;
        return this;
    }

    public void setLigneCredit(LigneCredit ligneCredit) {
        this.ligneCredit = ligneCredit;
    }

    public EtatPret getPret() {
        return pret;
    }

    public Pret pret(EtatPret etatPret) {
        this.pret = etatPret;
        return this;
    }

    public void setPret(EtatPret etatPret) {
        this.pret = etatPret;
    }

    public ReglementCommande getReglementCommande() {
        return reglementCommande;
    }

    public Pret reglementCommande(ReglementCommande reglementCommande) {
        this.reglementCommande = reglementCommande;
        return this;
    }

    public void setReglementCommande(ReglementCommande reglementCommande) {
        this.reglementCommande = reglementCommande;
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
        Pret pret = (Pret) o;
        if (pret.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pret.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pret{" +
            "id=" + getId() +
            ", numPret=" + getNumPret() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateMisAjour='" + getDateMisAjour() + "'" +
            ", capitalRestant=" + getCapitalRestant() +
            ", dateDernierRemb='" + getDateDernierRemb() + "'" +
            ", idOwner=" + getIdOwner() +
            "}";
    }
}
