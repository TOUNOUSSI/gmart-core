package com.gmart.api.core.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.entities.UserCore;
import com.gmart.api.core.security.JwtTokenProvider;
import com.gmart.api.core.services.AuthService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("messenger")
@Scope("session")
@Slf4j
public class FriendController {

	@Autowired
	AuthService authService;

	@Autowired
	JwtTokenProvider jwtProvider;

	@GetMapping("/add-new-friend/{username}")
	@ResponseBody
	public Boolean addUserToFrienList(@PathVariable("username") String username, HttpServletRequest request,
			HttpServletResponse response) {

		UserCore friendToBeAdd = null;
		UserCore user = null;
		log.info("username found : " + username);
		try {
			Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));
			user = this.authService.loadUserById(userId);
			log.info("Authenticated user found : " + username);

			friendToBeAdd = this.authService.loadUserByUsername(username);
			if (friendToBeAdd != null) {
				log.info("Friend user to be added : " + friendToBeAdd);
				log.info("Founded user in the session " + user.toString());
				this.authService.addUserToFriendList(user, friendToBeAdd);
				log.info("Updating friend list finished");
				log.info("######### FRIEND LIST of " + user.getUsername() + " #########");
				user.getFriends().forEach(f -> {
					log.info("Friend  : " + f.toString());
				});

			} else {
				return false;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}

		return true;
	}

	@GetMapping("/myfriends")
	@ResponseBody
	public List<UserCore> getFriendList( HttpServletRequest request,HttpServletResponse response) {

		List<UserCore> friends = null;
		UserCore user = null;
		try {
			log.info("get Friend List started here "+request.getHeader("Token"));
			Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));
			user = this.authService.loadUserById(userId);
			friends = user.getFriends().stream().distinct().collect(Collectors.toList());

		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return friends;
	}
}
