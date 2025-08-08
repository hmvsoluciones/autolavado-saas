package com.hmvsoluciones.web.rest;

import static com.hmvsoluciones.domain.ProveedorAsserts.*;
import static com.hmvsoluciones.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmvsoluciones.IntegrationTest;
import com.hmvsoluciones.domain.Proveedor;
import com.hmvsoluciones.repository.ProveedorRepository;
import com.hmvsoluciones.service.dto.ProveedorDTO;
import com.hmvsoluciones.service.mapper.ProveedorMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProveedorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProveedorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_RAZON_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZON_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_RFC = "AAAAAAAAAA";
    private static final String UPDATED_RFC = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/proveedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProveedorMapper proveedorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProveedorMockMvc;

    private Proveedor proveedor;

    private Proveedor insertedProveedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proveedor createEntity() {
        return new Proveedor()
            .nombre(DEFAULT_NOMBRE)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .razonSocial(DEFAULT_RAZON_SOCIAL)
            .rfc(DEFAULT_RFC)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proveedor createUpdatedEntity() {
        return new Proveedor()
            .nombre(UPDATED_NOMBRE)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .rfc(UPDATED_RFC)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        proveedor = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProveedor != null) {
            proveedorRepository.delete(insertedProveedor);
            insertedProveedor = null;
        }
    }

    @Test
    @Transactional
    void createProveedor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);
        var returnedProveedorDTO = om.readValue(
            restProveedorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProveedorDTO.class
        );

        // Validate the Proveedor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProveedor = proveedorMapper.toEntity(returnedProveedorDTO);
        assertProveedorUpdatableFieldsEquals(returnedProveedor, getPersistedProveedor(returnedProveedor));

        insertedProveedor = returnedProveedor;
    }

    @Test
    @Transactional
    void createProveedorWithExistingId() throws Exception {
        // Create the Proveedor with an existing ID
        proveedor.setId(1L);
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setNombre(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProveedors() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].razonSocial").value(hasItem(DEFAULT_RAZON_SOCIAL)))
            .andExpect(jsonPath("$.[*].rfc").value(hasItem(DEFAULT_RFC)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getProveedor() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get the proveedor
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL_ID, proveedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proveedor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.razonSocial").value(DEFAULT_RAZON_SOCIAL))
            .andExpect(jsonPath("$.rfc").value(DEFAULT_RFC))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getProveedorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        Long id = proveedor.getId();

        defaultProveedorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProveedorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProveedorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre equals to
        defaultProveedorFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre in
        defaultProveedorFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre is not null
        defaultProveedorFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre contains
        defaultProveedorFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre does not contain
        defaultProveedorFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where email equals to
        defaultProveedorFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProveedorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where email in
        defaultProveedorFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProveedorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where email is not null
        defaultProveedorFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where email contains
        defaultProveedorFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProveedorsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where email does not contain
        defaultProveedorFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllProveedorsByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where telefono equals to
        defaultProveedorFiltering("telefono.equals=" + DEFAULT_TELEFONO, "telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where telefono in
        defaultProveedorFiltering("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO, "telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where telefono is not null
        defaultProveedorFiltering("telefono.specified=true", "telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where telefono contains
        defaultProveedorFiltering("telefono.contains=" + DEFAULT_TELEFONO, "telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where telefono does not contain
        defaultProveedorFiltering("telefono.doesNotContain=" + UPDATED_TELEFONO, "telefono.doesNotContain=" + DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByRazonSocialIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where razonSocial equals to
        defaultProveedorFiltering("razonSocial.equals=" + DEFAULT_RAZON_SOCIAL, "razonSocial.equals=" + UPDATED_RAZON_SOCIAL);
    }

    @Test
    @Transactional
    void getAllProveedorsByRazonSocialIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where razonSocial in
        defaultProveedorFiltering(
            "razonSocial.in=" + DEFAULT_RAZON_SOCIAL + "," + UPDATED_RAZON_SOCIAL,
            "razonSocial.in=" + UPDATED_RAZON_SOCIAL
        );
    }

    @Test
    @Transactional
    void getAllProveedorsByRazonSocialIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where razonSocial is not null
        defaultProveedorFiltering("razonSocial.specified=true", "razonSocial.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByRazonSocialContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where razonSocial contains
        defaultProveedorFiltering("razonSocial.contains=" + DEFAULT_RAZON_SOCIAL, "razonSocial.contains=" + UPDATED_RAZON_SOCIAL);
    }

    @Test
    @Transactional
    void getAllProveedorsByRazonSocialNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where razonSocial does not contain
        defaultProveedorFiltering(
            "razonSocial.doesNotContain=" + UPDATED_RAZON_SOCIAL,
            "razonSocial.doesNotContain=" + DEFAULT_RAZON_SOCIAL
        );
    }

    @Test
    @Transactional
    void getAllProveedorsByRfcIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where rfc equals to
        defaultProveedorFiltering("rfc.equals=" + DEFAULT_RFC, "rfc.equals=" + UPDATED_RFC);
    }

    @Test
    @Transactional
    void getAllProveedorsByRfcIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where rfc in
        defaultProveedorFiltering("rfc.in=" + DEFAULT_RFC + "," + UPDATED_RFC, "rfc.in=" + UPDATED_RFC);
    }

    @Test
    @Transactional
    void getAllProveedorsByRfcIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where rfc is not null
        defaultProveedorFiltering("rfc.specified=true", "rfc.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByRfcContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where rfc contains
        defaultProveedorFiltering("rfc.contains=" + DEFAULT_RFC, "rfc.contains=" + UPDATED_RFC);
    }

    @Test
    @Transactional
    void getAllProveedorsByRfcNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where rfc does not contain
        defaultProveedorFiltering("rfc.doesNotContain=" + UPDATED_RFC, "rfc.doesNotContain=" + DEFAULT_RFC);
    }

    @Test
    @Transactional
    void getAllProveedorsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where activo equals to
        defaultProveedorFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllProveedorsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where activo in
        defaultProveedorFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllProveedorsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where activo is not null
        defaultProveedorFiltering("activo.specified=true", "activo.specified=false");
    }

    private void defaultProveedorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProveedorShouldBeFound(shouldBeFound);
        defaultProveedorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProveedorShouldBeFound(String filter) throws Exception {
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].razonSocial").value(hasItem(DEFAULT_RAZON_SOCIAL)))
            .andExpect(jsonPath("$.[*].rfc").value(hasItem(DEFAULT_RFC)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));

        // Check, that the count call also returns 1
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProveedorShouldNotBeFound(String filter) throws Exception {
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProveedor() throws Exception {
        // Get the proveedor
        restProveedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProveedor() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proveedor
        Proveedor updatedProveedor = proveedorRepository.findById(proveedor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProveedor are not directly saved in db
        em.detach(updatedProveedor);
        updatedProveedor
            .nombre(UPDATED_NOMBRE)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .rfc(UPDATED_RFC)
            .activo(UPDATED_ACTIVO);
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(updatedProveedor);

        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProveedorToMatchAllProperties(updatedProveedor);
    }

    @Test
    @Transactional
    void putNonExistingProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proveedor using partial update
        Proveedor partialUpdatedProveedor = new Proveedor();
        partialUpdatedProveedor.setId(proveedor.getId());

        partialUpdatedProveedor.rfc(UPDATED_RFC).activo(UPDATED_ACTIVO);

        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProveedor))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProveedorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProveedor, proveedor),
            getPersistedProveedor(proveedor)
        );
    }

    @Test
    @Transactional
    void fullUpdateProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proveedor using partial update
        Proveedor partialUpdatedProveedor = new Proveedor();
        partialUpdatedProveedor.setId(proveedor.getId());

        partialUpdatedProveedor
            .nombre(UPDATED_NOMBRE)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .rfc(UPDATED_RFC)
            .activo(UPDATED_ACTIVO);

        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProveedor))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProveedorUpdatableFieldsEquals(partialUpdatedProveedor, getPersistedProveedor(partialUpdatedProveedor));
    }

    @Test
    @Transactional
    void patchNonExistingProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProveedor() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the proveedor
        restProveedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, proveedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return proveedorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Proveedor getPersistedProveedor(Proveedor proveedor) {
        return proveedorRepository.findById(proveedor.getId()).orElseThrow();
    }

    protected void assertPersistedProveedorToMatchAllProperties(Proveedor expectedProveedor) {
        assertProveedorAllPropertiesEquals(expectedProveedor, getPersistedProveedor(expectedProveedor));
    }

    protected void assertPersistedProveedorToMatchUpdatableProperties(Proveedor expectedProveedor) {
        assertProveedorAllUpdatablePropertiesEquals(expectedProveedor, getPersistedProveedor(expectedProveedor));
    }
}
