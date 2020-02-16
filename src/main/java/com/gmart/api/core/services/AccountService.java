package com.gmart.api.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmart.api.core.entities.UserCore;
import com.gmart.api.core.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {
	@Autowired
	UserRepository userRepository;

	public List<UserCore> getAll(){
		return this.userRepository.findAll();
	}
}
