package ftg.ps.project.ms.paiements.repository;

import ftg.ps.project.ms.paiements.domain.LigneCredit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LigneCredit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneCreditRepository extends JpaRepository<LigneCredit, Long> {

}
