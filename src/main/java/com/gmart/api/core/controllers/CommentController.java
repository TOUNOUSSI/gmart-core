package com.gmart.api.core.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.entities.Comment;
import com.gmart.api.core.entities.Picture;
import com.gmart.api.core.entities.Post;
import com.gmart.api.core.entities.UserCore;
import com.gmart.api.core.repositories.CommentRepository;
import com.gmart.api.core.repositories.PictureRepository;
import com.gmart.api.core.repositories.PostRepository;
import com.gmart.api.core.security.JwtTokenProvider;
import com.gmart.api.core.services.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("comment")
@Scope("session")
@Slf4j
public class CommentController {

	@Autowired
	private JwtTokenProvider jwtProvider;
	
	@Autowired
	private AccountService accounthService;

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	PictureRepository pictureRepository;
	
	@PostMapping("/add-new-comment-on-picture/{postID}")
	@ResponseBody
	public Boolean addNewCommentOnPicture(@PathVariable("postID") String pictureID, @RequestBody Comment comment,
			HttpServletRequest request, HttpServletResponse response) {
		UserCore user = null;

		try {
			Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));
			user = this.accounthService.loadUserById(userId);
			if (user != null) {

				Picture picture = this.pictureRepository.getOne(pictureID);
				picture.getComments().add(comment);
				this.commentRepository.save(comment);

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
	public Boolean addNewCommentOnPost(@PathVariable("postID") String postID, @RequestBody Comment comment,
			HttpServletRequest request, HttpServletResponse response) {
		UserCore user = null;

		try {
			Long userId = jwtProvider.getUserIdFromJWT(request.getHeader("Token"));
			user = this.accounthService.loadUserById(userId);
			if (user != null) {

				Post post = this.postRepository.getOne(postID);
				post.getComments().add(comment);
				this.commentRepository.saveAndFlush(comment);

			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}

		return true;
	}
}
