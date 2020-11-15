package com.gmart.api.core.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.entities.Post;
import com.gmart.api.core.entities.UserCore;
import com.gmart.api.core.repositories.PostRepository;
import com.gmart.api.core.security.JwtTokenProvider;
import com.gmart.api.core.services.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping( "post")
@Scope("session")
@Slf4j
public class PostController {

	
	@Autowired
	private JwtTokenProvider jwtProvider;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	PostRepository postRepository;
	
	@PostMapping("/add-new-post")
	@ResponseBody
	public Boolean addNewPost(@RequestBody Post post, HttpServletRequest request,
			HttpServletResponse response) {
		UserCore user = null;

		try {
			Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));
			user = this.accountService.loadUserById(userId);
			if (user != null) {
				this.postRepository.save(post);
			}else {
				return false;
			}
		}catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
		
		return true;
	}
}
