/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gmart.api.core.domain.Post;
import com.gmart.api.core.domain.Profile;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

	/**
	 *
	 * @param profile
	 * @return
	 */
	Set<Post> findAllByProfileOrderByPostDateDesc(Profile profile);
}
