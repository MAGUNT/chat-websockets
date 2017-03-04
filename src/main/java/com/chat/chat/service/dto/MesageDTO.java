package com.chat.chat.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Mesage entity.
 */
public class MesageDTO implements Serializable {

    private Long id;

    @NotNull
    private String mesage;

    @NotNull
    private ZonedDateTime date;

    private Long emisorId;

    private String emisorLogin;

    private Long eventoId;

    private String eventoName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }
    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Long getEmisorId() {
        return emisorId;
    }

    public void setEmisorId(Long userId) {
        this.emisorId = userId;
    }

    public String getEmisorLogin() {
        return emisorLogin;
    }

    public void setEmisorLogin(String userLogin) {
        this.emisorLogin = userLogin;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventId) {
        this.eventoId = eventId;
    }

    public String getEventoName() {
        return eventoName;
    }

    public void setEventoName(String eventName) {
        this.eventoName = eventName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MesageDTO mesageDTO = (MesageDTO) o;

        if ( ! Objects.equals(id, mesageDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MesageDTO{" +
            "id=" + id +
            ", mesage='" + mesage + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
