package com.hmvsoluciones.web.rest;

import static com.hmvsoluciones.domain.DetalleVentaAsserts.*;
import static com.hmvsoluciones.web.rest.TestUtil.createUpdateProxyForBean;
import static com.hmvsoluciones.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmvsoluciones.IntegrationTest;
import com.hmvsoluciones.domain.DetalleVenta;
import com.hmvsoluciones.domain.Producto;
import com.hmvsoluciones.domain.Venta;
import com.hmvsoluciones.repository.DetalleVentaRepository;
import com.hmvsoluciones.service.dto.DetalleVentaDTO;
import com.hmvsoluciones.service.mapper.DetalleVentaMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link DetalleVentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetalleVentaResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final BigDecimal DEFAULT_PRECIO_UNITARIO = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO_UNITARIO = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRECIO_UNITARIO = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_SUBTOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_SUBTOTAL = new BigDecimal(1);
    private static final BigDecimal SMALLER_SUBTOTAL = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/detalle-ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private DetalleVentaMapper detalleVentaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetalleVentaMockMvc;

    private DetalleVenta detalleVenta;

    private DetalleVenta insertedDetalleVenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleVenta createEntity() {
        return new DetalleVenta().cantidad(DEFAULT_CANTIDAD).precioUnitario(DEFAULT_PRECIO_UNITARIO).subtotal(DEFAULT_SUBTOTAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleVenta createUpdatedEntity() {
        return new DetalleVenta().cantidad(UPDATED_CANTIDAD).precioUnitario(UPDATED_PRECIO_UNITARIO).subtotal(UPDATED_SUBTOTAL);
    }

    @BeforeEach
    void initTest() {
        detalleVenta = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDetalleVenta != null) {
            detalleVentaRepository.delete(insertedDetalleVenta);
            insertedDetalleVenta = null;
        }
    }

    @Test
    @Transactional
    void createDetalleVenta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);
        var returnedDetalleVentaDTO = om.readValue(
            restDetalleVentaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DetalleVentaDTO.class
        );

        // Validate the DetalleVenta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDetalleVenta = detalleVentaMapper.toEntity(returnedDetalleVentaDTO);
        assertDetalleVentaUpdatableFieldsEquals(returnedDetalleVenta, getPersistedDetalleVenta(returnedDetalleVenta));

        insertedDetalleVenta = returnedDetalleVenta;
    }

    @Test
    @Transactional
    void createDetalleVentaWithExistingId() throws Exception {
        // Create the DetalleVenta with an existing ID
        detalleVenta.setId(1L);
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleVenta.setCantidad(null);

        // Create the DetalleVenta, which fails.
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioUnitarioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleVenta.setPrecioUnitario(null);

        // Create the DetalleVenta, which fails.
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleVenta.setSubtotal(null);

        // Create the DetalleVenta, which fails.
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetalleVentas() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalleVenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(sameNumber(DEFAULT_PRECIO_UNITARIO))))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(sameNumber(DEFAULT_SUBTOTAL))));
    }

    @Test
    @Transactional
    void getDetalleVenta() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get the detalleVenta
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL_ID, detalleVenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detalleVenta.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.precioUnitario").value(sameNumber(DEFAULT_PRECIO_UNITARIO)))
            .andExpect(jsonPath("$.subtotal").value(sameNumber(DEFAULT_SUBTOTAL)));
    }

    @Test
    @Transactional
    void getDetalleVentasByIdFiltering() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        Long id = detalleVenta.getId();

        defaultDetalleVentaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDetalleVentaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDetalleVentaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDetalleVentasByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where cantidad equals to
        defaultDetalleVentaFiltering("cantidad.equals=" + DEFAULT_CANTIDAD, "cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetalleVentasByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where cantidad in
        defaultDetalleVentaFiltering("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD, "cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetalleVentasByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where cantidad is not null
        defaultDetalleVentaFiltering("cantidad.specified=true", "cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllDetalleVentasByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where cantidad is greater than or equal to
        defaultDetalleVentaFiltering("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD, "cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetalleVentasByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where cantidad is less than or equal to
        defaultDetalleVentaFiltering("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD, "cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetalleVentasByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where cantidad is less than
        defaultDetalleVentaFiltering("cantidad.lessThan=" + UPDATED_CANTIDAD, "cantidad.lessThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetalleVentasByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where cantidad is greater than
        defaultDetalleVentaFiltering("cantidad.greaterThan=" + SMALLER_CANTIDAD, "cantidad.greaterThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetalleVentasByPrecioUnitarioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where precioUnitario equals to
        defaultDetalleVentaFiltering(
            "precioUnitario.equals=" + DEFAULT_PRECIO_UNITARIO,
            "precioUnitario.equals=" + UPDATED_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleVentasByPrecioUnitarioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where precioUnitario in
        defaultDetalleVentaFiltering(
            "precioUnitario.in=" + DEFAULT_PRECIO_UNITARIO + "," + UPDATED_PRECIO_UNITARIO,
            "precioUnitario.in=" + UPDATED_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleVentasByPrecioUnitarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where precioUnitario is not null
        defaultDetalleVentaFiltering("precioUnitario.specified=true", "precioUnitario.specified=false");
    }

    @Test
    @Transactional
    void getAllDetalleVentasByPrecioUnitarioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where precioUnitario is greater than or equal to
        defaultDetalleVentaFiltering(
            "precioUnitario.greaterThanOrEqual=" + DEFAULT_PRECIO_UNITARIO,
            "precioUnitario.greaterThanOrEqual=" + UPDATED_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleVentasByPrecioUnitarioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where precioUnitario is less than or equal to
        defaultDetalleVentaFiltering(
            "precioUnitario.lessThanOrEqual=" + DEFAULT_PRECIO_UNITARIO,
            "precioUnitario.lessThanOrEqual=" + SMALLER_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleVentasByPrecioUnitarioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where precioUnitario is less than
        defaultDetalleVentaFiltering(
            "precioUnitario.lessThan=" + UPDATED_PRECIO_UNITARIO,
            "precioUnitario.lessThan=" + DEFAULT_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleVentasByPrecioUnitarioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where precioUnitario is greater than
        defaultDetalleVentaFiltering(
            "precioUnitario.greaterThan=" + SMALLER_PRECIO_UNITARIO,
            "precioUnitario.greaterThan=" + DEFAULT_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleVentasBySubtotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where subtotal equals to
        defaultDetalleVentaFiltering("subtotal.equals=" + DEFAULT_SUBTOTAL, "subtotal.equals=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleVentasBySubtotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where subtotal in
        defaultDetalleVentaFiltering("subtotal.in=" + DEFAULT_SUBTOTAL + "," + UPDATED_SUBTOTAL, "subtotal.in=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleVentasBySubtotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where subtotal is not null
        defaultDetalleVentaFiltering("subtotal.specified=true", "subtotal.specified=false");
    }

    @Test
    @Transactional
    void getAllDetalleVentasBySubtotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where subtotal is greater than or equal to
        defaultDetalleVentaFiltering("subtotal.greaterThanOrEqual=" + DEFAULT_SUBTOTAL, "subtotal.greaterThanOrEqual=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleVentasBySubtotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where subtotal is less than or equal to
        defaultDetalleVentaFiltering("subtotal.lessThanOrEqual=" + DEFAULT_SUBTOTAL, "subtotal.lessThanOrEqual=" + SMALLER_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleVentasBySubtotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where subtotal is less than
        defaultDetalleVentaFiltering("subtotal.lessThan=" + UPDATED_SUBTOTAL, "subtotal.lessThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleVentasBySubtotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList where subtotal is greater than
        defaultDetalleVentaFiltering("subtotal.greaterThan=" + SMALLER_SUBTOTAL, "subtotal.greaterThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleVentasByVentaIsEqualToSomething() throws Exception {
        Venta venta;
        if (TestUtil.findAll(em, Venta.class).isEmpty()) {
            detalleVentaRepository.saveAndFlush(detalleVenta);
            venta = VentaResourceIT.createEntity();
        } else {
            venta = TestUtil.findAll(em, Venta.class).get(0);
        }
        em.persist(venta);
        em.flush();
        detalleVenta.setVenta(venta);
        detalleVentaRepository.saveAndFlush(detalleVenta);
        Long ventaId = venta.getId();
        // Get all the detalleVentaList where venta equals to ventaId
        defaultDetalleVentaShouldBeFound("ventaId.equals=" + ventaId);

        // Get all the detalleVentaList where venta equals to (ventaId + 1)
        defaultDetalleVentaShouldNotBeFound("ventaId.equals=" + (ventaId + 1));
    }

    @Test
    @Transactional
    void getAllDetalleVentasByProductoIsEqualToSomething() throws Exception {
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            detalleVentaRepository.saveAndFlush(detalleVenta);
            producto = ProductoResourceIT.createEntity();
        } else {
            producto = TestUtil.findAll(em, Producto.class).get(0);
        }
        em.persist(producto);
        em.flush();
        detalleVenta.setProducto(producto);
        detalleVentaRepository.saveAndFlush(detalleVenta);
        Long productoId = producto.getId();
        // Get all the detalleVentaList where producto equals to productoId
        defaultDetalleVentaShouldBeFound("productoId.equals=" + productoId);

        // Get all the detalleVentaList where producto equals to (productoId + 1)
        defaultDetalleVentaShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    private void defaultDetalleVentaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDetalleVentaShouldBeFound(shouldBeFound);
        defaultDetalleVentaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDetalleVentaShouldBeFound(String filter) throws Exception {
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalleVenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(sameNumber(DEFAULT_PRECIO_UNITARIO))))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(sameNumber(DEFAULT_SUBTOTAL))));

        // Check, that the count call also returns 1
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDetalleVentaShouldNotBeFound(String filter) throws Exception {
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDetalleVenta() throws Exception {
        // Get the detalleVenta
        restDetalleVentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDetalleVenta() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detalleVenta
        DetalleVenta updatedDetalleVenta = detalleVentaRepository.findById(detalleVenta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDetalleVenta are not directly saved in db
        em.detach(updatedDetalleVenta);
        updatedDetalleVenta.cantidad(UPDATED_CANTIDAD).precioUnitario(UPDATED_PRECIO_UNITARIO).subtotal(UPDATED_SUBTOTAL);
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(updatedDetalleVenta);

        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detalleVentaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detalleVentaDTO))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDetalleVentaToMatchAllProperties(updatedDetalleVenta);
    }

    @Test
    @Transactional
    void putNonExistingDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detalleVentaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detalleVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detalleVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetalleVentaWithPatch() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detalleVenta using partial update
        DetalleVenta partialUpdatedDetalleVenta = new DetalleVenta();
        partialUpdatedDetalleVenta.setId(detalleVenta.getId());

        partialUpdatedDetalleVenta.cantidad(UPDATED_CANTIDAD).precioUnitario(UPDATED_PRECIO_UNITARIO);

        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDetalleVenta))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDetalleVentaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDetalleVenta, detalleVenta),
            getPersistedDetalleVenta(detalleVenta)
        );
    }

    @Test
    @Transactional
    void fullUpdateDetalleVentaWithPatch() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detalleVenta using partial update
        DetalleVenta partialUpdatedDetalleVenta = new DetalleVenta();
        partialUpdatedDetalleVenta.setId(detalleVenta.getId());

        partialUpdatedDetalleVenta.cantidad(UPDATED_CANTIDAD).precioUnitario(UPDATED_PRECIO_UNITARIO).subtotal(UPDATED_SUBTOTAL);

        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDetalleVenta))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDetalleVentaUpdatableFieldsEquals(partialUpdatedDetalleVenta, getPersistedDetalleVenta(partialUpdatedDetalleVenta));
    }

    @Test
    @Transactional
    void patchNonExistingDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detalleVentaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(detalleVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(detalleVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetalleVenta() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the detalleVenta
        restDetalleVentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, detalleVenta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return detalleVentaRepository.count();
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

    protected DetalleVenta getPersistedDetalleVenta(DetalleVenta detalleVenta) {
        return detalleVentaRepository.findById(detalleVenta.getId()).orElseThrow();
    }

    protected void assertPersistedDetalleVentaToMatchAllProperties(DetalleVenta expectedDetalleVenta) {
        assertDetalleVentaAllPropertiesEquals(expectedDetalleVenta, getPersistedDetalleVenta(expectedDetalleVenta));
    }

    protected void assertPersistedDetalleVentaToMatchUpdatableProperties(DetalleVenta expectedDetalleVenta) {
        assertDetalleVentaAllUpdatablePropertiesEquals(expectedDetalleVenta, getPersistedDetalleVenta(expectedDetalleVenta));
    }
}
