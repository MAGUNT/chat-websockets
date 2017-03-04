package com.chat.chat.repository;

import com.chat.chat.domain.Mesage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;



import java.util.List;

/**
 * Spring Data JPA repository for the Mesage entity.
 */
@SuppressWarnings("unused")
public interface MesageRepository extends JpaRepository<Mesage,Long> {

    @Query("select mesage from Mesage mesage where mesage.emisor.login = ?#{principal.username}")
    List<Mesage> findByEmisorIsCurrentUser();
    Page<Mesage> findByEventoId(Long id, Pageable page);
}
