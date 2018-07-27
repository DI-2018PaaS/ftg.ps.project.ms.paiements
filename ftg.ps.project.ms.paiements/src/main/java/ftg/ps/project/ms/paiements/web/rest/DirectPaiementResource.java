package ftg.ps.project.ms.paiements.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftg.ps.project.ms.paiements.domain.DirectPaiement;
import ftg.ps.project.ms.paiements.repository.DirectPaiementRepository;
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
 * REST controller for managing DirectPaiement.
 */
@RestController
@RequestMapping("/api")
public class DirectPaiementResource {

    private final Logger log = LoggerFactory.getLogger(DirectPaiementResource.class);

    private static final String ENTITY_NAME = "directPaiement";

    private final DirectPaiementRepository directPaiementRepository;

    public DirectPaiementResource(DirectPaiementRepository directPaiementRepository) {
        this.directPaiementRepository = directPaiementRepository;
    }

    /**
     * POST  /direct-paiements : Create a new directPaiement.
     *
     * @param directPaiement the directPaiement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new directPaiement, or with status 400 (Bad Request) if the directPaiement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/direct-paiements")
    @Timed
    public ResponseEntity<DirectPaiement> createDirectPaiement(@RequestBody DirectPaiement directPaiement) throws URISyntaxException {
        log.debug("REST request to save DirectPaiement : {}", directPaiement);
        if (directPaiement.getId() != null) {
            throw new BadRequestAlertException("A new directPaiement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DirectPaiement result = directPaiementRepository.save(directPaiement);
        return ResponseEntity.created(new URI("/api/direct-paiements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /direct-paiements : Updates an existing directPaiement.
     *
     * @param directPaiement the directPaiement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated directPaiement,
     * or with status 400 (Bad Request) if the directPaiement is not valid,
     * or with status 500 (Internal Server Error) if the directPaiement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/direct-paiements")
    @Timed
    public ResponseEntity<DirectPaiement> updateDirectPaiement(@RequestBody DirectPaiement directPaiement) throws URISyntaxException {
        log.debug("REST request to update DirectPaiement : {}", directPaiement);
        if (directPaiement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DirectPaiement result = directPaiementRepository.save(directPaiement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, directPaiement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /direct-paiements : get all the directPaiements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of directPaiements in body
     */
    @GetMapping("/direct-paiements")
    @Timed
    public List<DirectPaiement> getAllDirectPaiements() {
        log.debug("REST request to get all DirectPaiements");
        return directPaiementRepository.findAll();
    }

    /**
     * GET  /direct-paiements/:id : get the "id" directPaiement.
     *
     * @param id the id of the directPaiement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the directPaiement, or with status 404 (Not Found)
     */
    @GetMapping("/direct-paiements/{id}")
    @Timed
    public ResponseEntity<DirectPaiement> getDirectPaiement(@PathVariable Long id) {
        log.debug("REST request to get DirectPaiement : {}", id);
        Optional<DirectPaiement> directPaiement = directPaiementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(directPaiement);
    }

    /**
     * DELETE  /direct-paiements/:id : delete the "id" directPaiement.
     *
     * @param id the id of the directPaiement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/direct-paiements/{id}")
    @Timed
    public ResponseEntity<Void> deleteDirectPaiement(@PathVariable Long id) {
        log.debug("REST request to delete DirectPaiement : {}", id);

        directPaiementRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
