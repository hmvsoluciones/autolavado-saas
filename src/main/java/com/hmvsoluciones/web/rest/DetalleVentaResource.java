package com.hmvsoluciones.web.rest;

import com.hmvsoluciones.repository.DetalleVentaRepository;
import com.hmvsoluciones.service.DetalleVentaQueryService;
import com.hmvsoluciones.service.DetalleVentaService;
import com.hmvsoluciones.service.criteria.DetalleVentaCriteria;
import com.hmvsoluciones.service.dto.DetalleVentaDTO;
import com.hmvsoluciones.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hmvsoluciones.domain.DetalleVenta}.
 */
@RestController
@RequestMapping("/api/detalle-ventas")
public class DetalleVentaResource {

    private static final Logger LOG = LoggerFactory.getLogger(DetalleVentaResource.class);

    private static final String ENTITY_NAME = "detalleVenta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetalleVentaService detalleVentaService;

    private final DetalleVentaRepository detalleVentaRepository;

    private final DetalleVentaQueryService detalleVentaQueryService;

    public DetalleVentaResource(
        DetalleVentaService detalleVentaService,
        DetalleVentaRepository detalleVentaRepository,
        DetalleVentaQueryService detalleVentaQueryService
    ) {
        this.detalleVentaService = detalleVentaService;
        this.detalleVentaRepository = detalleVentaRepository;
        this.detalleVentaQueryService = detalleVentaQueryService;
    }

    /**
     * {@code POST  /detalle-ventas} : Create a new detalleVenta.
     *
     * @param detalleVentaDTO the detalleVentaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detalleVentaDTO, or with status {@code 400 (Bad Request)} if the detalleVenta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DetalleVentaDTO> createDetalleVenta(@Valid @RequestBody DetalleVentaDTO detalleVentaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DetalleVenta : {}", detalleVentaDTO);
        if (detalleVentaDTO.getId() != null) {
            throw new BadRequestAlertException("A new detalleVenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        detalleVentaDTO = detalleVentaService.save(detalleVentaDTO);
        return ResponseEntity.created(new URI("/api/detalle-ventas/" + detalleVentaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, detalleVentaDTO.getId().toString()))
            .body(detalleVentaDTO);
    }

    /**
     * {@code PUT  /detalle-ventas/:id} : Updates an existing detalleVenta.
     *
     * @param id the id of the detalleVentaDTO to save.
     * @param detalleVentaDTO the detalleVentaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleVentaDTO,
     * or with status {@code 400 (Bad Request)} if the detalleVentaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detalleVentaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DetalleVentaDTO> updateDetalleVenta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DetalleVentaDTO detalleVentaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DetalleVenta : {}, {}", id, detalleVentaDTO);
        if (detalleVentaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleVentaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detalleVentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        detalleVentaDTO = detalleVentaService.update(detalleVentaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, detalleVentaDTO.getId().toString()))
            .body(detalleVentaDTO);
    }

    /**
     * {@code PATCH  /detalle-ventas/:id} : Partial updates given fields of an existing detalleVenta, field will ignore if it is null
     *
     * @param id the id of the detalleVentaDTO to save.
     * @param detalleVentaDTO the detalleVentaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleVentaDTO,
     * or with status {@code 400 (Bad Request)} if the detalleVentaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the detalleVentaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the detalleVentaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetalleVentaDTO> partialUpdateDetalleVenta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DetalleVentaDTO detalleVentaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DetalleVenta partially : {}, {}", id, detalleVentaDTO);
        if (detalleVentaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleVentaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detalleVentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetalleVentaDTO> result = detalleVentaService.partialUpdate(detalleVentaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, detalleVentaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /detalle-ventas} : get all the detalleVentas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detalleVentas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DetalleVentaDTO>> getAllDetalleVentas(
        DetalleVentaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DetalleVentas by criteria: {}", criteria);

        Page<DetalleVentaDTO> page = detalleVentaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detalle-ventas/count} : count all the detalleVentas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDetalleVentas(DetalleVentaCriteria criteria) {
        LOG.debug("REST request to count DetalleVentas by criteria: {}", criteria);
        return ResponseEntity.ok().body(detalleVentaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /detalle-ventas/:id} : get the "id" detalleVenta.
     *
     * @param id the id of the detalleVentaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detalleVentaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaDTO> getDetalleVenta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DetalleVenta : {}", id);
        Optional<DetalleVentaDTO> detalleVentaDTO = detalleVentaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detalleVentaDTO);
    }

    /**
     * {@code DELETE  /detalle-ventas/:id} : delete the "id" detalleVenta.
     *
     * @param id the id of the detalleVentaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetalleVenta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DetalleVenta : {}", id);
        detalleVentaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
