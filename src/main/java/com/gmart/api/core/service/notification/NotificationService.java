/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.service.notification;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.domain.notification.Notification;
import com.gmart.api.core.repository.notification.NotificationRepository;
import com.gmart.api.core.service.AbstractService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 10 juin 2021
 **/
@Transactional
@Slf4j
@Service
public class NotificationService implements AbstractService<Notification, String> {

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public Notification findById(String id) {
		Optional<Notification> optionalNotification = notificationRepository.findById(id);
		if (optionalNotification.isPresent()) {
			return optionalNotification.get();
		}
		return null;
	}

	@Override
	public Set<Notification> findAll() {
		return new HashSet<>(notificationRepository.findAll());
	}

	@Override
	public Set<Notification> findAllById(String id) {
		return notificationRepository.findAllById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class, isolation = Isolation.READ_COMMITTED)
	public Notification saveContent(Notification notification) {
		// Save request
		log.info("Saving new notification");
		return notificationRepository.saveAndFlush(notification);
	}

	/**
	 * Get Set of All Receiver Notifications ordered by the newest ones then the already
	 * checked ones
	 *
	 * @return Set<Notification>
	 */
	public Set<Notification> findAllByProfileOrderByCheckedStatusAndNotificationDateDesc(Profile profile) {
		return this.notificationRepository.findAllByProfileOrderByCheckedAscNotificationDateDesc(profile);
	}


	/**
	 *
	 * @param notification
	 * @return
	 */
	public Notification save(Notification notification) {
		return this.notificationRepository.save(notification);
	}


	public Boolean delete(String id ) {
		log.info("Deleting the notification ID {}", id);
		try {
			this.notificationRepository.deleteById(id);
			return true;

		}catch (Exception e) {
			log.equals(e.getMessage());
			return false;
		}

	}

}
