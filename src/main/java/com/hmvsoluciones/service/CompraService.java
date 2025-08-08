package com.hmvsoluciones.service;

import com.hmvsoluciones.domain.Compra;
import com.hmvsoluciones.repository.CompraRepository;
import com.hmvsoluciones.service.dto.CompraDTO;
import com.hmvsoluciones.service.mapper.CompraMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hmvsoluciones.domain.Compra}.
 */
@Service
@Transactional
public class CompraService {

    private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);

    private final CompraRepository compraRepository;

    private final CompraMapper compraMapper;

    public CompraService(CompraRepository compraRepository, CompraMapper compraMapper) {
        this.compraRepository = compraRepository;
        this.compraMapper = compraMapper;
    }

    /**
     * Save a compra.
     *
     * @param compraDTO the entity to save.
     * @return the persisted entity.
     */
    public CompraDTO save(CompraDTO compraDTO) {
        LOG.debug("Request to save Compra : {}", compraDTO);
        Compra compra = compraMapper.toEntity(compraDTO);
        compra = compraRepository.save(compra);
        return compraMapper.toDto(compra);
    }

    /**
     * Update a compra.
     *
     * @param compraDTO the entity to save.
     * @return the persisted entity.
     */
    public CompraDTO update(CompraDTO compraDTO) {
        LOG.debug("Request to update Compra : {}", compraDTO);
        Compra compra = compraMapper.toEntity(compraDTO);
        compra = compraRepository.save(compra);
        return compraMapper.toDto(compra);
    }

    /**
     * Partially update a compra.
     *
     * @param compraDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompraDTO> partialUpdate(CompraDTO compraDTO) {
        LOG.debug("Request to partially update Compra : {}", compraDTO);

        return compraRepository
            .findById(compraDTO.getId())
            .map(existingCompra -> {
                compraMapper.partialUpdate(existingCompra, compraDTO);

                return existingCompra;
            })
            .map(compraRepository::save)
            .map(compraMapper::toDto);
    }

    /**
     * Get one compra by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompraDTO> findOne(Long id) {
        LOG.debug("Request to get Compra : {}", id);
        return compraRepository.findById(id).map(compraMapper::toDto);
    }

    /**
     * Delete the compra by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Compra : {}", id);
        compraRepository.deleteById(id);
    }
}
