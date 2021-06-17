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
import org.springframework.util.StringUtils;
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
import com.gmart.api.core.service.profile.ProfileService;
import com.gmart.common.messages.core.CustomProfileDTO;

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
	private AuthorizationServiceClient tokenProvider;

	/**
	 * @todo use ResponseEntity<DTO> instead of returning domain objects
	 */


	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/find-my-profile")
	@ResponseBody
	public Profile getMyProfile(HttpServletRequest request, HttpServletResponse response) {
		log.info("Loading my profile...");
		log.info("Token founded : " + request.getHeader("Token"));
		String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
		return this.profileService.findByUsername(username);
	}

	/**
	 *
	 * @param pseudoname
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/find-profile/{pseudoname}")
	public Profile getProfile(@PathVariable String pseudoname, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("Pseudoname param founded : " + pseudoname);
		return this.profileService.getProfileByPseudoname(pseudoname);
	}

	/**
	 *
	 * @param file
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/update-profile-cover")
	@ResponseBody
	public Picture updateProfileCover(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("Recieved inside core Profile : " + file.getName());
		String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
		if(!StringUtils.isEmpty(username)) {
			Profile profile = this.profileService.findByUsername(username);
			if(profile != null) {
				return this.profileService.updateProfileCover(profile, file);
			}
		}
		return null;
	}

	/**
	 *
	 * @param file
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/update-profile-picture")
	@ResponseBody
	public Picture updateProfilePicture(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("Recieved inside core Profile : " + file.getName());
		String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
		if(!StringUtils.isEmpty(username)) {
			Profile profile = this.profileService.findByUsername(username);
			if(profile != null) {
				return this.profileService.updateProfilePicture(profile, file);
			}
		}
		return null;
	}

	/**
	 * Find Custom Profile by username criteria
	 * @param username
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/find-custom-profile-by-username/{username}")
	public CustomProfileDTO findCustomProfileByUsername(@PathVariable String username, HttpServletRequest request,
			HttpServletResponse response) {
		CustomProfileDTO customProfileDTO = new CustomProfileDTO();
		Profile profile = this.profileService.findByUsername(username);
		if (profile != null) {
			customProfileDTO.setFirstname(profile.getFirstname());
			customProfileDTO.setLastname(profile.getLastname());
			customProfileDTO.setAvatar(profile.getAvatarPayload());
			customProfileDTO.setUsername(username);
			customProfileDTO.setPseudoname(profile.getPseudoname());
		}

		return customProfileDTO;
	}

}
