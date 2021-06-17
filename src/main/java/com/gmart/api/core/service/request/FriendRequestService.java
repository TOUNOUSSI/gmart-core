/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.service.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmart.api.core.domain.FriendRequest;
import com.gmart.api.core.repository.request.FriendRequestRepository;

import lombok.Data;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 14 juin 2021
 **/

@Data
@Service
public class FriendRequestService {

	@Autowired
	private FriendRequestRepository friendRequestRepository;

	public FriendRequest save(FriendRequest friendRequest) {
		return this.friendRequestRepository.save(friendRequest);
	}
}
