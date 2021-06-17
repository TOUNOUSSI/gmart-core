package com.gmart.api.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gmart.api.core.api.feigns.AuthorizationServiceClient;
import com.gmart.api.core.commons.exchange.RequestExchange;
import com.gmart.api.core.domain.UserProfile;
import com.gmart.api.core.service.AccountService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by TOUNOUSSI Youssef on 02/05/18.
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
	private AccountService accountService;// return UserPrincipal.create(user);

	@Autowired
    private AuthorizationServiceClient tokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				String details = tokenProvider.extractDetailsFromJWT(jwt);
                /*
                    Note that you could also encode the user's username and roles inside JWT claims
                    and create the UserDetails object by parsing those claims from the JWT.
                    That would avoid the following database hit. It's completely up to you.
                 */
				UserProfile userDetails = this.accountService.loadUserByUsername(details);
				request.setAttribute(RequestExchange.CURRENT_USER_HEADER, userDetails);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Impossible de définir l'authentification de l'utilisateur dans le contexte de sécurité", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("GMART_AUTHORIZATION ")) {
            return bearerToken.substring(20, bearerToken.length());
        }
        return null;
    }
}
