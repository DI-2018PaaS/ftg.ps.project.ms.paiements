package ftg.ps.project.ms.paiements.web.rest;

import ftg.ps.project.ms.paiements.McsPaiementsApp;

import ftg.ps.project.ms.paiements.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.paiements.domain.ReglementCommande;
import ftg.ps.project.ms.paiements.repository.ReglementCommandeRepository;
import ftg.ps.project.ms.paiements.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static ftg.ps.project.ms.paiements.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReglementCommandeResource REST controller.
 *
 * @see ReglementCommandeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, McsPaiementsApp.class})
public class ReglementCommandeResourceIntTest {

    private static final Long DEFAULT_ID_REGLEMENT = 1L;
    private static final Long UPDATED_ID_REGLEMENT = 2L;

    private static final LocalDate DEFAULT_DATE_REGLEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_REGLEMENT = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ID_BON_DE_COMMANDE = 1L;
    private static final Long UPDATED_ID_BON_DE_COMMANDE = 2L;

    @Autowired
    private ReglementCommandeRepository reglementCommandeRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReglementCommandeMockMvc;

    private ReglementCommande reglementCommande;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReglementCommandeResource reglementCommandeResource = new ReglementCommandeResource(reglementCommandeRepository);
        this.restReglementCommandeMockMvc = MockMvcBuilders.standaloneSetup(reglementCommandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReglementCommande createEntity(EntityManager em) {
        ReglementCommande reglementCommande = new ReglementCommande()
            .idReglement(DEFAULT_ID_REGLEMENT)
            .dateReglement(DEFAULT_DATE_REGLEMENT)
            .idBonDeCommande(DEFAULT_ID_BON_DE_COMMANDE);
        return reglementCommande;
    }

    @Before
    public void initTest() {
        reglementCommande = createEntity(em);
    }

    @Test
    @Transactional
    public void createReglementCommande() throws Exception {
        int databaseSizeBeforeCreate = reglementCommandeRepository.findAll().size();

        // Create the ReglementCommande
        restReglementCommandeMockMvc.perform(post("/api/reglement-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglementCommande)))
            .andExpect(status().isCreated());

        // Validate the ReglementCommande in the database
        List<ReglementCommande> reglementCommandeList = reglementCommandeRepository.findAll();
        assertThat(reglementCommandeList).hasSize(databaseSizeBeforeCreate + 1);
        ReglementCommande testReglementCommande = reglementCommandeList.get(reglementCommandeList.size() - 1);
        assertThat(testReglementCommande.getIdReglement()).isEqualTo(DEFAULT_ID_REGLEMENT);
        assertThat(testReglementCommande.getDateReglement()).isEqualTo(DEFAULT_DATE_REGLEMENT);
        assertThat(testReglementCommande.getIdBonDeCommande()).isEqualTo(DEFAULT_ID_BON_DE_COMMANDE);
    }

    @Test
    @Transactional
    public void createReglementCommandeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reglementCommandeRepository.findAll().size();

        // Create the ReglementCommande with an existing ID
        reglementCommande.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReglementCommandeMockMvc.perform(post("/api/reglement-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglementCommande)))
            .andExpect(status().isBadRequest());

        // Validate the ReglementCommande in the database
        List<ReglementCommande> reglementCommandeList = reglementCommandeRepository.findAll();
        assertThat(reglementCommandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReglementCommandes() throws Exception {
        // Initialize the database
        reglementCommandeRepository.saveAndFlush(reglementCommande);

        // Get all the reglementCommandeList
        restReglementCommandeMockMvc.perform(get("/api/reglement-commandes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reglementCommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].idReglement").value(hasItem(DEFAULT_ID_REGLEMENT.intValue())))
            .andExpect(jsonPath("$.[*].dateReglement").value(hasItem(DEFAULT_DATE_REGLEMENT.toString())))
            .andExpect(jsonPath("$.[*].idBonDeCommande").value(hasItem(DEFAULT_ID_BON_DE_COMMANDE.intValue())));
    }
    

    @Test
    @Transactional
    public void getReglementCommande() throws Exception {
        // Initialize the database
        reglementCommandeRepository.saveAndFlush(reglementCommande);

        // Get the reglementCommande
        restReglementCommandeMockMvc.perform(get("/api/reglement-commandes/{id}", reglementCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reglementCommande.getId().intValue()))
            .andExpect(jsonPath("$.idReglement").value(DEFAULT_ID_REGLEMENT.intValue()))
            .andExpect(jsonPath("$.dateReglement").value(DEFAULT_DATE_REGLEMENT.toString()))
            .andExpect(jsonPath("$.idBonDeCommande").value(DEFAULT_ID_BON_DE_COMMANDE.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingReglementCommande() throws Exception {
        // Get the reglementCommande
        restReglementCommandeMockMvc.perform(get("/api/reglement-commandes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReglementCommande() throws Exception {
        // Initialize the database
        reglementCommandeRepository.saveAndFlush(reglementCommande);

        int databaseSizeBeforeUpdate = reglementCommandeRepository.findAll().size();

        // Update the reglementCommande
        ReglementCommande updatedReglementCommande = reglementCommandeRepository.findById(reglementCommande.getId()).get();
        // Disconnect from session so that the updates on updatedReglementCommande are not directly saved in db
        em.detach(updatedReglementCommande);
        updatedReglementCommande
            .idReglement(UPDATED_ID_REGLEMENT)
            .dateReglement(UPDATED_DATE_REGLEMENT)
            .idBonDeCommande(UPDATED_ID_BON_DE_COMMANDE);

        restReglementCommandeMockMvc.perform(put("/api/reglement-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReglementCommande)))
            .andExpect(status().isOk());

        // Validate the ReglementCommande in the database
        List<ReglementCommande> reglementCommandeList = reglementCommandeRepository.findAll();
        assertThat(reglementCommandeList).hasSize(databaseSizeBeforeUpdate);
        ReglementCommande testReglementCommande = reglementCommandeList.get(reglementCommandeList.size() - 1);
        assertThat(testReglementCommande.getIdReglement()).isEqualTo(UPDATED_ID_REGLEMENT);
        assertThat(testReglementCommande.getDateReglement()).isEqualTo(UPDATED_DATE_REGLEMENT);
        assertThat(testReglementCommande.getIdBonDeCommande()).isEqualTo(UPDATED_ID_BON_DE_COMMANDE);
    }

    @Test
    @Transactional
    public void updateNonExistingReglementCommande() throws Exception {
        int databaseSizeBeforeUpdate = reglementCommandeRepository.findAll().size();

        // Create the ReglementCommande

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReglementCommandeMockMvc.perform(put("/api/reglement-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglementCommande)))
            .andExpect(status().isBadRequest());

        // Validate the ReglementCommande in the database
        List<ReglementCommande> reglementCommandeList = reglementCommandeRepository.findAll();
        assertThat(reglementCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReglementCommande() throws Exception {
        // Initialize the database
        reglementCommandeRepository.saveAndFlush(reglementCommande);

        int databaseSizeBeforeDelete = reglementCommandeRepository.findAll().size();

        // Get the reglementCommande
        restReglementCommandeMockMvc.perform(delete("/api/reglement-commandes/{id}", reglementCommande.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReglementCommande> reglementCommandeList = reglementCommandeRepository.findAll();
        assertThat(reglementCommandeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReglementCommande.class);
        ReglementCommande reglementCommande1 = new ReglementCommande();
        reglementCommande1.setId(1L);
        ReglementCommande reglementCommande2 = new ReglementCommande();
        reglementCommande2.setId(reglementCommande1.getId());
        assertThat(reglementCommande1).isEqualTo(reglementCommande2);
        reglementCommande2.setId(2L);
        assertThat(reglementCommande1).isNotEqualTo(reglementCommande2);
        reglementCommande1.setId(null);
        assertThat(reglementCommande1).isNotEqualTo(reglementCommande2);
    }
}
