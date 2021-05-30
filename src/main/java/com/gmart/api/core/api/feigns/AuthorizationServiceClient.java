/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.api.feigns;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gmart.common.messages.core.requests.SignInRequest;
import com.gmart.common.messages.core.requests.SignUpRequest;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 29 mars 2021
 **/
@FeignClient(name = "gmart-authorization-server", url = "http://localhost:8762/")
public interface AuthorizationServiceClient {

	@PostMapping("gmart-authorization-server/token/extract-details")
	String extractDetailsFromJWT(@RequestBody String token);

	/**
	 * Generate authentication Token AKA Sign-in
	 *
	 * @param authToken
	 * @return Boolean
	 */
	@PostMapping("gmart-authorization-server/token/generate-token")
	String generateToken(@Valid @RequestBody SignInRequest loginRequest);

	/**
	 * Registration
	 *
	 * AKA Sign-up
	 *
	 * @param authToken
	 * @return String
	 */
	@PostMapping("gmart-authorization-server/token/register")
	boolean register(@Valid @RequestBody SignUpRequest signUpRequest);

	/**
	 * Validate authentication Token
	 *
	 * @param authToken
	 * @return Boolean
	 */
	@PostMapping("gmart-authorization-server/token/validate")
	boolean validateToken(@RequestBody String authToken);

}
