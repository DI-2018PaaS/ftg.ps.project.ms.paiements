package ftg.ps.project.ms.paiements.web.rest;

import ftg.ps.project.ms.paiements.McsPaiementsApp;

import ftg.ps.project.ms.paiements.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.paiements.domain.RemboursementCredit;
import ftg.ps.project.ms.paiements.repository.RemboursementCreditRepository;
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
 * Test class for the RemboursementCreditResource REST controller.
 *
 * @see RemboursementCreditResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, McsPaiementsApp.class})
public class RemboursementCreditResourceIntTest {

    private static final Long DEFAULT_ID_REMBOURSEMENT = 1L;
    private static final Long UPDATED_ID_REMBOURSEMENT = 2L;

    private static final LocalDate DEFAULT_DATE_REMBOURSEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_REMBOURSEMENT = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ID_OWNER = 1L;
    private static final Long UPDATED_ID_OWNER = 2L;

    @Autowired
    private RemboursementCreditRepository remboursementCreditRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRemboursementCreditMockMvc;

    private RemboursementCredit remboursementCredit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RemboursementCreditResource remboursementCreditResource = new RemboursementCreditResource(remboursementCreditRepository);
        this.restRemboursementCreditMockMvc = MockMvcBuilders.standaloneSetup(remboursementCreditResource)
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
    public static RemboursementCredit createEntity(EntityManager em) {
        RemboursementCredit remboursementCredit = new RemboursementCredit()
            .idRemboursement(DEFAULT_ID_REMBOURSEMENT)
            .dateRemboursement(DEFAULT_DATE_REMBOURSEMENT)
            .idOwner(DEFAULT_ID_OWNER);
        return remboursementCredit;
    }

    @Before
    public void initTest() {
        remboursementCredit = createEntity(em);
    }

