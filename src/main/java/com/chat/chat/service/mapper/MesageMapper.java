package com.chat.chat.service.mapper;

import com.chat.chat.domain.*;
import com.chat.chat.service.dto.MesageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Mesage and its DTO MesageDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface MesageMapper {

    @Mapping(source = "emisor.id", target = "emisorId")
    @Mapping(source = "emisor.login", target = "emisorLogin")
    @Mapping(source = "evento.id", target = "eventoId")
    @Mapping(source = "evento.name", target = "eventoName")
    MesageDTO mesageToMesageDTO(Mesage mesage);

    List<MesageDTO> mesagesToMesageDTOs(List<Mesage> mesages);

    @Mapping(source = "emisorId", target = "emisor")
    @Mapping(source = "eventoId", target = "evento")
    Mesage mesageDTOToMesage(MesageDTO mesageDTO);

    List<Mesage> mesageDTOsToMesages(List<MesageDTO> mesageDTOs);

    default Event eventFromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
