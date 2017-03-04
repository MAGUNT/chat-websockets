package com.chat.chat.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.chat.chat.service.MesageService;
import com.chat.chat.web.rest.util.HeaderUtil;
import com.chat.chat.web.rest.util.PaginationUtil;
import com.chat.chat.service.dto.MesageDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
 * REST controller for managing Mesage.
 */
@RestController
@RequestMapping("/api")
public class MesageResource {

    private final Logger log = LoggerFactory.getLogger(MesageResource.class);

    private static final String ENTITY_NAME = "mesage";

    private final MesageService mesageService;

    public MesageResource(MesageService mesageService) {
        this.mesageService = mesageService;
    }

    /**
     * POST  /mesages : Create a new mesage.
     *
     * @param mesageDTO the mesageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mesageDTO, or with status 400 (Bad Request) if the mesage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mesages")
    @Timed
    public ResponseEntity<MesageDTO> createMesage(@Valid @RequestBody MesageDTO mesageDTO) throws URISyntaxException {
        log.debug("REST request to save Mesage : {}", mesageDTO);
        if (mesageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new mesage cannot already have an ID")).body(null);
        }
        MesageDTO result = mesageService.save(mesageDTO);
        return ResponseEntity.created(new URI("/api/mesages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mesages : Updates an existing mesage.
     *
     * @param mesageDTO the mesageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mesageDTO,
     * or with status 400 (Bad Request) if the mesageDTO is not valid,
     * or with status 500 (Internal Server Error) if the mesageDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mesages")
    @Timed
    public ResponseEntity<MesageDTO> updateMesage(@Valid @RequestBody MesageDTO mesageDTO) throws URISyntaxException {
        log.debug("REST request to update Mesage : {}", mesageDTO);
        if (mesageDTO.getId() == null) {
            return createMesage(mesageDTO);
        }
        MesageDTO result = mesageService.save(mesageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mesageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mesages : get all the mesages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mesages in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/mesages")
    @Timed
    public ResponseEntity<List<MesageDTO>> getAllMesages(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Mesages");
        Page<MesageDTO> page = mesageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mesages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mesages/:id : get the "id" mesage.
     *
     * @param id the id of the mesageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mesageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/mesages/{id}")
    @Timed
    public ResponseEntity<MesageDTO> getMesage(@PathVariable Long id) {
        log.debug("REST request to get Mesage : {}", id);
        MesageDTO mesageDTO = mesageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mesageDTO));
    }

    /**
     * DELETE  /mesages/:id : delete the "id" mesage.
     *
     * @param id the id of the mesageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mesages/{id}")
    @Timed
    public ResponseEntity<Void> deleteMesage(@PathVariable Long id) {
        log.debug("REST request to delete Mesage : {}", id);
        mesageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/mesages/event/{idEvent}")
    @Timed
    public ResponseEntity<List<MesageDTO>> getMesageByEvent(@PathVariable Long idEvent,
                                                            @PageableDefault(page= 0, value = Integer.MAX_VALUE)
                                                            @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get mesages from Event : {}", idEvent);
        Page<MesageDTO> page = mesageService.findEventMessages(idEvent, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mesages/event");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
