package ftg.ps.project.ms.paiements.web.rest;

import ftg.ps.project.ms.paiements.McsPaiementsApp;

import ftg.ps.project.ms.paiements.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.paiements.domain.LigneCredit;
import ftg.ps.project.ms.paiements.repository.LigneCreditRepository;
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
 * Test class for the LigneCreditResource REST controller.
 *
 * @see LigneCreditResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, McsPaiementsApp.class})
public class LigneCreditResourceIntTest {

    private static final Long DEFAULT_ID_LIGNE_CREDIT = 1L;
    private static final Long UPDATED_ID_LIGNE_CREDIT = 2L;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MIS_AJOUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MIS_AJOUR = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(2);

    private static final Long DEFAULT_ID_OWNER = 1L;
    private static final Long UPDATED_ID_OWNER = 2L;

    private static final String DEFAULT_NOM_FINANCIER = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FINANCIER = "BBBBBBBBBB";

    @Autowired
    private LigneCreditRepository ligneCreditRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLigneCreditMockMvc;

    private LigneCredit ligneCredit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LigneCreditResource ligneCreditResource = new LigneCreditResource(ligneCreditRepository);
        this.restLigneCreditMockMvc = MockMvcBuilders.standaloneSetup(ligneCreditResource)
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
    public static LigneCredit createEntity(EntityManager em) {
        LigneCredit ligneCredit = new LigneCredit()
            .idLigneCredit(DEFAULT_ID_LIGNE_CREDIT)
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateMisAjour(DEFAULT_DATE_MIS_AJOUR)
            .montant(DEFAULT_MONTANT)
            .idOwner(DEFAULT_ID_OWNER)
            .nomFinancier(DEFAULT_NOM_FINANCIER);
        return ligneCredit;
    }

    @Before
    public void initTest() {
        ligneCredit = createEntity(em);
    }

