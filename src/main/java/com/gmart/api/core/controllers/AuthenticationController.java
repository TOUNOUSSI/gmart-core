package com.gmart.api.core.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import org.springframework.context.annotation.Scope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.entities.Profile;
import com.gmart.api.core.entities.Role;
import com.gmart.api.core.entities.UserCore;
import com.gmart.api.core.exceptions.UserSignInException;
import com.gmart.api.core.exceptions.UserSignUpException;
import com.gmart.api.core.repositories.ProfileRepository;
import com.gmart.api.core.repositories.UserRepository;
import com.gmart.api.core.security.JwtTokenProvider;
import com.gmart.api.core.services.AccountService;
import com.gmart.api.messages.requests.SignInRequest;
import com.gmart.api.messages.requests.SignUpRequest;
import com.gmart.api.messages.responses.CustomError;
import com.gmart.api.messages.responses.SignInResponse;
import com.gmart.api.messages.responses.SignUpResponse;
import com.gmart.api.messages.responses.UserInfo;
import com.gmart.api.messages.responses.enums.LoginStatus;
import com.gmart.api.messages.responses.enums.SignUpStatus;
import com.gmart.common.enums.core.RoleName;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("authentication")
@Scope("session")
@Slf4j
public class AuthenticationController {

	/**
	 * Global Spring Authentication Object
	 */
	protected Authentication authentication;

	// DI of Spring AuthenticationManager
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AccountService accountService;

	// DI of PasswordEncoder
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * L'objet HttpSession
	 */
	private HttpSession session;

	// DI of TokenProvider for Tokens generation and validation
	@Autowired
	JwtTokenProvider tokenProvider;

	// UserRepository Injection
	@Autowired
	UserRepository userRepository;

	// Profile Repository injection
	@Autowired
	ProfileRepository profileRepository;

	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest loginRequest, HttpServletRequest request,
			HttpServletResponse response) {
		String jwt = "";
		log.info("Authentication process started for the request : " + loginRequest.toString());
		try {
			session = request.getSession();

			SignInResponse signInResponse = new SignInResponse();
			if (loginRequest != null) {
				UserCore userCore = accountService.loadUserByUsername(loginRequest.getUsername());
				if (userCore != null) {
					signInResponse.setLoginStatus(LoginStatus.AUTHENTICATED);
					UserInfo userInfo = new UserInfo();
					userInfo.setId(userCore.getId());
					userInfo.setEmail(userCore.getEmail());
					userInfo.setFirstname(userCore.getFirstname());
					userInfo.setLastname(userCore.getLastname());
					userInfo.setPhone(userCore.getPhone());
					userInfo.setRoles(userCore.getRoles());
					userInfo.setUsername(userCore.getUsername());
					userInfo.setPseudoname(userCore.getProfile().getPseudoname());
					signInResponse.setAuthenticatedUser(userInfo);
					log.info("User has been authenticated successfully ");
					log.info(loginRequest.toString());
					authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
							loginRequest.getUsername(), loginRequest.getPassword(), userCore.getAuthorities()));
					jwt = tokenProvider.generateToken(authentication);
					// session.setMaxInactiveInterval(3600);
					session.setAttribute("CurrentUser", userRepository.findByUsername(loginRequest.getUsername()));
					session.setAttribute("Token", jwt);
					signInResponse.setToken(jwt);

				} else {
					throw new UserSignInException("Incorrect username or password!", "404");

				}

			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(signInResponse);


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
	public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest newUser, HttpServletRequest request,
			HttpServletResponse response) {
		try {
				log.info(newUser.toString());

				// Successful sign up request
				UserCore userTobeRegistred = new UserCore();
				userTobeRegistred.setFirstname(newUser.getFirstname());
				userTobeRegistred.setLastname(newUser.getLastname());
				userTobeRegistred.setPassword(passwordEncoder.encode(newUser.getPassword()));
				userTobeRegistred.setPhone(newUser.getPhone());
				userTobeRegistred.setUsername(newUser.getUsername());
				userTobeRegistred.setEmail(newUser.getEmail());
				
				//setting the profile
				Profile profile = new Profile();
                profile.setPseudoname(newUser.getEmail().split("@")[0]);
                profile.setFirstname(newUser.getFirstname());
                profile.setLastname(newUser.getLastname());
                profile.setPhone(newUser.getPhone());
                
                profileRepository.saveAndFlush(profile);
				userTobeRegistred.setProfile(profile);
				// To be modified, just for test issue
				Set<Role> roles = new HashSet<Role>();
				Role user_role = new Role();
				Role admin_role = new Role();
				user_role.setName(RoleName.USER);
				admin_role.setName(RoleName.ADMIN);
				roles.add(user_role);
				roles.add(admin_role);
				userTobeRegistred.setRoles(roles);

				// Storing the object into database
				accountService.save(userTobeRegistred);

				// Creating the SignUp response
				SignUpResponse signUpResponse = new SignUpResponse();
				signUpResponse.setSignUpStatus(SignUpStatus.CREATED);
				// Returning after Appending the response to the body of the HttpResponse
				return ResponseEntity.accepted().body(signUpResponse);


		} catch (Exception e) {
			log.error(e.getClass().getName() + " | Message " + e.getMessage());
			SignUpResponse signUpResponse = new SignUpResponse();
			signUpResponse.setSignUpStatus(SignUpStatus.NOT_CREATED);
			CustomError error = new CustomError();
			if (e instanceof UserSignUpException) {
				error.setCode(((UserSignUpException) e).getCode());
				error.setMessage(e.getMessage());

			}
			if (e instanceof ConstraintViolationException || e instanceof DataIntegrityViolationException || e instanceof Exception) {
				error.setCode("409");
				error.setMessage("This user : '" + newUser.getUsername() + "' has been already registred!");
			}

			signUpResponse.setError(error);
			return ResponseEntity.status(Integer.parseInt(error.getCode())).body(signUpResponse);

		}
	}
}
