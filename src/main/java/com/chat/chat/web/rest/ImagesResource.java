package com.chat.chat.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.chat.chat.service.ImagesService;
import com.chat.chat.web.rest.util.HeaderUtil;
import com.chat.chat.web.rest.util.PaginationUtil;
import com.chat.chat.service.dto.ImagesDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Images.
 */
@RestController
@RequestMapping("/api")
public class ImagesResource {

    private final Logger log = LoggerFactory.getLogger(ImagesResource.class);

    private static final String ENTITY_NAME = "images";
        
    private final ImagesService imagesService;

    public ImagesResource(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    /**
     * POST  /images : Create a new images.
     *
     * @param imagesDTO the imagesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new imagesDTO, or with status 400 (Bad Request) if the images has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/images")
    @Timed
    public ResponseEntity<ImagesDTO> createImages(@Valid @RequestBody ImagesDTO imagesDTO) throws URISyntaxException {
        log.debug("REST request to save Images : {}", imagesDTO);
        if (imagesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new images cannot already have an ID")).body(null);
        }
        ImagesDTO result = imagesService.save(imagesDTO);
        return ResponseEntity.created(new URI("/api/images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /images : Updates an existing images.
     *
     * @param imagesDTO the imagesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated imagesDTO,
     * or with status 400 (Bad Request) if the imagesDTO is not valid,
     * or with status 500 (Internal Server Error) if the imagesDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/images")
    @Timed
    public ResponseEntity<ImagesDTO> updateImages(@Valid @RequestBody ImagesDTO imagesDTO) throws URISyntaxException {
        log.debug("REST request to update Images : {}", imagesDTO);
        if (imagesDTO.getId() == null) {
            return createImages(imagesDTO);
        }
        ImagesDTO result = imagesService.save(imagesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, imagesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /images : get all the images.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of images in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/images")
    @Timed
    public ResponseEntity<List<ImagesDTO>> getAllImages(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Images");
        Page<ImagesDTO> page = imagesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/images");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /images/:id : get the "id" images.
     *
     * @param id the id of the imagesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the imagesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/images/{id}")
    @Timed
    public ResponseEntity<ImagesDTO> getImages(@PathVariable Long id) {
        log.debug("REST request to get Images : {}", id);
        ImagesDTO imagesDTO = imagesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(imagesDTO));
    }

    /**
     * DELETE  /images/:id : delete the "id" images.
     *
     * @param id the id of the imagesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/images/{id}")
    @Timed
    public ResponseEntity<Void> deleteImages(@PathVariable Long id) {
        log.debug("REST request to delete Images : {}", id);
        imagesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
