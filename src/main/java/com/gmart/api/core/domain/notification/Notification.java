/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.domain.notification;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmart.api.core.domain.Profile;
import com.gmart.common.enums.core.notification.NotificationType;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 7 juin 2021
 **/
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NOTIFICATION")
public class Notification implements Serializable {
	/**
	*
	*/
	private static final long serialVersionUID = -6121093713403392019L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Lob
	private String body;

	@ManyToOne
	@EqualsAndHashCode.Exclude
	private Profile sender;

	@JsonIgnore
	@ManyToOne
	@EqualsAndHashCode.Exclude
	private Profile profile;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NOTIFICATION_DATE")
	private Date notificationDate = new Date();

	@Enumerated
	private NotificationType type;

	@JsonProperty("data")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "notification_data_id", referencedColumnName = "id")
	private NotificationData notificationData;

	@Column(name = "CHECKED")
	private Boolean checked = false;
}
