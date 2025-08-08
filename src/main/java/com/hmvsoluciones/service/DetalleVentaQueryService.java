package com.hmvsoluciones.service;

import com.hmvsoluciones.domain.*; // for static metamodels
import com.hmvsoluciones.domain.DetalleVenta;
import com.hmvsoluciones.repository.DetalleVentaRepository;
import com.hmvsoluciones.service.criteria.DetalleVentaCriteria;
import com.hmvsoluciones.service.dto.DetalleVentaDTO;
import com.hmvsoluciones.service.mapper.DetalleVentaMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DetalleVenta} entities in the database.
 * The main input is a {@link DetalleVentaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DetalleVentaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DetalleVentaQueryService extends QueryService<DetalleVenta> {

    private static final Logger LOG = LoggerFactory.getLogger(DetalleVentaQueryService.class);

    private final DetalleVentaRepository detalleVentaRepository;

    private final DetalleVentaMapper detalleVentaMapper;

    public DetalleVentaQueryService(DetalleVentaRepository detalleVentaRepository, DetalleVentaMapper detalleVentaMapper) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.detalleVentaMapper = detalleVentaMapper;
    }

    /**
     * Return a {@link Page} of {@link DetalleVentaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DetalleVentaDTO> findByCriteria(DetalleVentaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DetalleVenta> specification = createSpecification(criteria);
        return detalleVentaRepository.findAll(specification, page).map(detalleVentaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DetalleVentaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DetalleVenta> specification = createSpecification(criteria);
        return detalleVentaRepository.count(specification);
    }

    /**
     * Function to convert {@link DetalleVentaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DetalleVenta> createSpecification(DetalleVentaCriteria criteria) {
        Specification<DetalleVenta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DetalleVenta_.id),
                buildRangeSpecification(criteria.getCantidad(), DetalleVenta_.cantidad),
                buildRangeSpecification(criteria.getPrecioUnitario(), DetalleVenta_.precioUnitario),
                buildRangeSpecification(criteria.getSubtotal(), DetalleVenta_.subtotal),
                buildSpecification(criteria.getVentaId(), root -> root.join(DetalleVenta_.venta, JoinType.LEFT).get(Venta_.id)),
                buildSpecification(criteria.getProductoId(), root -> root.join(DetalleVenta_.producto, JoinType.LEFT).get(Producto_.id))
            );
        }
        return specification;
    }
}
