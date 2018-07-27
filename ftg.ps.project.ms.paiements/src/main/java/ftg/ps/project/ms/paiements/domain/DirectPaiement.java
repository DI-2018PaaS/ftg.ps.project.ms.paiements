package ftg.ps.project.ms.paiements.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DirectPaiement.
 */
@Entity
@Table(name = "direct_paiement")
public class DirectPaiement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_mode_paiement")
    private Long idModePaiement;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("remboursementCredits")
    private RemboursementCredit remboursementCredit;

    @ManyToOne
    @JsonIgnoreProperties("reglementCommandeDirectPaiements")
    private ReglementCommande reglementCommande;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdModePaiement() {
        return idModePaiement;
    }

    public DirectPaiement idModePaiement(Long idModePaiement) {
        this.idModePaiement = idModePaiement;
        return this;
    }

    public void setIdModePaiement(Long idModePaiement) {
        this.idModePaiement = idModePaiement;
    }

    public String getLibelle() {
        return libelle;
    }

    public DirectPaiement libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public DirectPaiement description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RemboursementCredit getRemboursementCredit() {
        return remboursementCredit;
    }

    public DirectPaiement remboursementCredit(RemboursementCredit remboursementCredit) {
        this.remboursementCredit = remboursementCredit;
        return this;
    }

    public void setRemboursementCredit(RemboursementCredit remboursementCredit) {
        this.remboursementCredit = remboursementCredit;
    }

    public ReglementCommande getReglementCommande() {
        return reglementCommande;
    }

    public DirectPaiement reglementCommande(ReglementCommande reglementCommande) {
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
        DirectPaiement directPaiement = (DirectPaiement) o;
        if (directPaiement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), directPaiement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DirectPaiement{" +
            "id=" + getId() +
            ", idModePaiement=" + getIdModePaiement() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
