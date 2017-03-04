package com.chat.chat.service.mapper;

import com.chat.chat.domain.*;
import com.chat.chat.service.dto.ImagesDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Images and its DTO ImagesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ImagesMapper {

    ImagesDTO imagesToImagesDTO(Images images);

    List<ImagesDTO> imagesToImagesDTOs(List<Images> images);

    Images imagesDTOToImages(ImagesDTO imagesDTO);

    List<Images> imagesDTOsToImages(List<ImagesDTO> imagesDTOs);
}
