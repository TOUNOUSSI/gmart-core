package com.gmart.api.core.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gmart.api.core.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

}
