/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.api.exposed;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.domain.notification.Notification;
import com.gmart.api.core.domain.notification.NotificationData;
import com.gmart.api.core.service.notification.NotificationService;
import com.gmart.api.core.service.profile.ProfileService;
import com.gmart.common.messages.core.notification.NotificationDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 26 mars 2021
 **/

@RestController
@RequestMapping("notification")
@CrossOrigin(origins = { "*" })
@Slf4j
public class NotificationController {

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ProfileService profileService;

	@MessageMapping("/send/push-notification")
	public void pushNotification(@Payload NotificationDTO notificationDTO) {
		Notification notification = new Notification();
		log.info("New Notification of type " + notificationDTO.getType());

		notification.setBody(notificationDTO.getBody());
		notification.setType(notificationDTO.getType());
		NotificationData notificationData = new NotificationData();

		if (notificationDTO.getData() != null) {
			notificationData.setUrl(notificationDTO.getData().getUrl());
		}

		notification.setNotificationData(notificationData);
		notification.setNotificationDate(new Date());

		// Getting the sender base on the username
		Profile sender = this.profileService.findByUsername(notificationDTO.getSender().getUsername());

		// Setting the sender
		notification.setSender(sender);
		// Getting the receiver base on the username
		Profile receiver = this.profileService.findByUsername(notificationDTO.getReceiver().getUsername());
		notification.setProfile(receiver);

		Notification savedNotification = this.notificationService.saveContent(notification);

		// Add notification to its list
		receiver.getNotifications().add(savedNotification);

		this.profileService.save(receiver);

		this.messagingTemplate.convertAndSend("/topic/private-notification-channel-of-" + notificationDTO.getReceiver().getUsername(),
				notificationDTO);

		log.info("Creation ended");
	}

	@GetMapping("/all/{username}")
	@ResponseBody
	public Set<Notification> getAllNotifications(@PathVariable String username, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("Getting all notifications");
		Set<Notification> notifications = new HashSet<>();
		try {

			Profile profile = this.profileService.findByUsername(username);
			if (profile != null) {
				log.info("Notifications of " + profile.getFirstname() + " " + profile.getLastname());
				notifications = this.notificationService
						.findAllByProfileOrderByCheckedStatusAndNotificationDateDesc(profile);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return notifications;
	}

	@DeleteMapping("/delete/{notificationID}")
	@ResponseBody
	public Boolean deleteNotification(@PathVariable("notificationID") String notifID, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			log.info("Updating notification status");
			return this.notificationService.delete(notifID);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@PutMapping("/viewed")
	@ResponseBody
	public Boolean changeNotificationCheckedStatus(@RequestBody String notifID, HttpServletRequest request,
			HttpServletResponse response) {
		Notification notification = null;
		try {
			log.info("Updating notification status");
			notification = this.notificationService.findById(notifID);
			if (notification != null) {
				notification.setChecked(true);
				this.notificationService.save(notification);
				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}
}
