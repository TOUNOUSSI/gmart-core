package com.gmart.api.core.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.gmart.api.core.entities.UserCore;
import com.gmart.api.core.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	public UserCore addUserToFriendList(UserCore user, UserCore friend) {
		SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Sring security Authenticated user "+SecurityContextHolder.getContext().getAuthentication().getName());
        user.getFriends().add(friend);
        return userRepository.save(user);
	}

	public UserCore loadUserById(Long userId) {
		Optional<UserCore> userCore = userRepository.findById(userId);
		if (userCore != null && userCore.get() != null) {
			return userCore.get();

		} else {
			return null;
		}
	}


	@Override
	public UserCore loadUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public UserCore save(UserCore user) {
		return userRepository.saveAndFlush(user);
	}

	public UserCore update(UserCore user) {
		return userRepository.save(user);
	}
}
