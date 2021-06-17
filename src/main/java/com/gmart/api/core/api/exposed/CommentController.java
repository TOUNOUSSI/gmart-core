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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.api.feigns.AuthorizationServiceClient;
import com.gmart.api.core.domain.Comment;
import com.gmart.api.core.domain.Picture;
import com.gmart.api.core.domain.Post;
import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.domain.UserProfile;
import com.gmart.api.core.service.AccountService;
import com.gmart.api.core.service.CommentService;
import com.gmart.api.core.service.PictureService;
import com.gmart.api.core.service.PostService;
import com.gmart.api.core.service.profile.ProfileService;
import com.gmart.common.messages.core.CommentRequestDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 29 mars 2021
 **/
@RestController
@RequestMapping("comment")
@CrossOrigin(origins = { "*" })
@Scope("session")
@Slf4j
public class CommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private PostService postService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private PictureService pictureService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthorizationServiceClient tokenProvider;

	@PostMapping("/add-new-comment-on-picture/{postID}")
	@ResponseBody
	public Boolean addNewCommentOnPicture(@PathVariable("postID") String pictureID, @RequestBody Comment comment,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
			UserProfile userProfile = this.accountService.loadUserByUsername(username);

			if (userProfile != null) {
				Picture picture = this.pictureService.findByID(pictureID);
				picture.getComments().add(comment);
				this.commentService.save(comment);

			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}

		return true;
	}

	@PostMapping("/add-new-comment-on-post/{postID}")
	@ResponseBody
	public Comment addNewCommentOnPost(@PathVariable("postID") String postID, @RequestBody CommentRequestDTO commentDTO,
			HttpServletRequest request, HttpServletResponse response) {
		Comment comment = null;
		try {
			log.info("Posting new comment");
			String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
			UserProfile userProfile = this.accountService.loadUserByUsername(username);

			if (userProfile != null) {
				comment = new Comment();
				comment.setValue(commentDTO.getValue());
				Profile commenterProfile = this.profileService.getProfileByPseudoname(commentDTO.getPseudoname());
				comment.setCommenterProfile(commenterProfile);
				Post post = this.postService.findByID(postID);
				post.getComments().add(comment);
				this.postService.save(post);
				return comment;

			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

		return null;
	}
}
