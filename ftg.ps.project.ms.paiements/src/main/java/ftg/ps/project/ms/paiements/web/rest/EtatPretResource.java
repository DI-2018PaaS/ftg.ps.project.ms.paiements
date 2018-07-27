package ftg.ps.project.ms.paiements.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftg.ps.project.ms.paiements.domain.EtatPret;
import ftg.ps.project.ms.paiements.repository.EtatPretRepository;
import ftg.ps.project.ms.paiements.web.rest.errors.BadRequestAlertException;
import ftg.ps.project.ms.paiements.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EtatPret.
 */
@RestController
@RequestMapping("/api")
public class EtatPretResource {

    private final Logger log = LoggerFactory.getLogger(EtatPretResource.class);

    private static final String ENTITY_NAME = "etatPret";

    private final EtatPretRepository etatPretRepository;

    public EtatPretResource(EtatPretRepository etatPretRepository) {
        this.etatPretRepository = etatPretRepository;
    }

    /**
     * POST  /etat-prets : Create a new etatPret.
     *
     * @param etatPret the etatPret to create
     * @return the ResponseEntity with status 201 (Created) and with body the new etatPret, or with status 400 (Bad Request) if the etatPret has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/etat-prets")
    @Timed
    public ResponseEntity<EtatPret> createEtatPret(@RequestBody EtatPret etatPret) throws URISyntaxException {
        log.debug("REST request to save EtatPret : {}", etatPret);
        if (etatPret.getId() != null) {
            throw new BadRequestAlertException("A new etatPret cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EtatPret result = etatPretRepository.save(etatPret);
        return ResponseEntity.created(new URI("/api/etat-prets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /etat-prets : Updates an existing etatPret.
     *
     * @param etatPret the etatPret to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated etatPret,
     * or with status 400 (Bad Request) if the etatPret is not valid,
     * or with status 500 (Internal Server Error) if the etatPret couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/etat-prets")
    @Timed
    public ResponseEntity<EtatPret> updateEtatPret(@RequestBody EtatPret etatPret) throws URISyntaxException {
        log.debug("REST request to update EtatPret : {}", etatPret);
        if (etatPret.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EtatPret result = etatPretRepository.save(etatPret);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, etatPret.getId().toString()))
            .body(result);
    }

    /**
     * GET  /etat-prets : get all the etatPrets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of etatPrets in body
     */
    @GetMapping("/etat-prets")
    @Timed
    public List<EtatPret> getAllEtatPrets() {
        log.debug("REST request to get all EtatPrets");
        return etatPretRepository.findAll();
    }

    /**
     * GET  /etat-prets/:id : get the "id" etatPret.
     *
     * @param id the id of the etatPret to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the etatPret, or with status 404 (Not Found)
     */
    @GetMapping("/etat-prets/{id}")
    @Timed
    public ResponseEntity<EtatPret> getEtatPret(@PathVariable Long id) {
        log.debug("REST request to get EtatPret : {}", id);
        Optional<EtatPret> etatPret = etatPretRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(etatPret);
    }

    /**
     * DELETE  /etat-prets/:id : delete the "id" etatPret.
     *
     * @param id the id of the etatPret to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/etat-prets/{id}")
    @Timed
    public ResponseEntity<Void> deleteEtatPret(@PathVariable Long id) {
        log.debug("REST request to delete EtatPret : {}", id);

        etatPretRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
