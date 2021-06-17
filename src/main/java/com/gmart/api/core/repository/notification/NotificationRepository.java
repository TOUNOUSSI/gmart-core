/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.repository.notification;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.domain.notification.Notification;


/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 10 juin 2021
 **/
@Transactional
@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

	Set<Notification> findAllById(String id);
	Set<Notification> findAllByProfileOrderByCheckedAscNotificationDateDesc(Profile profile);

}
