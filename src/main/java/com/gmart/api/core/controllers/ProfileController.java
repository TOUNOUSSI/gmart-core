package com.gmart.api.core.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gmart.api.core.entities.Picture;
import com.gmart.api.core.entities.Profile;
import com.gmart.api.core.entities.UserCore;
import com.gmart.api.core.security.JwtTokenProvider;
import com.gmart.api.core.services.AccountService;
import com.gmart.api.core.services.ProfileService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping( "profile")
@Scope("session")
@Slf4j
public class ProfileController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	ProfileService profileService;
	
	@Autowired
	JwtTokenProvider jwtProvider;
	
	@GetMapping("/find-profile/{pseudoname}")
	public Profile getProfile(@PathVariable String pseudoname, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("Pseudoname param founded : "+pseudoname);
		return this.profileService.getProfileByPseudoname(pseudoname);
	}
	
	
	@GetMapping("/find-my-profile")
	@ResponseBody
	public Profile getMyProfile(HttpServletRequest request,
			HttpServletResponse response) {
        UserCore user = null;
        log.info("Loading my profile...");
        log.info("Token founded : "+request.getHeader("Token"));
		Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));
		user = this.accountService.loadUserById(userId);
		log.info("Profile founded here : " +user.getFirstname() + user.getLastname());
		return user.getProfile();
	}

	
	@PostMapping("/update-profile-cover")
	@ResponseBody
	public Picture updateProfileCover(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response) {
		log.info("Recieved inside core Profile : "+file.getName());
		UserCore user = null;
		
		Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));

		user = this.accountService.loadUserById(userId);
		log.info("User's request found for :   "+user.getFirstname());
		log.info("Recieved inside core Pseudoname :"+user.getProfile().getPseudoname());
		
		return this.profileService.updateProfileCover(user.getProfile(), file);
		
	}

	
	@PostMapping("/update-profile-picture")
	@ResponseBody
	public Picture updateProfilePicture(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response) {
		log.info("Recieved inside core Profile : "+file.getName());
		UserCore user = null;
		
		Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));

		user = this.accountService.loadUserById(userId);
		log.info("User's request found for :   "+user.getFirstname());
		log.info("Recieved inside core Pseudoname :"+user.getProfile().getPseudoname());
		
		return this.profileService.updateProfilePicture(user.getProfile(), file);
		
	}
}
