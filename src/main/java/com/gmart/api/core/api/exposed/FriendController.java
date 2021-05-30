/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.api.exposed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.api.feigns.AuthorizationServiceClient;
import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.domain.UserProfile;
import com.gmart.api.core.services.AccountService;
import com.gmart.api.core.services.ProfileService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 29 mars 2021
 **/
@RestController
@RequestMapping("friend")
@CrossOrigin(origins = { "*" })
@Scope("session")
@Slf4j
@Data
public class FriendController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private AuthorizationServiceClient tokenProvider;

	@PutMapping("/add-new-friend/{pseudoname}")
	@ResponseBody
	public Boolean addUserToFrienList(@PathVariable("pseudoname") String pseudoname, HttpServletRequest request,
			HttpServletResponse response) {
		UserProfile user = null;
		UserProfile friendToBeAdd = null;
		log.info("Add new friend pseudo: " + pseudoname);

		Profile profile = this.profileService.getProfileByPseudoname(pseudoname);

		try {
			if (profile != null) {
				String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
				user = this.accountService.loadUserByUsername(username);

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

	@GetMapping("/are-we-already-friends/{pseudoname}")
	@ResponseBody
	public Boolean areWeAlreadyFriends(@PathVariable("pseudoname") String pseudoname, HttpServletRequest request,
			HttpServletResponse response) {
		Boolean result = false;
		try {
			log.info("Are We Already Friends Endpoint Started for " + pseudoname);
			Profile profile = this.profileService.getProfileByPseudoname(pseudoname);
			String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
			UserProfile user = this.accountService.loadUserByUsername(username);

			if (profile != null) {
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

	@GetMapping("/find-friends/{criteria}")
	@ResponseBody
	public Collection<UserProfile> getAllSearchAccountMatches(@PathVariable String criteria, HttpServletRequest request,
			HttpServletResponse response) {
		Collection<UserProfile> matchingUsersCores = null;
		try {
			log.info("Find matching accounts for criteria : " + criteria);
			matchingUsersCores = new ArrayList<>();
			if (!StringUtils.isEmpty(criteria)) {
				matchingUsersCores = this.accountService.getMatchingUsersList(criteria);
				if (!CollectionUtils.isEmpty(matchingUsersCores)) {
					String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
					final UserProfile user = this.accountService.loadUserByUsername(username);
					matchingUsersCores.stream().filter(usr -> !usr.getEmail().equals(user.getEmail()))
							.collect(Collectors.toList());
					return matchingUsersCores;
				}
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}

		return matchingUsersCores;
	}

	@GetMapping("/myfriends")
	@ResponseBody
	public List<UserProfile> getFriendList(HttpServletRequest request, HttpServletResponse response) {
		List<UserProfile> friends = null;
		UserProfile user = null;
		try {
			log.info("get Friend List started here " + request.getHeader("Token"));
			String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
			user = this.accountService.loadUserByUsername(username);
			friends = user.getFriends().stream().distinct().collect(Collectors.toList());

		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return friends;
	}
}
