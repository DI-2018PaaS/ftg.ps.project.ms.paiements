package ftg.ps.project.ms.paiements.repository;

import ftg.ps.project.ms.paiements.domain.ReglementCommande;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReglementCommande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReglementCommandeRepository extends JpaRepository<ReglementCommande, Long> {

}
