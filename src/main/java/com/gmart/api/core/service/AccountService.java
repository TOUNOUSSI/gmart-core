package com.gmart.api.core.service;

import java.util.Collection;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.domain.UserProfile;
import com.gmart.api.core.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class AccountService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Transactional(readOnly = true,rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class, isolation = Isolation.READ_COMMITTED)
	public UserProfile loadUserById(String id) {
		return this.userRepository.getOne(id);
	}

	@Transactional(readOnly = true,rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class, isolation = Isolation.READ_COMMITTED)
	public Collection<UserProfile> getAllUsers() {
		return this.userRepository.findAll();
	}

	@Transactional(readOnly = true,rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class, isolation = Isolation.READ_COMMITTED)
	public Collection<UserProfile> getMatchingUsersList(String criteria) {
		return this.userRepository.findByUsernameContainingIgnoreCase(criteria);
	}

	@Override
	public UserProfile loadUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public UserProfile loadUserByProfile(Profile profile) {
		return userRepository.findByProfile(profile);
	}

	public UserProfile save(UserProfile user) {
		return userRepository.saveAndFlush(user);
	}

	public UserProfile update(UserProfile user) {
		return userRepository.save(user);
	}

}
