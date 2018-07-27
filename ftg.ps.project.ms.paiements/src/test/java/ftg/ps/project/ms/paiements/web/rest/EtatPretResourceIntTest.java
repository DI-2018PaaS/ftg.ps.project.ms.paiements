package ftg.ps.project.ms.paiements.web.rest;

import ftg.ps.project.ms.paiements.McsPaiementsApp;

import ftg.ps.project.ms.paiements.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.paiements.domain.EtatPret;
import ftg.ps.project.ms.paiements.repository.EtatPretRepository;
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
 * Test class for the EtatPretResource REST controller.
 *
 * @see EtatPretResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, McsPaiementsApp.class})
public class EtatPretResourceIntTest {

    private static final Long DEFAULT_ID_ETAT = 1L;
    private static final Long UPDATED_ID_ETAT = 2L;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private EtatPretRepository etatPretRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEtatPretMockMvc;

    private EtatPret etatPret;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EtatPretResource etatPretResource = new EtatPretResource(etatPretRepository);
        this.restEtatPretMockMvc = MockMvcBuilders.standaloneSetup(etatPretResource)
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
    public static EtatPret createEntity(EntityManager em) {
        EtatPret etatPret = new EtatPret()
            .idEtat(DEFAULT_ID_ETAT)
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION);
        return etatPret;
    }

    @Before
    public void initTest() {
        etatPret = createEntity(em);
    }

    @Test
    @Transactional
    public void createEtatPret() throws Exception {
        int databaseSizeBeforeCreate = etatPretRepository.findAll().size();

        // Create the EtatPret
        restEtatPretMockMvc.perform(post("/api/etat-prets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etatPret)))
            .andExpect(status().isCreated());

        // Validate the EtatPret in the database
        List<EtatPret> etatPretList = etatPretRepository.findAll();
        assertThat(etatPretList).hasSize(databaseSizeBeforeCreate + 1);
        EtatPret testEtatPret = etatPretList.get(etatPretList.size() - 1);
        assertThat(testEtatPret.getIdEtat()).isEqualTo(DEFAULT_ID_ETAT);
        assertThat(testEtatPret.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testEtatPret.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createEtatPretWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = etatPretRepository.findAll().size();

        // Create the EtatPret with an existing ID
        etatPret.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtatPretMockMvc.perform(post("/api/etat-prets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etatPret)))
            .andExpect(status().isBadRequest());

        // Validate the EtatPret in the database
        List<EtatPret> etatPretList = etatPretRepository.findAll();
        assertThat(etatPretList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEtatPrets() throws Exception {
        // Initialize the database
        etatPretRepository.saveAndFlush(etatPret);

        // Get all the etatPretList
        restEtatPretMockMvc.perform(get("/api/etat-prets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etatPret.getId().intValue())))
            .andExpect(jsonPath("$.[*].idEtat").value(hasItem(DEFAULT_ID_ETAT.intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    

    @Test
    @Transactional
    public void getEtatPret() throws Exception {
        // Initialize the database
        etatPretRepository.saveAndFlush(etatPret);

        // Get the etatPret
        restEtatPretMockMvc.perform(get("/api/etat-prets/{id}", etatPret.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(etatPret.getId().intValue()))
            .andExpect(jsonPath("$.idEtat").value(DEFAULT_ID_ETAT.intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingEtatPret() throws Exception {
        // Get the etatPret
        restEtatPretMockMvc.perform(get("/api/etat-prets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtatPret() throws Exception {
        // Initialize the database
        etatPretRepository.saveAndFlush(etatPret);

        int databaseSizeBeforeUpdate = etatPretRepository.findAll().size();

        // Update the etatPret
        EtatPret updatedEtatPret = etatPretRepository.findById(etatPret.getId()).get();
        // Disconnect from session so that the updates on updatedEtatPret are not directly saved in db
        em.detach(updatedEtatPret);
        updatedEtatPret
            .idEtat(UPDATED_ID_ETAT)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);

        restEtatPretMockMvc.perform(put("/api/etat-prets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEtatPret)))
            .andExpect(status().isOk());

        // Validate the EtatPret in the database
        List<EtatPret> etatPretList = etatPretRepository.findAll();
        assertThat(etatPretList).hasSize(databaseSizeBeforeUpdate);
        EtatPret testEtatPret = etatPretList.get(etatPretList.size() - 1);
        assertThat(testEtatPret.getIdEtat()).isEqualTo(UPDATED_ID_ETAT);
        assertThat(testEtatPret.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testEtatPret.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingEtatPret() throws Exception {
        int databaseSizeBeforeUpdate = etatPretRepository.findAll().size();

        // Create the EtatPret

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEtatPretMockMvc.perform(put("/api/etat-prets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etatPret)))
            .andExpect(status().isBadRequest());

        // Validate the EtatPret in the database
        List<EtatPret> etatPretList = etatPretRepository.findAll();
        assertThat(etatPretList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEtatPret() throws Exception {
        // Initialize the database
        etatPretRepository.saveAndFlush(etatPret);

        int databaseSizeBeforeDelete = etatPretRepository.findAll().size();

        // Get the etatPret
        restEtatPretMockMvc.perform(delete("/api/etat-prets/{id}", etatPret.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EtatPret> etatPretList = etatPretRepository.findAll();
        assertThat(etatPretList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EtatPret.class);
        EtatPret etatPret1 = new EtatPret();
        etatPret1.setId(1L);
        EtatPret etatPret2 = new EtatPret();
        etatPret2.setId(etatPret1.getId());
        assertThat(etatPret1).isEqualTo(etatPret2);
        etatPret2.setId(2L);
        assertThat(etatPret1).isNotEqualTo(etatPret2);
        etatPret1.setId(null);
        assertThat(etatPret1).isNotEqualTo(etatPret2);
    }
}
