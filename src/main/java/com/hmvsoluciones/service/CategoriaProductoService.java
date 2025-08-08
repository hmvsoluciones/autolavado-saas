package com.hmvsoluciones.service;

import com.hmvsoluciones.domain.CategoriaProducto;
import com.hmvsoluciones.repository.CategoriaProductoRepository;
import com.hmvsoluciones.service.dto.CategoriaProductoDTO;
import com.hmvsoluciones.service.mapper.CategoriaProductoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hmvsoluciones.domain.CategoriaProducto}.
 */
@Service
@Transactional
public class CategoriaProductoService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoriaProductoService.class);

    private final CategoriaProductoRepository categoriaProductoRepository;

    private final CategoriaProductoMapper categoriaProductoMapper;

    public CategoriaProductoService(
        CategoriaProductoRepository categoriaProductoRepository,
        CategoriaProductoMapper categoriaProductoMapper
    ) {
        this.categoriaProductoRepository = categoriaProductoRepository;
        this.categoriaProductoMapper = categoriaProductoMapper;
    }

    /**
     * Save a categoriaProducto.
     *
     * @param categoriaProductoDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoriaProductoDTO save(CategoriaProductoDTO categoriaProductoDTO) {
        LOG.debug("Request to save CategoriaProducto : {}", categoriaProductoDTO);
        CategoriaProducto categoriaProducto = categoriaProductoMapper.toEntity(categoriaProductoDTO);
        categoriaProducto = categoriaProductoRepository.save(categoriaProducto);
        return categoriaProductoMapper.toDto(categoriaProducto);
    }

    /**
     * Update a categoriaProducto.
     *
     * @param categoriaProductoDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoriaProductoDTO update(CategoriaProductoDTO categoriaProductoDTO) {
        LOG.debug("Request to update CategoriaProducto : {}", categoriaProductoDTO);
        CategoriaProducto categoriaProducto = categoriaProductoMapper.toEntity(categoriaProductoDTO);
        categoriaProducto = categoriaProductoRepository.save(categoriaProducto);
        return categoriaProductoMapper.toDto(categoriaProducto);
    }

    /**
     * Partially update a categoriaProducto.
     *
     * @param categoriaProductoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoriaProductoDTO> partialUpdate(CategoriaProductoDTO categoriaProductoDTO) {
        LOG.debug("Request to partially update CategoriaProducto : {}", categoriaProductoDTO);

        return categoriaProductoRepository
            .findById(categoriaProductoDTO.getId())
            .map(existingCategoriaProducto -> {
                categoriaProductoMapper.partialUpdate(existingCategoriaProducto, categoriaProductoDTO);

                return existingCategoriaProducto;
            })
            .map(categoriaProductoRepository::save)
            .map(categoriaProductoMapper::toDto);
    }

    /**
     * Get one categoriaProducto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoriaProductoDTO> findOne(Long id) {
        LOG.debug("Request to get CategoriaProducto : {}", id);
        return categoriaProductoRepository.findById(id).map(categoriaProductoMapper::toDto);
    }

    /**
     * Delete the categoriaProducto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoriaProducto : {}", id);
        categoriaProductoRepository.deleteById(id);
    }
}
