package com.hmvsoluciones.service;

import com.hmvsoluciones.domain.Proveedor;
import com.hmvsoluciones.repository.ProveedorRepository;
import com.hmvsoluciones.service.dto.ProveedorDTO;
import com.hmvsoluciones.service.mapper.ProveedorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hmvsoluciones.domain.Proveedor}.
 */
@Service
@Transactional
public class ProveedorService {

    private static final Logger LOG = LoggerFactory.getLogger(ProveedorService.class);

    private final ProveedorRepository proveedorRepository;

    private final ProveedorMapper proveedorMapper;

    public ProveedorService(ProveedorRepository proveedorRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

    /**
     * Save a proveedor.
     *
     * @param proveedorDTO the entity to save.
     * @return the persisted entity.
     */
    public ProveedorDTO save(ProveedorDTO proveedorDTO) {
        LOG.debug("Request to save Proveedor : {}", proveedorDTO);
        Proveedor proveedor = proveedorMapper.toEntity(proveedorDTO);
        proveedor = proveedorRepository.save(proveedor);
        return proveedorMapper.toDto(proveedor);
    }

    /**
     * Update a proveedor.
     *
     * @param proveedorDTO the entity to save.
     * @return the persisted entity.
     */
    public ProveedorDTO update(ProveedorDTO proveedorDTO) {
        LOG.debug("Request to update Proveedor : {}", proveedorDTO);
        Proveedor proveedor = proveedorMapper.toEntity(proveedorDTO);
        proveedor = proveedorRepository.save(proveedor);
        return proveedorMapper.toDto(proveedor);
    }

    /**
     * Partially update a proveedor.
     *
     * @param proveedorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProveedorDTO> partialUpdate(ProveedorDTO proveedorDTO) {
        LOG.debug("Request to partially update Proveedor : {}", proveedorDTO);

        return proveedorRepository
            .findById(proveedorDTO.getId())
            .map(existingProveedor -> {
                proveedorMapper.partialUpdate(existingProveedor, proveedorDTO);

                return existingProveedor;
            })
            .map(proveedorRepository::save)
            .map(proveedorMapper::toDto);
    }

    /**
     * Get one proveedor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProveedorDTO> findOne(Long id) {
        LOG.debug("Request to get Proveedor : {}", id);
        return proveedorRepository.findById(id).map(proveedorMapper::toDto);
    }

    /**
     * Delete the proveedor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Proveedor : {}", id);
        proveedorRepository.deleteById(id);
    }
}
