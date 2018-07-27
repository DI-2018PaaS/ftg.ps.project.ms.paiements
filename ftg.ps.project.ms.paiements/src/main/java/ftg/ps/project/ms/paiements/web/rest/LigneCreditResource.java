package ftg.ps.project.ms.paiements.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftg.ps.project.ms.paiements.domain.LigneCredit;
import ftg.ps.project.ms.paiements.repository.LigneCreditRepository;
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
 * REST controller for managing LigneCredit.
 */
@RestController
@RequestMapping("/api")
public class LigneCreditResource {

    private final Logger log = LoggerFactory.getLogger(LigneCreditResource.class);

    private static final String ENTITY_NAME = "ligneCredit";

    private final LigneCreditRepository ligneCreditRepository;

    public LigneCreditResource(LigneCreditRepository ligneCreditRepository) {
        this.ligneCreditRepository = ligneCreditRepository;
    }

    /**
     * POST  /ligne-credits : Create a new ligneCredit.
     *
     * @param ligneCredit the ligneCredit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ligneCredit, or with status 400 (Bad Request) if the ligneCredit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ligne-credits")
    @Timed
    public ResponseEntity<LigneCredit> createLigneCredit(@RequestBody LigneCredit ligneCredit) throws URISyntaxException {
        log.debug("REST request to save LigneCredit : {}", ligneCredit);
        if (ligneCredit.getId() != null) {
            throw new BadRequestAlertException("A new ligneCredit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LigneCredit result = ligneCreditRepository.save(ligneCredit);
        return ResponseEntity.created(new URI("/api/ligne-credits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ligne-credits : Updates an existing ligneCredit.
     *
     * @param ligneCredit the ligneCredit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ligneCredit,
     * or with status 400 (Bad Request) if the ligneCredit is not valid,
     * or with status 500 (Internal Server Error) if the ligneCredit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ligne-credits")
    @Timed
    public ResponseEntity<LigneCredit> updateLigneCredit(@RequestBody LigneCredit ligneCredit) throws URISyntaxException {
        log.debug("REST request to update LigneCredit : {}", ligneCredit);
        if (ligneCredit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LigneCredit result = ligneCreditRepository.save(ligneCredit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ligneCredit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ligne-credits : get all the ligneCredits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ligneCredits in body
     */
    @GetMapping("/ligne-credits")
    @Timed
    public List<LigneCredit> getAllLigneCredits() {
        log.debug("REST request to get all LigneCredits");
        return ligneCreditRepository.findAll();
    }

    /**
     * GET  /ligne-credits/:id : get the "id" ligneCredit.
     *
     * @param id the id of the ligneCredit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneCredit, or with status 404 (Not Found)
     */
    @GetMapping("/ligne-credits/{id}")
    @Timed
    public ResponseEntity<LigneCredit> getLigneCredit(@PathVariable Long id) {
        log.debug("REST request to get LigneCredit : {}", id);
        Optional<LigneCredit> ligneCredit = ligneCreditRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ligneCredit);
    }

    /**
     * DELETE  /ligne-credits/:id : delete the "id" ligneCredit.
     *
     * @param id the id of the ligneCredit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ligne-credits/{id}")
    @Timed
    public ResponseEntity<Void> deleteLigneCredit(@PathVariable Long id) {
        log.debug("REST request to delete LigneCredit : {}", id);

        ligneCreditRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
