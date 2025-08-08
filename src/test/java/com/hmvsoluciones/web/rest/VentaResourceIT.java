package com.hmvsoluciones.web.rest;

import static com.hmvsoluciones.domain.VentaAsserts.*;
import static com.hmvsoluciones.web.rest.TestUtil.createUpdateProxyForBean;
import static com.hmvsoluciones.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmvsoluciones.IntegrationTest;
import com.hmvsoluciones.domain.Cliente;
import com.hmvsoluciones.domain.Venta;
import com.hmvsoluciones.repository.VentaRepository;
import com.hmvsoluciones.service.dto.VentaDTO;
import com.hmvsoluciones.service.mapper.VentaMapper;
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
 * Integration tests for the {@link VentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VentaResourceIT {

    private static final LocalDate DEFAULT_FECHA_VENTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_VENTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_VENTA = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaMapper ventaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVentaMockMvc;

    private Venta venta;

    private Venta insertedVenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venta createEntity() {
        return new Venta().fechaVenta(DEFAULT_FECHA_VENTA).total(DEFAULT_TOTAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venta createUpdatedEntity() {
        return new Venta().fechaVenta(UPDATED_FECHA_VENTA).total(UPDATED_TOTAL);
    }

    @BeforeEach
    void initTest() {
        venta = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVenta != null) {
            ventaRepository.delete(insertedVenta);
            insertedVenta = null;
        }
    }

    @Test
    @Transactional
    void createVenta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);
        var returnedVentaDTO = om.readValue(
            restVentaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VentaDTO.class
        );

        // Validate the Venta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVenta = ventaMapper.toEntity(returnedVentaDTO);
        assertVentaUpdatableFieldsEquals(returnedVenta, getPersistedVenta(returnedVenta));

        insertedVenta = returnedVenta;
    }

    @Test
    @Transactional
    void createVentaWithExistingId() throws Exception {
        // Create the Venta with an existing ID
        venta.setId(1L);
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaVentaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        venta.setFechaVenta(null);

        // Create the Venta, which fails.
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        venta.setTotal(null);

        // Create the Venta, which fails.
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVentas() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList
        restVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(venta.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaVenta").value(hasItem(DEFAULT_FECHA_VENTA.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))));
    }

    @Test
    @Transactional
    void getVenta() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get the venta
        restVentaMockMvc
            .perform(get(ENTITY_API_URL_ID, venta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(venta.getId().intValue()))
            .andExpect(jsonPath("$.fechaVenta").value(DEFAULT_FECHA_VENTA.toString()))
            .andExpect(jsonPath("$.total").value(sameNumber(DEFAULT_TOTAL)));
    }

    @Test
    @Transactional
    void getVentasByIdFiltering() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        Long id = venta.getId();

        defaultVentaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVentaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVentaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVentasByFechaVentaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where fechaVenta equals to
        defaultVentaFiltering("fechaVenta.equals=" + DEFAULT_FECHA_VENTA, "fechaVenta.equals=" + UPDATED_FECHA_VENTA);
    }

    @Test
    @Transactional
    void getAllVentasByFechaVentaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where fechaVenta in
        defaultVentaFiltering("fechaVenta.in=" + DEFAULT_FECHA_VENTA + "," + UPDATED_FECHA_VENTA, "fechaVenta.in=" + UPDATED_FECHA_VENTA);
    }

    @Test
    @Transactional
    void getAllVentasByFechaVentaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where fechaVenta is not null
        defaultVentaFiltering("fechaVenta.specified=true", "fechaVenta.specified=false");
    }

    @Test
    @Transactional
    void getAllVentasByFechaVentaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where fechaVenta is greater than or equal to
        defaultVentaFiltering(
            "fechaVenta.greaterThanOrEqual=" + DEFAULT_FECHA_VENTA,
            "fechaVenta.greaterThanOrEqual=" + UPDATED_FECHA_VENTA
        );
    }

    @Test
    @Transactional
    void getAllVentasByFechaVentaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where fechaVenta is less than or equal to
        defaultVentaFiltering("fechaVenta.lessThanOrEqual=" + DEFAULT_FECHA_VENTA, "fechaVenta.lessThanOrEqual=" + SMALLER_FECHA_VENTA);
    }

    @Test
    @Transactional
    void getAllVentasByFechaVentaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where fechaVenta is less than
        defaultVentaFiltering("fechaVenta.lessThan=" + UPDATED_FECHA_VENTA, "fechaVenta.lessThan=" + DEFAULT_FECHA_VENTA);
    }

    @Test
    @Transactional
    void getAllVentasByFechaVentaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where fechaVenta is greater than
        defaultVentaFiltering("fechaVenta.greaterThan=" + SMALLER_FECHA_VENTA, "fechaVenta.greaterThan=" + DEFAULT_FECHA_VENTA);
    }

    @Test
    @Transactional
    void getAllVentasByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where total equals to
        defaultVentaFiltering("total.equals=" + DEFAULT_TOTAL, "total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentasByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where total in
        defaultVentaFiltering("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL, "total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentasByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where total is not null
        defaultVentaFiltering("total.specified=true", "total.specified=false");
    }

    @Test
    @Transactional
    void getAllVentasByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where total is greater than or equal to
        defaultVentaFiltering("total.greaterThanOrEqual=" + DEFAULT_TOTAL, "total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentasByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where total is less than or equal to
        defaultVentaFiltering("total.lessThanOrEqual=" + DEFAULT_TOTAL, "total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentasByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where total is less than
        defaultVentaFiltering("total.lessThan=" + UPDATED_TOTAL, "total.lessThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentasByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList where total is greater than
        defaultVentaFiltering("total.greaterThan=" + SMALLER_TOTAL, "total.greaterThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllVentasByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            ventaRepository.saveAndFlush(venta);
            cliente = ClienteResourceIT.createEntity();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        venta.setCliente(cliente);
        ventaRepository.saveAndFlush(venta);
        Long clienteId = cliente.getId();
        // Get all the ventaList where cliente equals to clienteId
        defaultVentaShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the ventaList where cliente equals to (clienteId + 1)
        defaultVentaShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultVentaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVentaShouldBeFound(shouldBeFound);
        defaultVentaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVentaShouldBeFound(String filter) throws Exception {
        restVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(venta.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaVenta").value(hasItem(DEFAULT_FECHA_VENTA.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))));

        // Check, that the count call also returns 1
        restVentaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVentaShouldNotBeFound(String filter) throws Exception {
        restVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVentaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVenta() throws Exception {
        // Get the venta
        restVentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVenta() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venta
        Venta updatedVenta = ventaRepository.findById(venta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVenta are not directly saved in db
        em.detach(updatedVenta);
        updatedVenta.fechaVenta(UPDATED_FECHA_VENTA).total(UPDATED_TOTAL);
        VentaDTO ventaDTO = ventaMapper.toDto(updatedVenta);

        restVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ventaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVentaToMatchAllProperties(updatedVenta);
    }

    @Test
    @Transactional
    void putNonExistingVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ventaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ventaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVentaWithPatch() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venta using partial update
        Venta partialUpdatedVenta = new Venta();
        partialUpdatedVenta.setId(venta.getId());

        partialUpdatedVenta.fechaVenta(UPDATED_FECHA_VENTA).total(UPDATED_TOTAL);

        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVenta))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVentaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVenta, venta), getPersistedVenta(venta));
    }

    @Test
    @Transactional
    void fullUpdateVentaWithPatch() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venta using partial update
        Venta partialUpdatedVenta = new Venta();
        partialUpdatedVenta.setId(venta.getId());

        partialUpdatedVenta.fechaVenta(UPDATED_FECHA_VENTA).total(UPDATED_TOTAL);

        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVenta))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVentaUpdatableFieldsEquals(partialUpdatedVenta, getPersistedVenta(partialUpdatedVenta));
    }

    @Test
    @Transactional
    void patchNonExistingVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ventaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ventaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ventaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVenta() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the venta
        restVentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, venta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ventaRepository.count();
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

    protected Venta getPersistedVenta(Venta venta) {
        return ventaRepository.findById(venta.getId()).orElseThrow();
    }

    protected void assertPersistedVentaToMatchAllProperties(Venta expectedVenta) {
        assertVentaAllPropertiesEquals(expectedVenta, getPersistedVenta(expectedVenta));
    }

    protected void assertPersistedVentaToMatchUpdatableProperties(Venta expectedVenta) {
        assertVentaAllUpdatablePropertiesEquals(expectedVenta, getPersistedVenta(expectedVenta));
    }
}