    @Test
    @Transactional
    public void createLigneCredit() throws Exception {
        int databaseSizeBeforeCreate = ligneCreditRepository.findAll().size();

        // Create the LigneCredit
        restLigneCreditMockMvc.perform(post("/api/ligne-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneCredit)))
            .andExpect(status().isCreated());

        // Validate the LigneCredit in the database
        List<LigneCredit> ligneCreditList = ligneCreditRepository.findAll();
        assertThat(ligneCreditList).hasSize(databaseSizeBeforeCreate + 1);
        LigneCredit testLigneCredit = ligneCreditList.get(ligneCreditList.size() - 1);
        assertThat(testLigneCredit.getIdLigneCredit()).isEqualTo(DEFAULT_ID_LIGNE_CREDIT);
        assertThat(testLigneCredit.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testLigneCredit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLigneCredit.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testLigneCredit.getDateMisAjour()).isEqualTo(DEFAULT_DATE_MIS_AJOUR);
        assertThat(testLigneCredit.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testLigneCredit.getIdOwner()).isEqualTo(DEFAULT_ID_OWNER);
        assertThat(testLigneCredit.getNomFinancier()).isEqualTo(DEFAULT_NOM_FINANCIER);
    }

    @Test
    @Transactional
    public void createLigneCreditWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ligneCreditRepository.findAll().size();

        // Create the LigneCredit with an existing ID
        ligneCredit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneCreditMockMvc.perform(post("/api/ligne-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneCredit)))
            .andExpect(status().isBadRequest());

        // Validate the LigneCredit in the database
        List<LigneCredit> ligneCreditList = ligneCreditRepository.findAll();
        assertThat(ligneCreditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLigneCredits() throws Exception {
        // Initialize the database
        ligneCreditRepository.saveAndFlush(ligneCredit);

        // Get all the ligneCreditList
        restLigneCreditMockMvc.perform(get("/api/ligne-credits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneCredit.getId().intValue())))
            .andExpect(jsonPath("$.[*].idLigneCredit").value(hasItem(DEFAULT_ID_LIGNE_CREDIT.intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateMisAjour").value(hasItem(DEFAULT_DATE_MIS_AJOUR.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].idOwner").value(hasItem(DEFAULT_ID_OWNER.intValue())))
            .andExpect(jsonPath("$.[*].nomFinancier").value(hasItem(DEFAULT_NOM_FINANCIER.toString())));
    }
    

    @Test
    @Transactional
    public void getLigneCredit() throws Exception {
        // Initialize the database
        ligneCreditRepository.saveAndFlush(ligneCredit);

        // Get the ligneCredit
        restLigneCreditMockMvc.perform(get("/api/ligne-credits/{id}", ligneCredit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ligneCredit.getId().intValue()))
            .andExpect(jsonPath("$.idLigneCredit").value(DEFAULT_ID_LIGNE_CREDIT.intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateMisAjour").value(DEFAULT_DATE_MIS_AJOUR.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.idOwner").value(DEFAULT_ID_OWNER.intValue()))
            .andExpect(jsonPath("$.nomFinancier").value(DEFAULT_NOM_FINANCIER.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingLigneCredit() throws Exception {
        // Get the ligneCredit
        restLigneCreditMockMvc.perform(get("/api/ligne-credits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneCredit() throws Exception {
        // Initialize the database
        ligneCreditRepository.saveAndFlush(ligneCredit);

        int databaseSizeBeforeUpdate = ligneCreditRepository.findAll().size();

        // Update the ligneCredit
        LigneCredit updatedLigneCredit = ligneCreditRepository.findById(ligneCredit.getId()).get();
        // Disconnect from session so that the updates on updatedLigneCredit are not directly saved in db
        em.detach(updatedLigneCredit);
        updatedLigneCredit
            .idLigneCredit(UPDATED_ID_LIGNE_CREDIT)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateMisAjour(UPDATED_DATE_MIS_AJOUR)
            .montant(UPDATED_MONTANT)
            .idOwner(UPDATED_ID_OWNER)
            .nomFinancier(UPDATED_NOM_FINANCIER);

        restLigneCreditMockMvc.perform(put("/api/ligne-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLigneCredit)))
            .andExpect(status().isOk());

        // Validate the LigneCredit in the database
        List<LigneCredit> ligneCreditList = ligneCreditRepository.findAll();
        assertThat(ligneCreditList).hasSize(databaseSizeBeforeUpdate);
        LigneCredit testLigneCredit = ligneCreditList.get(ligneCreditList.size() - 1);
        assertThat(testLigneCredit.getIdLigneCredit()).isEqualTo(UPDATED_ID_LIGNE_CREDIT);
        assertThat(testLigneCredit.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testLigneCredit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLigneCredit.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testLigneCredit.getDateMisAjour()).isEqualTo(UPDATED_DATE_MIS_AJOUR);
        assertThat(testLigneCredit.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testLigneCredit.getIdOwner()).isEqualTo(UPDATED_ID_OWNER);
        assertThat(testLigneCredit.getNomFinancier()).isEqualTo(UPDATED_NOM_FINANCIER);
    }

    @Test
    @Transactional
    public void updateNonExistingLigneCredit() throws Exception {
        int databaseSizeBeforeUpdate = ligneCreditRepository.findAll().size();

        // Create the LigneCredit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLigneCreditMockMvc.perform(put("/api/ligne-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneCredit)))
            .andExpect(status().isBadRequest());

        // Validate the LigneCredit in the database
        List<LigneCredit> ligneCreditList = ligneCreditRepository.findAll();
        assertThat(ligneCreditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLigneCredit() throws Exception {
        // Initialize the database
        ligneCreditRepository.saveAndFlush(ligneCredit);

        int databaseSizeBeforeDelete = ligneCreditRepository.findAll().size();

        // Get the ligneCredit
        restLigneCreditMockMvc.perform(delete("/api/ligne-credits/{id}", ligneCredit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LigneCredit> ligneCreditList = ligneCreditRepository.findAll();
        assertThat(ligneCreditList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneCredit.class);
        LigneCredit ligneCredit1 = new LigneCredit();
        ligneCredit1.setId(1L);
        LigneCredit ligneCredit2 = new LigneCredit();
        ligneCredit2.setId(ligneCredit1.getId());
        assertThat(ligneCredit1).isEqualTo(ligneCredit2);
        ligneCredit2.setId(2L);
        assertThat(ligneCredit1).isNotEqualTo(ligneCredit2);
        ligneCredit1.setId(null);
        assertThat(ligneCredit1).isNotEqualTo(ligneCredit2);
    }
}
