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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.api.feigns.AuthorizationServiceClient;
import com.gmart.api.core.domain.Post;
import com.gmart.api.core.domain.UserProfile;
import com.gmart.api.core.service.AccountService;
import com.gmart.api.core.service.PostService;
import com.gmart.common.messages.core.PostDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 29 mars 2021
 **/
@RestController
@RequestMapping("post")
@CrossOrigin(origins = { "*" })
@Scope("session")
@Slf4j
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthorizationServiceClient tokenProvider;

	@PostMapping("/add-new-post")
	@ResponseBody
	public Post addNewPost(@RequestBody PostDTO postDTO, HttpServletRequest request, HttpServletResponse response) {
		Post post = null;
		UserProfile user = null;

		try {
			String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
			user = this.accountService.loadUserByUsername(username);

			if (user != null) {
				post = new Post();
				post.setDescription(postDTO.getDescription());
				post.setType(postDTO.getType());
				post.setProfile(user.getProfile());
				return this.postService.save(post);
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@GetMapping("/all-recent-posts")
	@ResponseBody
	public Set<Post> getRecentPosts(HttpServletRequest request, HttpServletResponse response) {
		UserProfile userProfile = null;
		log.info("Getting list of recent posts");
		try {
			String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
			userProfile = this.accountService.loadUserByUsername(username);

			if (userProfile != null) {
				return this.postService.findRecentPostByProfile(userProfile.getProfile());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new HashSet<>();
		}

		return new HashSet<>();
	}
}
