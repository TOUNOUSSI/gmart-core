/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmart.api.core.domain.notification.Notification;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PROFILE")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Profile implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1355284798592977463L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column(unique = true, nullable = false)
	private String pseudoname;

	@Column(unique = true, nullable = false)
	private String username;

	private String firstname;
	private String lastname;
	private String nickname;
	private String phone;
	private String profileDescription;

	@JsonProperty("avatar")
	@Lob
	@Column(name = "AVATAR_PAYLOAD")
	private byte[] avatarPayload;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Collection<Picture> pictures = new HashSet<>();

	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Collection<Post> posts = new HashSet<>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	@EqualsAndHashCode.Exclude
	private Collection<Notification> notifications = new HashSet<>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Collection<FriendRequest> friendRequests = new HashSet<>();
}
