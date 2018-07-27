package ftg.ps.project.ms.paiements.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ReglementCommande.
 */
@Entity
@Table(name = "reglement_commande")
public class ReglementCommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_reglement")
    private Long idReglement;

    @Column(name = "date_reglement")
    private LocalDate dateReglement;

    @Column(name = "id_bon_de_commande")
    private Long idBonDeCommande;

    @OneToMany(mappedBy = "reglementCommande")
    private Set<Pret> reglementCommandePrets = new HashSet<>();

    @OneToMany(mappedBy = "reglementCommande")
    private Set<DirectPaiement> reglementCommandeDirectPaiements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdReglement() {
        return idReglement;
    }

    public ReglementCommande idReglement(Long idReglement) {
        this.idReglement = idReglement;
        return this;
    }

    public void setIdReglement(Long idReglement) {
        this.idReglement = idReglement;
    }

    public LocalDate getDateReglement() {
        return dateReglement;
    }

    public ReglementCommande dateReglement(LocalDate dateReglement) {
        this.dateReglement = dateReglement;
        return this;
    }

    public void setDateReglement(LocalDate dateReglement) {
        this.dateReglement = dateReglement;
    }

    public Long getIdBonDeCommande() {
        return idBonDeCommande;
    }

    public ReglementCommande idBonDeCommande(Long idBonDeCommande) {
        this.idBonDeCommande = idBonDeCommande;
        return this;
    }

    public void setIdBonDeCommande(Long idBonDeCommande) {
        this.idBonDeCommande = idBonDeCommande;
    }

    public Set<Pret> getReglementCommandePrets() {
        return reglementCommandePrets;
    }

    public ReglementCommande reglementCommandePrets(Set<Pret> prets) {
        this.reglementCommandePrets = prets;
        return this;
    }

    public ReglementCommande addReglementCommandePret(Pret pret) {
        this.reglementCommandePrets.add(pret);
        pret.setReglementCommande(this);
        return this;
    }

    public ReglementCommande removeReglementCommandePret(Pret pret) {
        this.reglementCommandePrets.remove(pret);
        pret.setReglementCommande(null);
        return this;
    }

    public void setReglementCommandePrets(Set<Pret> prets) {
        this.reglementCommandePrets = prets;
    }

    public Set<DirectPaiement> getReglementCommandeDirectPaiements() {
        return reglementCommandeDirectPaiements;
    }

    public ReglementCommande reglementCommandeDirectPaiements(Set<DirectPaiement> directPaiements) {
        this.reglementCommandeDirectPaiements = directPaiements;
        return this;
    }

    public ReglementCommande addReglementCommandeDirectPaiement(DirectPaiement directPaiement) {
        this.reglementCommandeDirectPaiements.add(directPaiement);
        directPaiement.setReglementCommande(this);
        return this;
    }

    public ReglementCommande removeReglementCommandeDirectPaiement(DirectPaiement directPaiement) {
        this.reglementCommandeDirectPaiements.remove(directPaiement);
        directPaiement.setReglementCommande(null);
        return this;
    }

    public void setReglementCommandeDirectPaiements(Set<DirectPaiement> directPaiements) {
        this.reglementCommandeDirectPaiements = directPaiements;
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
        ReglementCommande reglementCommande = (ReglementCommande) o;
        if (reglementCommande.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reglementCommande.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReglementCommande{" +
            "id=" + getId() +
            ", idReglement=" + getIdReglement() +
            ", dateReglement='" + getDateReglement() + "'" +
            ", idBonDeCommande=" + getIdBonDeCommande() +
            "}";
    }
}
