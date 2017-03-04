package com.chat.chat.service;

import com.chat.chat.domain.Mesage;
import com.chat.chat.domain.User;
import com.chat.chat.repository.MesageRepository;
import com.chat.chat.security.SecurityUtils;
import com.chat.chat.service.dto.MesageDTO;
import com.chat.chat.service.mapper.MesageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Mesage.
 */
@Service
@Transactional
public class MesageService {

    private final Logger log = LoggerFactory.getLogger(MesageService.class);

    private final MesageRepository mesageRepository;

    private final UserService userService;

    private final MesageMapper mesageMapper;

    public MesageService(MesageRepository mesageRepository, MesageMapper mesageMapper, UserService userService) {
        this.mesageRepository = mesageRepository;
        this.mesageMapper = mesageMapper;
        this.userService = userService;
    }

    /**
     * Save a mesage.
     *
     * @param mesageDTO the entity to save
     * @return the persisted entity
     */
    public MesageDTO save(MesageDTO mesageDTO) {
        log.debug("Request to save Mesage : {}", mesageDTO);
        Mesage mesage = mesageMapper.mesageDTOToMesage(mesageDTO);
        mesage = mesageRepository.save(mesage);
        MesageDTO result = mesageMapper.mesageToMesageDTO(mesage);
        return result;
    }

    public MesageDTO saveMessageFromCurrentUser(MesageDTO mesageDTO) {
        User current = userService.getUserWithAuthorities();
        mesageDTO.setEmisorId(current.getId());
        return save(mesageDTO);
    }


    /**
     *  Get all the mesages.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MesageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mesages");
        Page<Mesage> result = mesageRepository.findAll(pageable);
        return result.map(mesage -> mesageMapper.mesageToMesageDTO(mesage));
    }

    /**
     *  Get one mesage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MesageDTO findOne(Long id) {
        log.debug("Request to get Mesage : {}", id);
        Mesage mesage = mesageRepository.findOne(id);
        MesageDTO mesageDTO = mesageMapper.mesageToMesageDTO(mesage);
        return mesageDTO;
    }

    /**
     *  Delete the  mesage by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Mesage : {}", id);
        mesageRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<MesageDTO> findEventMessages(Long id, Pageable page) {
        return mesageRepository.findByEventoId(id, page)
            .map(mesageMapper::mesageToMesageDTO);
    }
}
