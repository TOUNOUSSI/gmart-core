package com.gmart.api.core.controllers;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.entities.Profile;
import com.gmart.api.core.entities.UserCore;
import com.gmart.api.core.security.JwtTokenProvider;
import com.gmart.api.core.services.AccountService;
import com.gmart.api.core.services.ProfileService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("friend")
@Scope("session")
@Slf4j
@Data
public class FriendController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private JwtTokenProvider jwtProvider;

	@PutMapping("/add-new-friend/{pseudoname}")
	@ResponseBody
	public Boolean addUserToFrienList(@PathVariable("pseudoname") String pseudoname, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("Add new controller: " + pseudoname);

		UserCore friendToBeAdd = null;
		UserCore user = null;
		Profile profile = this.profileService.getProfileByPseudoname(pseudoname);

		try {
			if (profile != null) {
				Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));
				user = this.accountService.loadUserById(userId);
				if (user != null) {
					friendToBeAdd = this.accountService.loadUserByProfile(profile);
					if (friendToBeAdd != null) {
						user.getFriends().add(friendToBeAdd);
						this.accountService.update(user);
						log.info("Updating friend list finished");

					} else {
						return false;
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}

		return true;
	}

	@GetMapping("/myfriends")
	@ResponseBody
	public List<UserCore> getFriendList(HttpServletRequest request, HttpServletResponse response) {
		List<UserCore> friends=null;
		UserCore user = null;
		try {
			log.info("get Friend List started here " + request.getHeader("Token"));
			Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));
			user = this.accountService.loadUserById(userId);
			friends = user.getFriends().stream().distinct().collect(Collectors.toList());

		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return friends;
	}

	@GetMapping("/find-friend/{criteria}")
	@ResponseBody
	public List<UserCore> getAllSearchAccountMatches(@PathVariable String criteria, HttpServletRequest request,
			HttpServletResponse response) {

		log.info("Find matching accounts for criteria : " + criteria);
		Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));
		final UserCore user = this.accountService.loadUserById(userId);
		return this.accountService.getMatchingAccountList(criteria).stream().filter(usr -> !usr.getEmail().equals(user.getEmail()))
				  .collect(Collectors.toList());
	}

	@GetMapping("/are-we-already-friends/{pseudoname}")
	@ResponseBody
	public Boolean areWeAlreadyFriends(@PathVariable("pseudoname") String pseudoname, HttpServletRequest request,
			HttpServletResponse response) {
		Boolean result = false;
		try {
			log.info("Are We Already Friends Endpoint Started for " + pseudoname);
			Profile profile = this.profileService.getProfileByPseudoname(pseudoname);
			Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));
			UserCore user = this.accountService.loadUserById(userId);
			if (profile != null ) {
				log.info("A User profile has been found for pseudoname : " + pseudoname);
				if (!CollectionUtils.isEmpty(user.getFriends())) {
					result = user.getFriends().stream().anyMatch(t -> t.getProfile().equals(profile));
					log.info("So are we friends? " + result);

				} else {
					throw new Exception("Friend list is empty");
				}

			} else {
				throw new Exception("No profile found with this pseudoname");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return result;
	}
}
