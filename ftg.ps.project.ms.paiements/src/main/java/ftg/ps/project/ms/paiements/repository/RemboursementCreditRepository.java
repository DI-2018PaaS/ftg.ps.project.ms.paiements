package ftg.ps.project.ms.paiements.repository;

import ftg.ps.project.ms.paiements.domain.RemboursementCredit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RemboursementCredit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RemboursementCreditRepository extends JpaRepository<RemboursementCredit, Long> {

}
