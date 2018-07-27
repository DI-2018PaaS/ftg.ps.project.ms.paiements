package ftg.ps.project.ms.paiements.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftg.ps.project.ms.paiements.domain.RemboursementCredit;
import ftg.ps.project.ms.paiements.repository.RemboursementCreditRepository;
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
 * REST controller for managing RemboursementCredit.
 */
@RestController
@RequestMapping("/api")
public class RemboursementCreditResource {

    private final Logger log = LoggerFactory.getLogger(RemboursementCreditResource.class);

    private static final String ENTITY_NAME = "remboursementCredit";

    private final RemboursementCreditRepository remboursementCreditRepository;

    public RemboursementCreditResource(RemboursementCreditRepository remboursementCreditRepository) {
        this.remboursementCreditRepository = remboursementCreditRepository;
    }

    /**
     * POST  /remboursement-credits : Create a new remboursementCredit.
     *
     * @param remboursementCredit the remboursementCredit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new remboursementCredit, or with status 400 (Bad Request) if the remboursementCredit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/remboursement-credits")
    @Timed
    public ResponseEntity<RemboursementCredit> createRemboursementCredit(@RequestBody RemboursementCredit remboursementCredit) throws URISyntaxException {
        log.debug("REST request to save RemboursementCredit : {}", remboursementCredit);
        if (remboursementCredit.getId() != null) {
            throw new BadRequestAlertException("A new remboursementCredit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RemboursementCredit result = remboursementCreditRepository.save(remboursementCredit);
        return ResponseEntity.created(new URI("/api/remboursement-credits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /remboursement-credits : Updates an existing remboursementCredit.
     *
     * @param remboursementCredit the remboursementCredit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated remboursementCredit,
     * or with status 400 (Bad Request) if the remboursementCredit is not valid,
     * or with status 500 (Internal Server Error) if the remboursementCredit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/remboursement-credits")
    @Timed
    public ResponseEntity<RemboursementCredit> updateRemboursementCredit(@RequestBody RemboursementCredit remboursementCredit) throws URISyntaxException {
        log.debug("REST request to update RemboursementCredit : {}", remboursementCredit);
        if (remboursementCredit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RemboursementCredit result = remboursementCreditRepository.save(remboursementCredit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, remboursementCredit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /remboursement-credits : get all the remboursementCredits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of remboursementCredits in body
     */
    @GetMapping("/remboursement-credits")
    @Timed
    public List<RemboursementCredit> getAllRemboursementCredits() {
        log.debug("REST request to get all RemboursementCredits");
        return remboursementCreditRepository.findAll();
    }

    /**
     * GET  /remboursement-credits/:id : get the "id" remboursementCredit.
     *
     * @param id the id of the remboursementCredit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the remboursementCredit, or with status 404 (Not Found)
     */
    @GetMapping("/remboursement-credits/{id}")
    @Timed
    public ResponseEntity<RemboursementCredit> getRemboursementCredit(@PathVariable Long id) {
        log.debug("REST request to get RemboursementCredit : {}", id);
        Optional<RemboursementCredit> remboursementCredit = remboursementCreditRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(remboursementCredit);
    }

    /**
     * DELETE  /remboursement-credits/:id : delete the "id" remboursementCredit.
     *
     * @param id the id of the remboursementCredit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/remboursement-credits/{id}")
    @Timed
    public ResponseEntity<Void> deleteRemboursementCredit(@PathVariable Long id) {
        log.debug("REST request to delete RemboursementCredit : {}", id);

        remboursementCreditRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
