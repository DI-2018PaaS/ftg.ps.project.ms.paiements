package ftg.ps.project.ms.paiements.web.rest;

import ftg.ps.project.ms.paiements.McsPaiementsApp;

import ftg.ps.project.ms.paiements.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.paiements.domain.Pret;
import ftg.ps.project.ms.paiements.repository.PretRepository;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static ftg.ps.project.ms.paiements.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PretResource REST controller.
 *
 * @see PretResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, McsPaiementsApp.class})
public class PretResourceIntTest {

    private static final Long DEFAULT_NUM_PRET = 1L;
    private static final Long UPDATED_NUM_PRET = 2L;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MIS_AJOUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MIS_AJOUR = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_CAPITAL_RESTANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CAPITAL_RESTANT = new BigDecimal(2);

    private static final LocalDate DEFAULT_DATE_DERNIER_REMB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DERNIER_REMB = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ID_OWNER = 1L;
    private static final Long UPDATED_ID_OWNER = 2L;

    @Autowired
    private PretRepository pretRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPretMockMvc;

    private Pret pret;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PretResource pretResource = new PretResource(pretRepository);
        this.restPretMockMvc = MockMvcBuilders.standaloneSetup(pretResource)
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
    public static Pret createEntity(EntityManager em) {
        Pret pret = new Pret()
            .numPret(DEFAULT_NUM_PRET)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateMisAjour(DEFAULT_DATE_MIS_AJOUR)
            .capitalRestant(DEFAULT_CAPITAL_RESTANT)
            .dateDernierRemb(DEFAULT_DATE_DERNIER_REMB)
            .idOwner(DEFAULT_ID_OWNER);
        return pret;
    }

    @Before
    public void initTest() {
        pret = createEntity(em);
    }

    @Test
    @Transactional
    public void createPret() throws Exception {
        int databaseSizeBeforeCreate = pretRepository.findAll().size();

        // Create the Pret
        restPretMockMvc.perform(post("/api/prets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pret)))
            .andExpect(status().isCreated());

        // Validate the Pret in the database
        List<Pret> pretList = pretRepository.findAll();
        assertThat(pretList).hasSize(databaseSizeBeforeCreate + 1);
        Pret testPret = pretList.get(pretList.size() - 1);
        assertThat(testPret.getNumPret()).isEqualTo(DEFAULT_NUM_PRET);
        assertThat(testPret.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testPret.getDateMisAjour()).isEqualTo(DEFAULT_DATE_MIS_AJOUR);
        assertThat(testPret.getCapitalRestant()).isEqualTo(DEFAULT_CAPITAL_RESTANT);
        assertThat(testPret.getDateDernierRemb()).isEqualTo(DEFAULT_DATE_DERNIER_REMB);
        assertThat(testPret.getIdOwner()).isEqualTo(DEFAULT_ID_OWNER);
    }

    @Test
    @Transactional
    public void createPretWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pretRepository.findAll().size();

        // Create the Pret with an existing ID
        pret.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPretMockMvc.perform(post("/api/prets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pret)))
            .andExpect(status().isBadRequest());

        // Validate the Pret in the database
        List<Pret> pretList = pretRepository.findAll();
        assertThat(pretList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPrets() throws Exception {
        // Initialize the database
        pretRepository.saveAndFlush(pret);

        // Get all the pretList
        restPretMockMvc.perform(get("/api/prets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pret.getId().intValue())))
            .andExpect(jsonPath("$.[*].numPret").value(hasItem(DEFAULT_NUM_PRET.intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateMisAjour").value(hasItem(DEFAULT_DATE_MIS_AJOUR.toString())))
            .andExpect(jsonPath("$.[*].capitalRestant").value(hasItem(DEFAULT_CAPITAL_RESTANT.intValue())))
            .andExpect(jsonPath("$.[*].dateDernierRemb").value(hasItem(DEFAULT_DATE_DERNIER_REMB.toString())))
            .andExpect(jsonPath("$.[*].idOwner").value(hasItem(DEFAULT_ID_OWNER.intValue())));
    }
    

    @Test
    @Transactional
    public void getPret() throws Exception {
        // Initialize the database
        pretRepository.saveAndFlush(pret);

        // Get the pret
        restPretMockMvc.perform(get("/api/prets/{id}", pret.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pret.getId().intValue()))
            .andExpect(jsonPath("$.numPret").value(DEFAULT_NUM_PRET.intValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateMisAjour").value(DEFAULT_DATE_MIS_AJOUR.toString()))
            .andExpect(jsonPath("$.capitalRestant").value(DEFAULT_CAPITAL_RESTANT.intValue()))
            .andExpect(jsonPath("$.dateDernierRemb").value(DEFAULT_DATE_DERNIER_REMB.toString()))
            .andExpect(jsonPath("$.idOwner").value(DEFAULT_ID_OWNER.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPret() throws Exception {
        // Get the pret
        restPretMockMvc.perform(get("/api/prets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePret() throws Exception {
        // Initialize the database
        pretRepository.saveAndFlush(pret);

        int databaseSizeBeforeUpdate = pretRepository.findAll().size();

        // Update the pret
        Pret updatedPret = pretRepository.findById(pret.getId()).get();
        // Disconnect from session so that the updates on updatedPret are not directly saved in db
        em.detach(updatedPret);
        updatedPret
            .numPret(UPDATED_NUM_PRET)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateMisAjour(UPDATED_DATE_MIS_AJOUR)
            .capitalRestant(UPDATED_CAPITAL_RESTANT)
            .dateDernierRemb(UPDATED_DATE_DERNIER_REMB)
            .idOwner(UPDATED_ID_OWNER);

        restPretMockMvc.perform(put("/api/prets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPret)))
            .andExpect(status().isOk());

        // Validate the Pret in the database
        List<Pret> pretList = pretRepository.findAll();
        assertThat(pretList).hasSize(databaseSizeBeforeUpdate);
        Pret testPret = pretList.get(pretList.size() - 1);
        assertThat(testPret.getNumPret()).isEqualTo(UPDATED_NUM_PRET);
        assertThat(testPret.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testPret.getDateMisAjour()).isEqualTo(UPDATED_DATE_MIS_AJOUR);
        assertThat(testPret.getCapitalRestant()).isEqualTo(UPDATED_CAPITAL_RESTANT);
        assertThat(testPret.getDateDernierRemb()).isEqualTo(UPDATED_DATE_DERNIER_REMB);
        assertThat(testPret.getIdOwner()).isEqualTo(UPDATED_ID_OWNER);
    }

    @Test
    @Transactional
    public void updateNonExistingPret() throws Exception {
        int databaseSizeBeforeUpdate = pretRepository.findAll().size();

        // Create the Pret

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPretMockMvc.perform(put("/api/prets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pret)))
            .andExpect(status().isBadRequest());

        // Validate the Pret in the database
        List<Pret> pretList = pretRepository.findAll();
        assertThat(pretList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePret() throws Exception {
        // Initialize the database
        pretRepository.saveAndFlush(pret);

        int databaseSizeBeforeDelete = pretRepository.findAll().size();

        // Get the pret
        restPretMockMvc.perform(delete("/api/prets/{id}", pret.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pret> pretList = pretRepository.findAll();
        assertThat(pretList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pret.class);
        Pret pret1 = new Pret();
        pret1.setId(1L);
        Pret pret2 = new Pret();
        pret2.setId(pret1.getId());
        assertThat(pret1).isEqualTo(pret2);
        pret2.setId(2L);
        assertThat(pret1).isNotEqualTo(pret2);
        pret1.setId(null);
        assertThat(pret1).isNotEqualTo(pret2);
    }
}
