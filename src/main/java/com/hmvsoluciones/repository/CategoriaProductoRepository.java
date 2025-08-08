package com.hmvsoluciones.repository;

import com.hmvsoluciones.domain.CategoriaProducto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CategoriaProducto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long>, JpaSpecificationExecutor<CategoriaProducto> {}
