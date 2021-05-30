package com.gmart.api.core.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.domain.UserProfile;
import com.gmart.api.core.repositories.UserRepository;

import lombok.Data;

@Data
@Service
public class AccountService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	public UserProfile loadUserById(String id) {
		return this.userRepository.getOne(id);
	}

	public Collection<UserProfile> getAllUsers() {
		return this.userRepository.findAll();
	}

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
