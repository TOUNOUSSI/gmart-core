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
import java.util.Set;
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
import com.gmart.api.core.domain.FriendRequest;
import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.domain.UserProfile;
import com.gmart.api.core.service.AccountService;
import com.gmart.api.core.service.profile.ProfileService;
import com.gmart.api.core.service.request.FriendRequestService;

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
	private FriendRequestService friendRequestService;

	@Autowired
	private AuthorizationServiceClient tokenProvider;

	@PutMapping("/add-new-friend/{pseudoname}")
	@ResponseBody
	public UserProfile addUserToFrienList(@PathVariable("pseudoname") String pseudoname, HttpServletRequest request,
			HttpServletResponse response) {
		UserProfile requesterUserprofile = null;
		UserProfile friendToBeAdd = null;
		log.info("Add new friend pseudo: " + pseudoname);

		Profile profile = this.profileService.getProfileByPseudoname(pseudoname);

		try {
			if (profile != null) {
				String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
				requesterUserprofile = this.accountService.loadUserByUsername(username);

				if (requesterUserprofile != null) {
					friendToBeAdd = this.accountService.loadUserByProfile(profile);
					if (friendToBeAdd != null) {
						friendToBeAdd.getFriends().add(requesterUserprofile);
						UserProfile updatedFriendUserProfile = this.accountService.update(friendToBeAdd);
						requesterUserprofile.getFriends().add(updatedFriendUserProfile);
						this.accountService.update(requesterUserprofile);
						log.info("Updating friend request list finished");

					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return requesterUserprofile;
	}

	@PutMapping("/add-friend-request/{pseudoname}")
	@ResponseBody
	public UserProfile addFriendRequest(@PathVariable("pseudoname") String pseudoname, HttpServletRequest request,
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
						FriendRequest friendRequest = new FriendRequest();
						friendRequest.setProfile(user.getProfile());
						this.friendRequestService.save(friendRequest);
						log.info("Updating friend request list finished");

					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return friendToBeAdd;
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
	public Set<UserProfile> getFriendList(HttpServletRequest request, HttpServletResponse response) {
		Set<UserProfile> friends = null;
		UserProfile friend = null;
		try {
			log.info("get Friend List started here " + request.getHeader("Token"));
			String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
			friend = this.accountService.loadUserByUsername(username);
			if (friend != null) {
				return friend.getFriends();
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return friends;
	}
}
