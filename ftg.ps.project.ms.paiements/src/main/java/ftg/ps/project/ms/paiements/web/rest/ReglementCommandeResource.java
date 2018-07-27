package ftg.ps.project.ms.paiements.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftg.ps.project.ms.paiements.domain.ReglementCommande;
import ftg.ps.project.ms.paiements.repository.ReglementCommandeRepository;
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
 * REST controller for managing ReglementCommande.
 */
@RestController
@RequestMapping("/api")
public class ReglementCommandeResource {

    private final Logger log = LoggerFactory.getLogger(ReglementCommandeResource.class);

    private static final String ENTITY_NAME = "reglementCommande";

    private final ReglementCommandeRepository reglementCommandeRepository;

    public ReglementCommandeResource(ReglementCommandeRepository reglementCommandeRepository) {
        this.reglementCommandeRepository = reglementCommandeRepository;
    }

    /**
     * POST  /reglement-commandes : Create a new reglementCommande.
     *
     * @param reglementCommande the reglementCommande to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reglementCommande, or with status 400 (Bad Request) if the reglementCommande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reglement-commandes")
    @Timed
    public ResponseEntity<ReglementCommande> createReglementCommande(@RequestBody ReglementCommande reglementCommande) throws URISyntaxException {
        log.debug("REST request to save ReglementCommande : {}", reglementCommande);
        if (reglementCommande.getId() != null) {
            throw new BadRequestAlertException("A new reglementCommande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReglementCommande result = reglementCommandeRepository.save(reglementCommande);
        return ResponseEntity.created(new URI("/api/reglement-commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reglement-commandes : Updates an existing reglementCommande.
     *
     * @param reglementCommande the reglementCommande to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reglementCommande,
     * or with status 400 (Bad Request) if the reglementCommande is not valid,
     * or with status 500 (Internal Server Error) if the reglementCommande couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reglement-commandes")
    @Timed
    public ResponseEntity<ReglementCommande> updateReglementCommande(@RequestBody ReglementCommande reglementCommande) throws URISyntaxException {
        log.debug("REST request to update ReglementCommande : {}", reglementCommande);
        if (reglementCommande.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReglementCommande result = reglementCommandeRepository.save(reglementCommande);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reglementCommande.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reglement-commandes : get all the reglementCommandes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reglementCommandes in body
     */
    @GetMapping("/reglement-commandes")
    @Timed
    public List<ReglementCommande> getAllReglementCommandes() {
        log.debug("REST request to get all ReglementCommandes");
        return reglementCommandeRepository.findAll();
    }

    /**
     * GET  /reglement-commandes/:id : get the "id" reglementCommande.
     *
     * @param id the id of the reglementCommande to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reglementCommande, or with status 404 (Not Found)
     */
    @GetMapping("/reglement-commandes/{id}")
    @Timed
    public ResponseEntity<ReglementCommande> getReglementCommande(@PathVariable Long id) {
        log.debug("REST request to get ReglementCommande : {}", id);
        Optional<ReglementCommande> reglementCommande = reglementCommandeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reglementCommande);
    }

    /**
     * DELETE  /reglement-commandes/:id : delete the "id" reglementCommande.
     *
     * @param id the id of the reglementCommande to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reglement-commandes/{id}")
    @Timed
    public ResponseEntity<Void> deleteReglementCommande(@PathVariable Long id) {
        log.debug("REST request to delete ReglementCommande : {}", id);

        reglementCommandeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
