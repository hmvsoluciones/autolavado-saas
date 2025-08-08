package com.hmvsoluciones.service;

import com.hmvsoluciones.domain.*; // for static metamodels
import com.hmvsoluciones.domain.CategoriaProducto;
import com.hmvsoluciones.repository.CategoriaProductoRepository;
import com.hmvsoluciones.service.criteria.CategoriaProductoCriteria;
import com.hmvsoluciones.service.dto.CategoriaProductoDTO;
import com.hmvsoluciones.service.mapper.CategoriaProductoMapper;
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
 * Service for executing complex queries for {@link CategoriaProducto} entities in the database.
 * The main input is a {@link CategoriaProductoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CategoriaProductoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoriaProductoQueryService extends QueryService<CategoriaProducto> {

    private static final Logger LOG = LoggerFactory.getLogger(CategoriaProductoQueryService.class);

    private final CategoriaProductoRepository categoriaProductoRepository;

    private final CategoriaProductoMapper categoriaProductoMapper;

    public CategoriaProductoQueryService(
        CategoriaProductoRepository categoriaProductoRepository,
        CategoriaProductoMapper categoriaProductoMapper
    ) {
        this.categoriaProductoRepository = categoriaProductoRepository;
        this.categoriaProductoMapper = categoriaProductoMapper;
    }

    /**
     * Return a {@link Page} of {@link CategoriaProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoriaProductoDTO> findByCriteria(CategoriaProductoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategoriaProducto> specification = createSpecification(criteria);
        return categoriaProductoRepository.findAll(specification, page).map(categoriaProductoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoriaProductoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CategoriaProducto> specification = createSpecification(criteria);
        return categoriaProductoRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoriaProductoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategoriaProducto> createSpecification(CategoriaProductoCriteria criteria) {
        Specification<CategoriaProducto> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CategoriaProducto_.id),
                buildStringSpecification(criteria.getNombre(), CategoriaProducto_.nombre),
                buildStringSpecification(criteria.getDescripcion(), CategoriaProducto_.descripcion),
                buildSpecification(criteria.getProductoId(), root ->
                    root.join(CategoriaProducto_.productos, JoinType.LEFT).get(Producto_.id)
                )
            );
        }
        return specification;
    }
}
