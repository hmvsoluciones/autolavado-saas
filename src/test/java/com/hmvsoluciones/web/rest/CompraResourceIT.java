package com.hmvsoluciones.web.rest;

import static com.hmvsoluciones.domain.CompraAsserts.*;
import static com.hmvsoluciones.web.rest.TestUtil.createUpdateProxyForBean;
import static com.hmvsoluciones.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmvsoluciones.IntegrationTest;
import com.hmvsoluciones.domain.Compra;
import com.hmvsoluciones.domain.Proveedor;
import com.hmvsoluciones.repository.CompraRepository;
import com.hmvsoluciones.service.dto.CompraDTO;
import com.hmvsoluciones.service.mapper.CompraMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CompraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompraResourceIT {

    private static final LocalDate DEFAULT_FECHA_COMPRA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_COMPRA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_COMPRA = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/compras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private CompraMapper compraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompraMockMvc;

    private Compra compra;

    private Compra insertedCompra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createEntity() {
        return new Compra().fechaCompra(DEFAULT_FECHA_COMPRA).total(DEFAULT_TOTAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createUpdatedEntity() {
        return new Compra().fechaCompra(UPDATED_FECHA_COMPRA).total(UPDATED_TOTAL);
    }

    @BeforeEach
    void initTest() {
        compra = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompra != null) {
            compraRepository.delete(insertedCompra);
            insertedCompra = null;
        }
    }

    @Test
    @Transactional
    void createCompra() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);
        var returnedCompraDTO = om.readValue(
            restCompraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompraDTO.class
        );

        // Validate the Compra in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompra = compraMapper.toEntity(returnedCompraDTO);
        assertCompraUpdatableFieldsEquals(returnedCompra, getPersistedCompra(returnedCompra));

        insertedCompra = returnedCompra;
    }

    @Test
    @Transactional
    void createCompraWithExistingId() throws Exception {
        // Create the Compra with an existing ID
        compra.setId(1L);
        CompraDTO compraDTO = compraMapper.toDto(compra);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaCompraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compra.setFechaCompra(null);

        // Create the Compra, which fails.
        CompraDTO compraDTO = compraMapper.toDto(compra);

        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compra.setTotal(null);

        // Create the Compra, which fails.
        CompraDTO compraDTO = compraMapper.toDto(compra);

        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompras() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compra.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCompra").value(hasItem(DEFAULT_FECHA_COMPRA.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))));
    }

    @Test
    @Transactional
    void getCompra() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get the compra
        restCompraMockMvc
            .perform(get(ENTITY_API_URL_ID, compra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compra.getId().intValue()))
            .andExpect(jsonPath("$.fechaCompra").value(DEFAULT_FECHA_COMPRA.toString()))
            .andExpect(jsonPath("$.total").value(sameNumber(DEFAULT_TOTAL)));
    }

    @Test
    @Transactional
    void getComprasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        Long id = compra.getId();

        defaultCompraFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCompraFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCompraFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComprasByFechaCompraIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where fechaCompra equals to
        defaultCompraFiltering("fechaCompra.equals=" + DEFAULT_FECHA_COMPRA, "fechaCompra.equals=" + UPDATED_FECHA_COMPRA);
    }

    @Test
    @Transactional
    void getAllComprasByFechaCompraIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where fechaCompra in
        defaultCompraFiltering(
            "fechaCompra.in=" + DEFAULT_FECHA_COMPRA + "," + UPDATED_FECHA_COMPRA,
            "fechaCompra.in=" + UPDATED_FECHA_COMPRA
        );
    }

    @Test
    @Transactional
    void getAllComprasByFechaCompraIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where fechaCompra is not null
        defaultCompraFiltering("fechaCompra.specified=true", "fechaCompra.specified=false");
    }

    @Test
    @Transactional
    void getAllComprasByFechaCompraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where fechaCompra is greater than or equal to
        defaultCompraFiltering(
            "fechaCompra.greaterThanOrEqual=" + DEFAULT_FECHA_COMPRA,
            "fechaCompra.greaterThanOrEqual=" + UPDATED_FECHA_COMPRA
        );
    }

    @Test
    @Transactional
    void getAllComprasByFechaCompraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where fechaCompra is less than or equal to
        defaultCompraFiltering(
            "fechaCompra.lessThanOrEqual=" + DEFAULT_FECHA_COMPRA,
            "fechaCompra.lessThanOrEqual=" + SMALLER_FECHA_COMPRA
        );
    }

    @Test
    @Transactional
    void getAllComprasByFechaCompraIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where fechaCompra is less than
        defaultCompraFiltering("fechaCompra.lessThan=" + UPDATED_FECHA_COMPRA, "fechaCompra.lessThan=" + DEFAULT_FECHA_COMPRA);
    }

    @Test
    @Transactional
    void getAllComprasByFechaCompraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where fechaCompra is greater than
        defaultCompraFiltering("fechaCompra.greaterThan=" + SMALLER_FECHA_COMPRA, "fechaCompra.greaterThan=" + DEFAULT_FECHA_COMPRA);
    }

    @Test
    @Transactional
    void getAllComprasByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where total equals to
        defaultCompraFiltering("total.equals=" + DEFAULT_TOTAL, "total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprasByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where total in
        defaultCompraFiltering("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL, "total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprasByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where total is not null
        defaultCompraFiltering("total.specified=true", "total.specified=false");
    }

    @Test
    @Transactional
    void getAllComprasByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where total is greater than or equal to
        defaultCompraFiltering("total.greaterThanOrEqual=" + DEFAULT_TOTAL, "total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprasByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where total is less than or equal to
        defaultCompraFiltering("total.lessThanOrEqual=" + DEFAULT_TOTAL, "total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprasByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where total is less than
        defaultCompraFiltering("total.lessThan=" + UPDATED_TOTAL, "total.lessThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprasByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        // Get all the compraList where total is greater than
        defaultCompraFiltering("total.greaterThan=" + SMALLER_TOTAL, "total.greaterThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprasByProveedorIsEqualToSomething() throws Exception {
        Proveedor proveedor;
        if (TestUtil.findAll(em, Proveedor.class).isEmpty()) {
            compraRepository.saveAndFlush(compra);
            proveedor = ProveedorResourceIT.createEntity();
        } else {
            proveedor = TestUtil.findAll(em, Proveedor.class).get(0);
        }
        em.persist(proveedor);
        em.flush();
        compra.setProveedor(proveedor);
        compraRepository.saveAndFlush(compra);
        Long proveedorId = proveedor.getId();
        // Get all the compraList where proveedor equals to proveedorId
        defaultCompraShouldBeFound("proveedorId.equals=" + proveedorId);

        // Get all the compraList where proveedor equals to (proveedorId + 1)
        defaultCompraShouldNotBeFound("proveedorId.equals=" + (proveedorId + 1));
    }

    private void defaultCompraFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCompraShouldBeFound(shouldBeFound);
        defaultCompraShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompraShouldBeFound(String filter) throws Exception {
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compra.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCompra").value(hasItem(DEFAULT_FECHA_COMPRA.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))));

        // Check, that the count call also returns 1
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompraShouldNotBeFound(String filter) throws Exception {
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompra() throws Exception {
        // Get the compra
        restCompraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompra() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compra
        Compra updatedCompra = compraRepository.findById(compra.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompra are not directly saved in db
        em.detach(updatedCompra);
        updatedCompra.fechaCompra(UPDATED_FECHA_COMPRA).total(UPDATED_TOTAL);
        CompraDTO compraDTO = compraMapper.toDto(updatedCompra);

        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compraDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraDTO))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompraToMatchAllProperties(updatedCompra);
    }

    @Test
    @Transactional
    void putNonExistingCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compraDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompraUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompra, compra), getPersistedCompra(compra));
    }

    @Test
    @Transactional
    void fullUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        partialUpdatedCompra.fechaCompra(UPDATED_FECHA_COMPRA).total(UPDATED_TOTAL);

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompraUpdatableFieldsEquals(partialUpdatedCompra, getPersistedCompra(partialUpdatedCompra));
    }

    @Test
    @Transactional
    void patchNonExistingCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compra.setId(longCount.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompra() throws Exception {
        // Initialize the database
        insertedCompra = compraRepository.saveAndFlush(compra);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compra
        restCompraMockMvc
            .perform(delete(ENTITY_API_URL_ID, compra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return compraRepository.count();
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

    protected Compra getPersistedCompra(Compra compra) {
        return compraRepository.findById(compra.getId()).orElseThrow();
    }

    protected void assertPersistedCompraToMatchAllProperties(Compra expectedCompra) {
        assertCompraAllPropertiesEquals(expectedCompra, getPersistedCompra(expectedCompra));
    }

    protected void assertPersistedCompraToMatchUpdatableProperties(Compra expectedCompra) {
        assertCompraAllUpdatablePropertiesEquals(expectedCompra, getPersistedCompra(expectedCompra));
    }
}
