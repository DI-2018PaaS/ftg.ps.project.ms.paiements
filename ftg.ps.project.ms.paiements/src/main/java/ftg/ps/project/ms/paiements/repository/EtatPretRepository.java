package ftg.ps.project.ms.paiements.repository;

import ftg.ps.project.ms.paiements.domain.EtatPret;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EtatPret entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtatPretRepository extends JpaRepository<EtatPret, Long> {

}
