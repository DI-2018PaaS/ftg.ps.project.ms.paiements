package ftg.ps.project.ms.paiements.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A LigneCredit.
 */
@Entity
@Table(name = "ligne_credit")
public class LigneCredit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_ligne_credit")
    private Long idLigneCredit;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "description")
    private String description;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_mis_ajour")
    private LocalDate dateMisAjour;

    @Column(name = "montant", precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(name = "id_owner")
    private Long idOwner;

    @Column(name = "nom_financier")
    private String nomFinancier;

    @OneToMany(mappedBy = "ligneCredit")
    private Set<Pret> ligneCredits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdLigneCredit() {
        return idLigneCredit;
    }

    public LigneCredit idLigneCredit(Long idLigneCredit) {
        this.idLigneCredit = idLigneCredit;
        return this;
    }

    public void setIdLigneCredit(Long idLigneCredit) {
        this.idLigneCredit = idLigneCredit;
    }

    public String getLibelle() {
        return libelle;
    }

    public LigneCredit libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public LigneCredit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public LigneCredit dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateMisAjour() {
        return dateMisAjour;
    }

    public LigneCredit dateMisAjour(LocalDate dateMisAjour) {
        this.dateMisAjour = dateMisAjour;
        return this;
    }

    public void setDateMisAjour(LocalDate dateMisAjour) {
        this.dateMisAjour = dateMisAjour;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public LigneCredit montant(BigDecimal montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public LigneCredit idOwner(Long idOwner) {
        this.idOwner = idOwner;
        return this;
    }

    public void setIdOwner(Long idOwner) {
        this.idOwner = idOwner;
    }

    public String getNomFinancier() {
        return nomFinancier;
    }

    public LigneCredit nomFinancier(String nomFinancier) {
        this.nomFinancier = nomFinancier;
        return this;
    }

    public void setNomFinancier(String nomFinancier) {
        this.nomFinancier = nomFinancier;
    }

    public Set<Pret> getLigneCredits() {
        return ligneCredits;
    }

    public LigneCredit ligneCredits(Set<Pret> prets) {
        this.ligneCredits = prets;
        return this;
    }

    public LigneCredit addLigneCredit(Pret pret) {
        this.ligneCredits.add(pret);
        pret.setLigneCredit(this);
        return this;
    }

    public LigneCredit removeLigneCredit(Pret pret) {
        this.ligneCredits.remove(pret);
        pret.setLigneCredit(null);
        return this;
    }

    public void setLigneCredits(Set<Pret> prets) {
        this.ligneCredits = prets;
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
        LigneCredit ligneCredit = (LigneCredit) o;
        if (ligneCredit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ligneCredit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LigneCredit{" +
            "id=" + getId() +
            ", idLigneCredit=" + getIdLigneCredit() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateMisAjour='" + getDateMisAjour() + "'" +
            ", montant=" + getMontant() +
            ", idOwner=" + getIdOwner() +
            ", nomFinancier='" + getNomFinancier() + "'" +
            "}";
    }
}
