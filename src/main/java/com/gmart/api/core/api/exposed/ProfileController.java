/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.api.exposed;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gmart.api.core.api.feigns.AuthorizationServiceClient;
import com.gmart.api.core.domain.Picture;
import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.domain.UserProfile;
import com.gmart.api.core.services.AccountService;
import com.gmart.api.core.services.ProfileService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 29 mars 2021
 **/
@RestController
@RequestMapping("profile")
@CrossOrigin(origins = { "*" })
@Scope("session")
@Slf4j
public class ProfileController {

	@Autowired
	private ProfileService profileService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthorizationServiceClient tokenProvider;

	@GetMapping("/find-my-profile")
	@ResponseBody
	public Profile getMyProfile(HttpServletRequest request, HttpServletResponse response) {
		UserProfile user = null;

		log.info("Loading my profile...");
		log.info("Token founded : " + request.getHeader("Token"));
		String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
		user = this.accountService.loadUserByUsername(username);

		log.info("Profile founded here : " + user.getFirstname() + user.getLastname());
		return user.getProfile();
	}

	@GetMapping("/find-profile/{pseudoname}")
	public Profile getProfile(@PathVariable String pseudoname, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("Pseudoname param founded : " + pseudoname);
		return this.profileService.getProfileByPseudoname(pseudoname);
	}

	@PostMapping("/update-profile-cover")
	@ResponseBody
	public Picture updateProfileCover(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("Recieved inside core Profile : " + file.getName());
		String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
		UserProfile user = this.accountService.loadUserByUsername(username);

		log.info("User's request found for :   " + user.getFirstname());
		log.info("Recieved inside core Pseudoname :" + user.getProfile().getPseudoname());

		return this.profileService.updateProfileCover(user.getProfile(), file);

	}

	@PostMapping("/update-profile-picture")
	@ResponseBody
	public Picture updateProfilePicture(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("Recieved inside core Profile : " + file.getName());
		String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
		UserProfile user = this.accountService.loadUserByUsername(username);

		log.info("User's request found for :   " + user.getFirstname());
		log.info("Recieved inside core Pseudoname :" + user.getProfile().getPseudoname());

		return this.profileService.updateProfilePicture(user.getProfile(), file);

	}
}
