package com.gmart.api.core.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by TOUNOUSSI Youssef on 11/10/2019.
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException e) throws IOException, ServletException {
		log.error("Reply with an unauthorized error. Message - {} , {}", e.getMessage(),httpServletRequest.getRequestURI());
		httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				"Sorry, you are not allowed to access this resource");
    }
}
