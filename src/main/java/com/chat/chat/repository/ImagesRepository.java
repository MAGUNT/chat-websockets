package com.chat.chat.repository;

import com.chat.chat.domain.Images;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Images entity.
 */
@SuppressWarnings("unused")
public interface ImagesRepository extends JpaRepository<Images,Long> {

}
