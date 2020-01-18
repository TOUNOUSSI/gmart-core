package com.gmart.api.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gmart.api.core.entities.UserCore;

@Repository
public interface UserRepository extends JpaRepository<UserCore, Long> {

	public UserCore findByUsername(String username);

}
