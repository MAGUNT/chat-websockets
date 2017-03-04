package com.chat.chat.web.rest;

import com.chat.chat.ChatApp;

import com.chat.chat.domain.Mesage;
import com.chat.chat.repository.MesageRepository;
import com.chat.chat.service.MesageService;
import com.chat.chat.service.dto.MesageDTO;
import com.chat.chat.service.mapper.MesageMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.chat.chat.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MesageResource REST controller.
 *
 * @see MesageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChatApp.class)
public class MesageResourceIntTest {

    private static final String DEFAULT_MESAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESAGE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private MesageRepository mesageRepository;

    @Autowired
    private MesageMapper mesageMapper;

    @Autowired
    private MesageService mesageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restMesageMockMvc;

    private Mesage mesage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MesageResource mesageResource = new MesageResource(mesageService);
        this.restMesageMockMvc = MockMvcBuilders.standaloneSetup(mesageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mesage createEntity(EntityManager em) {
        Mesage mesage = new Mesage()
                .mesage(DEFAULT_MESAGE)
                .date(DEFAULT_DATE);
        return mesage;
    }

    @Before
    public void initTest() {
        mesage = createEntity(em);
    }

    @Test
    @Transactional
    public void createMesage() throws Exception {
        int databaseSizeBeforeCreate = mesageRepository.findAll().size();

        // Create the Mesage
        MesageDTO mesageDTO = mesageMapper.mesageToMesageDTO(mesage);

        restMesageMockMvc.perform(post("/api/mesages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mesageDTO)))
            .andExpect(status().isCreated());

        // Validate the Mesage in the database
        List<Mesage> mesageList = mesageRepository.findAll();
        assertThat(mesageList).hasSize(databaseSizeBeforeCreate + 1);
        Mesage testMesage = mesageList.get(mesageList.size() - 1);
        assertThat(testMesage.getMesage()).isEqualTo(DEFAULT_MESAGE);
        assertThat(testMesage.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createMesageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mesageRepository.findAll().size();

        // Create the Mesage with an existing ID
        Mesage existingMesage = new Mesage();
        existingMesage.setId(1L);
        MesageDTO existingMesageDTO = mesageMapper.mesageToMesageDTO(existingMesage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMesageMockMvc.perform(post("/api/mesages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingMesageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Mesage> mesageList = mesageRepository.findAll();
        assertThat(mesageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMesageIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesageRepository.findAll().size();
        // set the field null
        mesage.setMesage(null);

        // Create the Mesage, which fails.
        MesageDTO mesageDTO = mesageMapper.mesageToMesageDTO(mesage);

        restMesageMockMvc.perform(post("/api/mesages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mesageDTO)))
            .andExpect(status().isBadRequest());

        List<Mesage> mesageList = mesageRepository.findAll();
        assertThat(mesageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesageRepository.findAll().size();
        // set the field null
        mesage.setDate(null);

        // Create the Mesage, which fails.
        MesageDTO mesageDTO = mesageMapper.mesageToMesageDTO(mesage);

        restMesageMockMvc.perform(post("/api/mesages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mesageDTO)))
            .andExpect(status().isBadRequest());

        List<Mesage> mesageList = mesageRepository.findAll();
        assertThat(mesageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMesages() throws Exception {
        // Initialize the database
        mesageRepository.saveAndFlush(mesage);

        // Get all the mesageList
        restMesageMockMvc.perform(get("/api/mesages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mesage.getId().intValue())))
            .andExpect(jsonPath("$.[*].mesage").value(hasItem(DEFAULT_MESAGE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    public void getMesage() throws Exception {
        // Initialize the database
        mesageRepository.saveAndFlush(mesage);

        // Get the mesage
        restMesageMockMvc.perform(get("/api/mesages/{id}", mesage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mesage.getId().intValue()))
            .andExpect(jsonPath("$.mesage").value(DEFAULT_MESAGE.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingMesage() throws Exception {
        // Get the mesage
        restMesageMockMvc.perform(get("/api/mesages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMesage() throws Exception {
        // Initialize the database
        mesageRepository.saveAndFlush(mesage);
        int databaseSizeBeforeUpdate = mesageRepository.findAll().size();

        // Update the mesage
        Mesage updatedMesage = mesageRepository.findOne(mesage.getId());
        updatedMesage
                .mesage(UPDATED_MESAGE)
                .date(UPDATED_DATE);
        MesageDTO mesageDTO = mesageMapper.mesageToMesageDTO(updatedMesage);

        restMesageMockMvc.perform(put("/api/mesages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mesageDTO)))
            .andExpect(status().isOk());

        // Validate the Mesage in the database
        List<Mesage> mesageList = mesageRepository.findAll();
        assertThat(mesageList).hasSize(databaseSizeBeforeUpdate);
        Mesage testMesage = mesageList.get(mesageList.size() - 1);
        assertThat(testMesage.getMesage()).isEqualTo(UPDATED_MESAGE);
        assertThat(testMesage.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingMesage() throws Exception {
        int databaseSizeBeforeUpdate = mesageRepository.findAll().size();

        // Create the Mesage
        MesageDTO mesageDTO = mesageMapper.mesageToMesageDTO(mesage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMesageMockMvc.perform(put("/api/mesages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mesageDTO)))
            .andExpect(status().isCreated());

        // Validate the Mesage in the database
        List<Mesage> mesageList = mesageRepository.findAll();
        assertThat(mesageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMesage() throws Exception {
        // Initialize the database
        mesageRepository.saveAndFlush(mesage);
        int databaseSizeBeforeDelete = mesageRepository.findAll().size();

        // Get the mesage
        restMesageMockMvc.perform(delete("/api/mesages/{id}", mesage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Mesage> mesageList = mesageRepository.findAll();
        assertThat(mesageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mesage.class);
    }
}
