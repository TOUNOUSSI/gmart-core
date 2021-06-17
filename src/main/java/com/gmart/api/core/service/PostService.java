/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmart.api.core.domain.Post;
import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.repository.PostRepository;

import lombok.Data;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 27 mai 2021
 **/
@Data
@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	public Post save(Post user) {
		return postRepository.save(user);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public Post findByID(String id) {
		return postRepository.getOne(id);
	}

	/**
	 * Get list of Recent Post by Profile  (This is going to be a fixed size)
	 * @param profile, see {@link Profile}
	 * @return
	 */
	public Set<Post> findRecentPostByProfile(Profile profile) {
		return this.postRepository.findAllByProfileOrderByPostDateDesc(profile);
	}
}
