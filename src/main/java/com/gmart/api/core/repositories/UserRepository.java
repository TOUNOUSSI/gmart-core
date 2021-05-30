/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.domain.UserProfile;


@Repository
public interface UserRepository extends JpaRepository<UserProfile, String> {

	public UserProfile findByUsername(String username);
	public UserProfile findByProfile(Profile profile);
	public List<UserProfile> findByUsernameContainingIgnoreCase(String username);

}
