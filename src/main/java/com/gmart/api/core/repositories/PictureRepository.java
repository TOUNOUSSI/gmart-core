package com.gmart.api.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gmart.api.core.entities.Picture;


@Repository
public interface PictureRepository extends JpaRepository<Picture, String> {


}
