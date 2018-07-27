package ftg.ps.project.ms.paiements.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftg.ps.project.ms.paiements.domain.Pret;
import ftg.ps.project.ms.paiements.repository.PretRepository;
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
 * REST controller for managing Pret.
 */
@RestController
@RequestMapping("/api")
public class PretResource {

    private final Logger log = LoggerFactory.getLogger(PretResource.class);

    private static final String ENTITY_NAME = "pret";

    private final PretRepository pretRepository;

    public PretResource(PretRepository pretRepository) {
        this.pretRepository = pretRepository;
    }

    /**
     * POST  /prets : Create a new pret.
     *
     * @param pret the pret to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pret, or with status 400 (Bad Request) if the pret has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prets")
    @Timed
    public ResponseEntity<Pret> createPret(@RequestBody Pret pret) throws URISyntaxException {
        log.debug("REST request to save Pret : {}", pret);
        if (pret.getId() != null) {
            throw new BadRequestAlertException("A new pret cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pret result = pretRepository.save(pret);
        return ResponseEntity.created(new URI("/api/prets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prets : Updates an existing pret.
     *
     * @param pret the pret to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pret,
     * or with status 400 (Bad Request) if the pret is not valid,
     * or with status 500 (Internal Server Error) if the pret couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prets")
    @Timed
    public ResponseEntity<Pret> updatePret(@RequestBody Pret pret) throws URISyntaxException {
        log.debug("REST request to update Pret : {}", pret);
        if (pret.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Pret result = pretRepository.save(pret);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pret.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prets : get all the prets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of prets in body
     */
    @GetMapping("/prets")
    @Timed
    public List<Pret> getAllPrets() {
        log.debug("REST request to get all Prets");
        return pretRepository.findAll();
    }

    /**
     * GET  /prets/:id : get the "id" pret.
     *
     * @param id the id of the pret to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pret, or with status 404 (Not Found)
     */
    @GetMapping("/prets/{id}")
    @Timed
    public ResponseEntity<Pret> getPret(@PathVariable Long id) {
        log.debug("REST request to get Pret : {}", id);
        Optional<Pret> pret = pretRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pret);
    }

    /**
     * DELETE  /prets/:id : delete the "id" pret.
     *
     * @param id the id of the pret to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prets/{id}")
    @Timed
    public ResponseEntity<Void> deletePret(@PathVariable Long id) {
        log.debug("REST request to delete Pret : {}", id);

        pretRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
