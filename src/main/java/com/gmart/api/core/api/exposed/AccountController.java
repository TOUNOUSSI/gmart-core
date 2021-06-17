/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.api.exposed;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.api.feigns.AuthorizationServiceClient;
import com.gmart.api.core.commons.exchange.RequestExchange;
import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.domain.Role;
import com.gmart.api.core.domain.UserProfile;
import com.gmart.api.core.enums.RoleName;
import com.gmart.api.core.exception.UserSignInException;
import com.gmart.api.core.exception.UserSignUpException;
import com.gmart.api.core.repository.profile.ProfileRepository;
import com.gmart.api.core.service.AccountService;
import com.gmart.common.enums.core.LoginStatus;
import com.gmart.common.enums.core.SignUpStatus;
import com.gmart.common.messages.core.UserInfoDTO;
import com.gmart.common.messages.core.requests.SignInRequest;
import com.gmart.common.messages.core.requests.SignUpRequest;
import com.gmart.common.messages.core.responses.CustomError;
import com.gmart.common.messages.core.responses.SignInResponse;
import com.gmart.common.messages.core.responses.SignUpResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 29 mars 2021
 **/
@RestController
@RequestMapping("account")
@CrossOrigin(origins = { "*" })
@Scope("session")
@Slf4j
public class AccountController {

	// DI of User account service
	@Autowired
	private AccountService accountService;

	// DI of AuthorizationService FeignClient for Tokens generation and validation
	@Autowired
	private AuthorizationServiceClient autorizationService;

	// DI of PasswordEncoder
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * L'objet HttpSession
	 */
	@Autowired
	private HttpSession session;

	// Profile Repository injection
	@Autowired
	private ProfileRepository profileRepository;

	@PostMapping("/auth/signin")
	public ResponseEntity<SignInResponse> signin(@Valid @RequestBody SignInRequest loginRequest,
			HttpServletRequest request, HttpServletResponse response) {
		String jwt = "";
		log.info("Authentication process started for the request : " + loginRequest.toString());
		try {
			SignInResponse signInResponse = new SignInResponse();
			jwt = autorizationService.generateToken(loginRequest);
			UserProfile userCore = accountService.loadUserByUsername(loginRequest.getUsername());

			if (!StringUtils.isEmpty(jwt) && userCore != null) {
				signInResponse.setToken(jwt);
				signInResponse.setLoginStatus(LoginStatus.AUTHENTICATED);
				UserInfoDTO userInfo = new UserInfoDTO();
				userInfo.setId(userCore.getId());
				userInfo.setEmail(userCore.getEmail());
				userInfo.setFirstname(userCore.getFirstname());
				userInfo.setLastname(userCore.getLastname());
				userInfo.setPhone(userCore.getPhone());
				userInfo.setUsername(userCore.getUsername());
				userInfo.setPseudoname(userCore.getProfile().getPseudoname());
				signInResponse.setAuthenticatedUser(userInfo);
				log.info("UserProfile has been loaded successfully ");
				log.info(loginRequest.toString());

				session.setAttribute(RequestExchange.CURRENT_USER_HEADER, userCore);
				session.setAttribute(RequestExchange.TOKEN_HEADER, jwt);
			} else {
				throw new UserSignInException("Incorrect username or password!", "404");
			}

			return ResponseEntity.status(HttpStatus.OK).body(signInResponse);

		} catch (Exception e) {
			log.error("AuthenticationController->Signin | Message : " + e.getMessage());
			SignInResponse signInResponse = new SignInResponse();
			CustomError error = new CustomError();
			signInResponse.setLoginStatus(LoginStatus.NOT_AUTHENTICATED);

			if (e instanceof UserSignInException) {
				error.setCode(((UserSignInException) e).getCode());
				error.setMessage(((UserSignInException) e).getMessage());
			} else {
				error.setCode("500");
				error.setMessage(e.getMessage());
			}
			signInResponse.setError(error);

			return ResponseEntity.status(Integer.parseInt(error.getCode())).body(signInResponse);

		}
	}

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			SignUpResponse signUpResponse = new SignUpResponse();
			log.info("Sign up prossess started");

			UserProfile userCore = accountService.loadUserByUsername(signUpRequest.getUsername());

			if (userCore == null) {

				autorizationService.register(signUpRequest);

				// Successful sign up request
				UserProfile userTobeRegistred = new UserProfile();
				userTobeRegistred.setFirstname(signUpRequest.getFirstname());
				userTobeRegistred.setLastname(signUpRequest.getLastname());
				userTobeRegistred.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
				userTobeRegistred.setPhone(signUpRequest.getPhone());
				userTobeRegistred.setUsername(signUpRequest.getUsername());
				userTobeRegistred.setEmail(signUpRequest.getEmail());

				// setting the profile
				Profile profile = new Profile();
				profile.setPseudoname(signUpRequest.getEmail().split("@")[0]);
				profile.setFirstname(signUpRequest.getFirstname());
				profile.setLastname(signUpRequest.getLastname());
				profile.setPhone(signUpRequest.getPhone());
				profile.setUsername(signUpRequest.getUsername());

				Profile savedProfile = profileRepository.saveAndFlush(profile);
				userTobeRegistred.setProfile(savedProfile);
				// To be modified, just for test issue
				Set<Role> roles = new HashSet<>();
				Role userRole = new Role();
				Role adminRole = new Role();
				userRole.setName(RoleName.USER);
				adminRole.setName(RoleName.ADMIN);
				roles.add(userRole);
				roles.add(adminRole);
				userTobeRegistred.setRoles(roles);

				// Storing the object into database
				accountService.save(userTobeRegistred);

				signUpResponse.setSignUpStatus(SignUpStatus.CREATED);
				// Returning after Appending the response to the body of the HttpResponse
				return ResponseEntity.accepted().body(signUpResponse);
			} else {
				signUpResponse.setSignUpStatus(SignUpStatus.NOT_CREATED);
				throw new UserSignUpException("User has been already registred!", "500");
			}

		} catch (Exception e) {
			log.error(e.getClass().getName() + " | Message " + e.getMessage());
			SignUpResponse signUpResponse = new SignUpResponse();
			signUpResponse.setSignUpStatus(SignUpStatus.NOT_CREATED);
			CustomError error = new CustomError();
			if (e instanceof UserSignUpException) {
				error.setCode(((UserSignUpException) e).getCode());
				error.setMessage(e.getMessage());

			}
			if (e instanceof ConstraintViolationException || e instanceof DataIntegrityViolationException
					|| e instanceof Exception) {
				error.setCode("409");
				error.setMessage("This user : '" + signUpRequest.getUsername() + "' has been already registred!");
			}

			signUpResponse.setError(error);
			return ResponseEntity.status(Integer.parseInt(error.getCode())).body(signUpResponse);

		}
	}

	@GetMapping("/auth/signout")
	public Boolean signout(HttpServletRequest request, HttpServletResponse response) {
		try {
			String username = autorizationService.extractDetailsFromJWT(request.getHeader("Token"));
			final UserProfile user = this.accountService.loadUserByUsername(username);
			if (user != null) {
				this.session.removeAttribute("CurrentUser");
				this.session.removeAttribute("jwt");
				this.session.invalidate();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}
}
