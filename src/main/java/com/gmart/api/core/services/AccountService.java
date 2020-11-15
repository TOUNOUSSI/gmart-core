package com.gmart.api.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.gmart.api.core.entities.Profile;
import com.gmart.api.core.entities.UserCore;
import com.gmart.api.core.repositories.UserRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class AccountService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	public UserCore loadUserById(Long id) {
		return this.userRepository.getOne(id);
	}
	
	public List<UserCore> getAll(){
		return this.userRepository.findAll();
	}
	
	public List<UserCore> getMatchingAccountList(String criteria){
		return this.userRepository.findByUsernameContainingIgnoreCase(criteria);
	}
	
	public UserCore loadUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public UserCore loadUserByProfile(Profile profile) {
		return userRepository.findByProfile(profile);
	}

	public UserCore save(UserCore user) {
		return userRepository.saveAndFlush(user);
	}

	public UserCore update(UserCore user) {
		return userRepository.save(user);
	}
	
}
