package ftg.ps.project.ms.paiements.repository;

import ftg.ps.project.ms.paiements.domain.Pret;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Pret entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PretRepository extends JpaRepository<Pret, Long> {

}
