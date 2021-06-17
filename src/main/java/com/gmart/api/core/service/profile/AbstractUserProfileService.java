/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.service.profile;

import javax.persistence.EntityNotFoundException;

import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.gmart.api.core.repository.profile.TUserProfileRepository;
import com.gmart.api.core.service.AbstractService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 10 juin 2021
 **/

@Retryable(value = { StaleStateException.class, CannotAcquireLockException.class })
@Slf4j
@Service
public abstract class AbstractUserProfileService<T, D>  implements AbstractService<T,D>{

	@Autowired
	private TUserProfileRepository<T> userProfileRepository;

	@Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class, isolation = Isolation.READ_COMMITTED)
	public T findByUsername(String username) {
		return this.userProfileRepository.findByUsername(username);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class, isolation = Isolation.READ_COMMITTED)
	public T saveContent(T request) {
		// Save request
		log.info("Saving the " + request.getClass().getSimpleName() + " request ...");
		return userProfileRepository.saveAndFlush(request);
	}

	@Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class, isolation = Isolation.READ_COMMITTED)
	public T save(T t) {
		return this.userProfileRepository.save(t);
	}
}
