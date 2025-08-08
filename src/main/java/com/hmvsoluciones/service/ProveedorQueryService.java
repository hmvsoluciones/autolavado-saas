package com.hmvsoluciones.service;

import com.hmvsoluciones.domain.*; // for static metamodels
import com.hmvsoluciones.domain.Proveedor;
import com.hmvsoluciones.repository.ProveedorRepository;
import com.hmvsoluciones.service.criteria.ProveedorCriteria;
import com.hmvsoluciones.service.dto.ProveedorDTO;
import com.hmvsoluciones.service.mapper.ProveedorMapper;
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
 * Service for executing complex queries for {@link Proveedor} entities in the database.
 * The main input is a {@link ProveedorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProveedorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProveedorQueryService extends QueryService<Proveedor> {

    private static final Logger LOG = LoggerFactory.getLogger(ProveedorQueryService.class);

    private final ProveedorRepository proveedorRepository;

    private final ProveedorMapper proveedorMapper;

    public ProveedorQueryService(ProveedorRepository proveedorRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

    /**
     * Return a {@link Page} of {@link ProveedorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProveedorDTO> findByCriteria(ProveedorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Proveedor> specification = createSpecification(criteria);
        return proveedorRepository.findAll(specification, page).map(proveedorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProveedorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Proveedor> specification = createSpecification(criteria);
        return proveedorRepository.count(specification);
    }

    /**
     * Function to convert {@link ProveedorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Proveedor> createSpecification(ProveedorCriteria criteria) {
        Specification<Proveedor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Proveedor_.id),
                buildStringSpecification(criteria.getNombre(), Proveedor_.nombre),
                buildStringSpecification(criteria.getEmail(), Proveedor_.email),
                buildStringSpecification(criteria.getTelefono(), Proveedor_.telefono),
                buildStringSpecification(criteria.getRazonSocial(), Proveedor_.razonSocial),
                buildStringSpecification(criteria.getRfc(), Proveedor_.rfc),
                buildSpecification(criteria.getActivo(), Proveedor_.activo),
                buildSpecification(criteria.getCompraId(), root -> root.join(Proveedor_.compras, JoinType.LEFT).get(Compra_.id))
            );
        }
        return specification;
    }
}
