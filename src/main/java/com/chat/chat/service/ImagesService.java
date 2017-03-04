package com.chat.chat.service;

import com.chat.chat.domain.Images;
import com.chat.chat.repository.ImagesRepository;
import com.chat.chat.service.dto.ImagesDTO;
import com.chat.chat.service.mapper.ImagesMapper;
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
 * Service Implementation for managing Images.
 */
@Service
@Transactional
public class ImagesService {

    private final Logger log = LoggerFactory.getLogger(ImagesService.class);
    
    private final ImagesRepository imagesRepository;

    private final ImagesMapper imagesMapper;

    public ImagesService(ImagesRepository imagesRepository, ImagesMapper imagesMapper) {
        this.imagesRepository = imagesRepository;
        this.imagesMapper = imagesMapper;
    }

    /**
     * Save a images.
     *
     * @param imagesDTO the entity to save
     * @return the persisted entity
     */
    public ImagesDTO save(ImagesDTO imagesDTO) {
        log.debug("Request to save Images : {}", imagesDTO);
        Images images = imagesMapper.imagesDTOToImages(imagesDTO);
        images = imagesRepository.save(images);
        ImagesDTO result = imagesMapper.imagesToImagesDTO(images);
        return result;
    }

    /**
     *  Get all the images.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ImagesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Images");
        Page<Images> result = imagesRepository.findAll(pageable);
        return result.map(images -> imagesMapper.imagesToImagesDTO(images));
    }

    /**
     *  Get one images by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ImagesDTO findOne(Long id) {
        log.debug("Request to get Images : {}", id);
        Images images = imagesRepository.findOne(id);
        ImagesDTO imagesDTO = imagesMapper.imagesToImagesDTO(images);
        return imagesDTO;
    }

    /**
     *  Delete the  images by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Images : {}", id);
        imagesRepository.delete(id);
    }
}
