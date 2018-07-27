package ftg.ps.project.ms.paiements.repository;

import ftg.ps.project.ms.paiements.domain.DirectPaiement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DirectPaiement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirectPaiementRepository extends JpaRepository<DirectPaiement, Long> {

}
