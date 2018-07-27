package ftg.ps.project.ms.paiements.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A EtatPret.
 */
@Entity
@Table(name = "etat_pret")
public class EtatPret implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_etat")
    private Long idEtat;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEtat() {
        return idEtat;
    }

    public EtatPret idEtat(Long idEtat) {
        this.idEtat = idEtat;
        return this;
    }

    public void setIdEtat(Long idEtat) {
        this.idEtat = idEtat;
    }

    public String getLibelle() {
        return libelle;
    }

    public EtatPret libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public EtatPret description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
        EtatPret etatPret = (EtatPret) o;
        if (etatPret.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), etatPret.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EtatPret{" +
            "id=" + getId() +
            ", idEtat=" + getIdEtat() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
