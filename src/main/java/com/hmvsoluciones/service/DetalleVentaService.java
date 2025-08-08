package com.hmvsoluciones.service;

import com.hmvsoluciones.domain.DetalleVenta;
import com.hmvsoluciones.repository.DetalleVentaRepository;
import com.hmvsoluciones.service.dto.DetalleVentaDTO;
import com.hmvsoluciones.service.mapper.DetalleVentaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hmvsoluciones.domain.DetalleVenta}.
 */
@Service
@Transactional
public class DetalleVentaService {

    private static final Logger LOG = LoggerFactory.getLogger(DetalleVentaService.class);

    private final DetalleVentaRepository detalleVentaRepository;

    private final DetalleVentaMapper detalleVentaMapper;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository, DetalleVentaMapper detalleVentaMapper) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.detalleVentaMapper = detalleVentaMapper;
    }

    /**
     * Save a detalleVenta.
     *
     * @param detalleVentaDTO the entity to save.
     * @return the persisted entity.
     */
    public DetalleVentaDTO save(DetalleVentaDTO detalleVentaDTO) {
        LOG.debug("Request to save DetalleVenta : {}", detalleVentaDTO);
        DetalleVenta detalleVenta = detalleVentaMapper.toEntity(detalleVentaDTO);
        detalleVenta = detalleVentaRepository.save(detalleVenta);
        return detalleVentaMapper.toDto(detalleVenta);
    }

    /**
     * Update a detalleVenta.
     *
     * @param detalleVentaDTO the entity to save.
     * @return the persisted entity.
     */
    public DetalleVentaDTO update(DetalleVentaDTO detalleVentaDTO) {
        LOG.debug("Request to update DetalleVenta : {}", detalleVentaDTO);
        DetalleVenta detalleVenta = detalleVentaMapper.toEntity(detalleVentaDTO);
        detalleVenta = detalleVentaRepository.save(detalleVenta);
        return detalleVentaMapper.toDto(detalleVenta);
    }

    /**
     * Partially update a detalleVenta.
     *
     * @param detalleVentaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DetalleVentaDTO> partialUpdate(DetalleVentaDTO detalleVentaDTO) {
        LOG.debug("Request to partially update DetalleVenta : {}", detalleVentaDTO);

        return detalleVentaRepository
            .findById(detalleVentaDTO.getId())
            .map(existingDetalleVenta -> {
                detalleVentaMapper.partialUpdate(existingDetalleVenta, detalleVentaDTO);

                return existingDetalleVenta;
            })
            .map(detalleVentaRepository::save)
            .map(detalleVentaMapper::toDto);
    }

    /**
     * Get one detalleVenta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DetalleVentaDTO> findOne(Long id) {
        LOG.debug("Request to get DetalleVenta : {}", id);
        return detalleVentaRepository.findById(id).map(detalleVentaMapper::toDto);
    }

    /**
     * Delete the detalleVenta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DetalleVenta : {}", id);
        detalleVentaRepository.deleteById(id);
    }
}
