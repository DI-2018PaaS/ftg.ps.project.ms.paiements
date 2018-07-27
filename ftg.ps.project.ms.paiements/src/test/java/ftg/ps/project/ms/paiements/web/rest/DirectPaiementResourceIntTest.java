package ftg.ps.project.ms.paiements.web.rest;

import ftg.ps.project.ms.paiements.McsPaiementsApp;

import ftg.ps.project.ms.paiements.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.paiements.domain.DirectPaiement;
import ftg.ps.project.ms.paiements.repository.DirectPaiementRepository;
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
import java.util.List;


import static ftg.ps.project.ms.paiements.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DirectPaiementResource REST controller.
 *
 * @see DirectPaiementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, McsPaiementsApp.class})
public class DirectPaiementResourceIntTest {

    private static final Long DEFAULT_ID_MODE_PAIEMENT = 1L;
    private static final Long UPDATED_ID_MODE_PAIEMENT = 2L;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DirectPaiementRepository directPaiementRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDirectPaiementMockMvc;

    private DirectPaiement directPaiement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DirectPaiementResource directPaiementResource = new DirectPaiementResource(directPaiementRepository);
        this.restDirectPaiementMockMvc = MockMvcBuilders.standaloneSetup(directPaiementResource)
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
    public static DirectPaiement createEntity(EntityManager em) {
        DirectPaiement directPaiement = new DirectPaiement()
            .idModePaiement(DEFAULT_ID_MODE_PAIEMENT)
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION);
        return directPaiement;
    }

    @Before
    public void initTest() {
        directPaiement = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirectPaiement() throws Exception {
        int databaseSizeBeforeCreate = directPaiementRepository.findAll().size();

        // Create the DirectPaiement
        restDirectPaiementMockMvc.perform(post("/api/direct-paiements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directPaiement)))
            .andExpect(status().isCreated());

        // Validate the DirectPaiement in the database
        List<DirectPaiement> directPaiementList = directPaiementRepository.findAll();
        assertThat(directPaiementList).hasSize(databaseSizeBeforeCreate + 1);
        DirectPaiement testDirectPaiement = directPaiementList.get(directPaiementList.size() - 1);
        assertThat(testDirectPaiement.getIdModePaiement()).isEqualTo(DEFAULT_ID_MODE_PAIEMENT);
        assertThat(testDirectPaiement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDirectPaiement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDirectPaiementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = directPaiementRepository.findAll().size();

        // Create the DirectPaiement with an existing ID
        directPaiement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectPaiementMockMvc.perform(post("/api/direct-paiements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directPaiement)))
            .andExpect(status().isBadRequest());

        // Validate the DirectPaiement in the database
        List<DirectPaiement> directPaiementList = directPaiementRepository.findAll();
        assertThat(directPaiementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDirectPaiements() throws Exception {
        // Initialize the database
        directPaiementRepository.saveAndFlush(directPaiement);

        // Get all the directPaiementList
        restDirectPaiementMockMvc.perform(get("/api/direct-paiements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directPaiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].idModePaiement").value(hasItem(DEFAULT_ID_MODE_PAIEMENT.intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    

    @Test
    @Transactional
    public void getDirectPaiement() throws Exception {
        // Initialize the database
        directPaiementRepository.saveAndFlush(directPaiement);

        // Get the directPaiement
        restDirectPaiementMockMvc.perform(get("/api/direct-paiements/{id}", directPaiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(directPaiement.getId().intValue()))
            .andExpect(jsonPath("$.idModePaiement").value(DEFAULT_ID_MODE_PAIEMENT.intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingDirectPaiement() throws Exception {
        // Get the directPaiement
        restDirectPaiementMockMvc.perform(get("/api/direct-paiements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirectPaiement() throws Exception {
        // Initialize the database
        directPaiementRepository.saveAndFlush(directPaiement);

        int databaseSizeBeforeUpdate = directPaiementRepository.findAll().size();

        // Update the directPaiement
        DirectPaiement updatedDirectPaiement = directPaiementRepository.findById(directPaiement.getId()).get();
        // Disconnect from session so that the updates on updatedDirectPaiement are not directly saved in db
        em.detach(updatedDirectPaiement);
        updatedDirectPaiement
            .idModePaiement(UPDATED_ID_MODE_PAIEMENT)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);

        restDirectPaiementMockMvc.perform(put("/api/direct-paiements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDirectPaiement)))
            .andExpect(status().isOk());

        // Validate the DirectPaiement in the database
        List<DirectPaiement> directPaiementList = directPaiementRepository.findAll();
        assertThat(directPaiementList).hasSize(databaseSizeBeforeUpdate);
        DirectPaiement testDirectPaiement = directPaiementList.get(directPaiementList.size() - 1);
        assertThat(testDirectPaiement.getIdModePaiement()).isEqualTo(UPDATED_ID_MODE_PAIEMENT);
        assertThat(testDirectPaiement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDirectPaiement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDirectPaiement() throws Exception {
        int databaseSizeBeforeUpdate = directPaiementRepository.findAll().size();

        // Create the DirectPaiement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDirectPaiementMockMvc.perform(put("/api/direct-paiements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directPaiement)))
            .andExpect(status().isBadRequest());

        // Validate the DirectPaiement in the database
        List<DirectPaiement> directPaiementList = directPaiementRepository.findAll();
        assertThat(directPaiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDirectPaiement() throws Exception {
        // Initialize the database
        directPaiementRepository.saveAndFlush(directPaiement);

        int databaseSizeBeforeDelete = directPaiementRepository.findAll().size();

        // Get the directPaiement
        restDirectPaiementMockMvc.perform(delete("/api/direct-paiements/{id}", directPaiement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DirectPaiement> directPaiementList = directPaiementRepository.findAll();
        assertThat(directPaiementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DirectPaiement.class);
        DirectPaiement directPaiement1 = new DirectPaiement();
        directPaiement1.setId(1L);
        DirectPaiement directPaiement2 = new DirectPaiement();
        directPaiement2.setId(directPaiement1.getId());
        assertThat(directPaiement1).isEqualTo(directPaiement2);
        directPaiement2.setId(2L);
        assertThat(directPaiement1).isNotEqualTo(directPaiement2);
        directPaiement1.setId(null);
        assertThat(directPaiement1).isNotEqualTo(directPaiement2);
    }
}
