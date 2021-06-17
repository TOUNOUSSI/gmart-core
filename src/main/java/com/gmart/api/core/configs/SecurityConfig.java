package com.gmart.api.core.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.gmart.api.core.security.JwtAuthenticationEntryPoint;
import com.gmart.api.core.security.JwtAuthenticationFilter;
import com.gmart.api.core.service.AccountService;

/**
 * Created by TOUNOUSSI Youssef on 02/05/18.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AccountService accountService;

	@Value("${gmart.jwt.secret}")
	public String secret;

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService((UserDetailsService) accountService)
				.passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable().and().cors().and().sessionManagement().maximumSessions(1).and()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
				.authenticationEntryPoint(unauthorizedHandler).and().authorizeRequests().and().anonymous().and()
				.servletApi().and().headers().cacheControl().and().and()

				// Relax CSRF on the WebSocket due to needing direct access from apps
				.csrf().ignoringAntMatchers("/ws/**", "/notification/**").and().authorizeRequests()
				.antMatchers("/", "/*.js", "/*.jsp", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg",
						"/**/*.html", "/**/*.css", "/**/*.js", "/account/login", "/**/login*", "/**/login**",
						"/invalidSession", "/resources/invalidSession")
				.permitAll().antMatchers("/h2-console/**", "/h2-console", "/notification", "/notification/**")
				.permitAll().antMatchers(org.springframework.http.HttpMethod.OPTIONS, "/service/**").permitAll()
				.antMatchers(HttpMethod.POST, "/account/auth/signin", "/account/auth/signout", "/account/signup",
						"/profile/update-profile-picture", "/profile/update-profile-cover")
				.permitAll()
				.antMatchers(HttpMethod.POST, "/post/add-new-post", "/comment/add-new-comment-on-post/{postID}")
				.permitAll().antMatchers(HttpMethod.POST, "/notifications").permitAll()
				.antMatchers(HttpMethod.GET, "/friend/myfriends", "/account/accounts").permitAll()
				.antMatchers(HttpMethod.GET, "/authentication/signout").permitAll()
				.antMatchers(HttpMethod.GET, "/friend/find-friends/{criteria}",
						"/friend/are-we-already-friends/{pseudoname}", "/profile/find-my-profile",
						"/profile/find-profile/{pseudoname}")
				.permitAll()
				.antMatchers(HttpMethod.GET, "/post/all-recent-posts",
						"/profile/find-custom-profile-by-username/{username}", "/notification/all/{username}")
				.permitAll()
				.antMatchers(HttpMethod.DELETE,"/notification/delete/{notificationID}").permitAll()
				.antMatchers(HttpMethod.PUT, "/friend/add-new-friend/{pseudoname}", "/add-friend-request/{pseudoname}",
						"/notification/viewed")
				.permitAll().anyRequest().authenticated().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll().and().csrf().disable(); // Disabling
																												// the
		// CSRF

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

}