    @Test
    @Transactional
    public void createRemboursementCredit() throws Exception {
        int databaseSizeBeforeCreate = remboursementCreditRepository.findAll().size();

        // Create the RemboursementCredit
        restRemboursementCreditMockMvc.perform(post("/api/remboursement-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remboursementCredit)))
            .andExpect(status().isCreated());

        // Validate the RemboursementCredit in the database
        List<RemboursementCredit> remboursementCreditList = remboursementCreditRepository.findAll();
        assertThat(remboursementCreditList).hasSize(databaseSizeBeforeCreate + 1);
        RemboursementCredit testRemboursementCredit = remboursementCreditList.get(remboursementCreditList.size() - 1);
        assertThat(testRemboursementCredit.getIdRemboursement()).isEqualTo(DEFAULT_ID_REMBOURSEMENT);
        assertThat(testRemboursementCredit.getDateRemboursement()).isEqualTo(DEFAULT_DATE_REMBOURSEMENT);
        assertThat(testRemboursementCredit.getIdOwner()).isEqualTo(DEFAULT_ID_OWNER);
    }

    @Test
    @Transactional
    public void createRemboursementCreditWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = remboursementCreditRepository.findAll().size();

        // Create the RemboursementCredit with an existing ID
        remboursementCredit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRemboursementCreditMockMvc.perform(post("/api/remboursement-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remboursementCredit)))
            .andExpect(status().isBadRequest());

        // Validate the RemboursementCredit in the database
        List<RemboursementCredit> remboursementCreditList = remboursementCreditRepository.findAll();
        assertThat(remboursementCreditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRemboursementCredits() throws Exception {
        // Initialize the database
        remboursementCreditRepository.saveAndFlush(remboursementCredit);

        // Get all the remboursementCreditList
        restRemboursementCreditMockMvc.perform(get("/api/remboursement-credits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(remboursementCredit.getId().intValue())))
            .andExpect(jsonPath("$.[*].idRemboursement").value(hasItem(DEFAULT_ID_REMBOURSEMENT.intValue())))
            .andExpect(jsonPath("$.[*].dateRemboursement").value(hasItem(DEFAULT_DATE_REMBOURSEMENT.toString())))
            .andExpect(jsonPath("$.[*].idOwner").value(hasItem(DEFAULT_ID_OWNER.intValue())));
    }
    

    @Test
    @Transactional
    public void getRemboursementCredit() throws Exception {
        // Initialize the database
        remboursementCreditRepository.saveAndFlush(remboursementCredit);

        // Get the remboursementCredit
        restRemboursementCreditMockMvc.perform(get("/api/remboursement-credits/{id}", remboursementCredit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(remboursementCredit.getId().intValue()))
            .andExpect(jsonPath("$.idRemboursement").value(DEFAULT_ID_REMBOURSEMENT.intValue()))
            .andExpect(jsonPath("$.dateRemboursement").value(DEFAULT_DATE_REMBOURSEMENT.toString()))
            .andExpect(jsonPath("$.idOwner").value(DEFAULT_ID_OWNER.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingRemboursementCredit() throws Exception {
        // Get the remboursementCredit
        restRemboursementCreditMockMvc.perform(get("/api/remboursement-credits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRemboursementCredit() throws Exception {
        // Initialize the database
        remboursementCreditRepository.saveAndFlush(remboursementCredit);

        int databaseSizeBeforeUpdate = remboursementCreditRepository.findAll().size();

        // Update the remboursementCredit
        RemboursementCredit updatedRemboursementCredit = remboursementCreditRepository.findById(remboursementCredit.getId()).get();
        // Disconnect from session so that the updates on updatedRemboursementCredit are not directly saved in db
        em.detach(updatedRemboursementCredit);
        updatedRemboursementCredit
            .idRemboursement(UPDATED_ID_REMBOURSEMENT)
            .dateRemboursement(UPDATED_DATE_REMBOURSEMENT)
            .idOwner(UPDATED_ID_OWNER);

        restRemboursementCreditMockMvc.perform(put("/api/remboursement-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRemboursementCredit)))
            .andExpect(status().isOk());

        // Validate the RemboursementCredit in the database
        List<RemboursementCredit> remboursementCreditList = remboursementCreditRepository.findAll();
        assertThat(remboursementCreditList).hasSize(databaseSizeBeforeUpdate);
        RemboursementCredit testRemboursementCredit = remboursementCreditList.get(remboursementCreditList.size() - 1);
        assertThat(testRemboursementCredit.getIdRemboursement()).isEqualTo(UPDATED_ID_REMBOURSEMENT);
        assertThat(testRemboursementCredit.getDateRemboursement()).isEqualTo(UPDATED_DATE_REMBOURSEMENT);
        assertThat(testRemboursementCredit.getIdOwner()).isEqualTo(UPDATED_ID_OWNER);
    }

    @Test
    @Transactional
    public void updateNonExistingRemboursementCredit() throws Exception {
        int databaseSizeBeforeUpdate = remboursementCreditRepository.findAll().size();

        // Create the RemboursementCredit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRemboursementCreditMockMvc.perform(put("/api/remboursement-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remboursementCredit)))
            .andExpect(status().isBadRequest());

        // Validate the RemboursementCredit in the database
        List<RemboursementCredit> remboursementCreditList = remboursementCreditRepository.findAll();
        assertThat(remboursementCreditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRemboursementCredit() throws Exception {
        // Initialize the database
        remboursementCreditRepository.saveAndFlush(remboursementCredit);

        int databaseSizeBeforeDelete = remboursementCreditRepository.findAll().size();

        // Get the remboursementCredit
        restRemboursementCreditMockMvc.perform(delete("/api/remboursement-credits/{id}", remboursementCredit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RemboursementCredit> remboursementCreditList = remboursementCreditRepository.findAll();
        assertThat(remboursementCreditList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RemboursementCredit.class);
        RemboursementCredit remboursementCredit1 = new RemboursementCredit();
        remboursementCredit1.setId(1L);
        RemboursementCredit remboursementCredit2 = new RemboursementCredit();
        remboursementCredit2.setId(remboursementCredit1.getId());
        assertThat(remboursementCredit1).isEqualTo(remboursementCredit2);
        remboursementCredit2.setId(2L);
        assertThat(remboursementCredit1).isNotEqualTo(remboursementCredit2);
        remboursementCredit1.setId(null);
        assertThat(remboursementCredit1).isNotEqualTo(remboursementCredit2);
    }
}
