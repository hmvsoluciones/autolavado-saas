package com.hmvsoluciones.service;

import com.hmvsoluciones.domain.*; // for static metamodels
import com.hmvsoluciones.domain.Producto;
import com.hmvsoluciones.repository.ProductoRepository;
import com.hmvsoluciones.service.criteria.ProductoCriteria;
import com.hmvsoluciones.service.dto.ProductoDTO;
import com.hmvsoluciones.service.mapper.ProductoMapper;
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
 * Service for executing complex queries for {@link Producto} entities in the database.
 * The main input is a {@link ProductoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductoQueryService extends QueryService<Producto> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductoQueryService.class);

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    public ProductoQueryService(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductoDTO> findByCriteria(ProductoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.findAll(specification, page).map(productoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Producto> createSpecification(ProductoCriteria criteria) {
        Specification<Producto> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Producto_.id),
                buildStringSpecification(criteria.getNombre(), Producto_.nombre),
                buildStringSpecification(criteria.getDescripcion(), Producto_.descripcion),
                buildRangeSpecification(criteria.getPrecio(), Producto_.precio),
                buildRangeSpecification(criteria.getCantidad(), Producto_.cantidad),
                buildRangeSpecification(criteria.getFechaActualizacion(), Producto_.fechaActualizacion),
                buildSpecification(criteria.getActivo(), Producto_.activo),
                buildSpecification(criteria.getCategoriaProductoId(), root ->
                    root.join(Producto_.categoriaProducto, JoinType.LEFT).get(CategoriaProducto_.id)
                ),
                buildSpecification(criteria.getDetalleVentaId(), root ->
                    root.join(Producto_.detalleVentas, JoinType.LEFT).get(DetalleVenta_.id)
                )
            );
        }
        return specification;
    }
}
