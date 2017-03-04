package com.chat.chat.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Mesage.
 */
@Entity
@Table(name = "mesage")
public class Mesage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "mesage", nullable = false)
    private String mesage;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @ManyToOne
    private User emisor;

    @ManyToOne
    private Event evento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMesage() {
        return mesage;
    }

    public Mesage mesage(String mesage) {
        this.mesage = mesage;
        return this;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Mesage date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public User getEmisor() {
        return emisor;
    }

    public Mesage emisor(User user) {
        this.emisor = user;
        return this;
    }

    public void setEmisor(User user) {
        this.emisor = user;
    }

    public Event getEvento() {
        return evento;
    }

    public Mesage evento(Event event) {
        this.evento = event;
        return this;
    }

    public void setEvento(Event event) {
        this.evento = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mesage mesage = (Mesage) o;
        if (mesage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, mesage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Mesage{" +
            "id=" + id +
            ", mesage='" + mesage + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
