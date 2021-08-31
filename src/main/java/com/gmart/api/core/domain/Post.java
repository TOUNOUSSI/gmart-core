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
import java.util.Date;
import java.util.HashSet;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.gmart.common.enums.core.PostType;

import lombok.Data;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 29 mars 2021
 **/
@Entity
@Table(name = "POST")
@Data
public class Post implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1688788232198242218L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Enumerated
	private PostType type;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POST_DATE")
	private Date postDate = new Date();

	private String description;

	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private Collection<Picture> pictures= new HashSet<>();

	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private Collection<Comment> comments = new HashSet<>();


	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private Collection<Reaction> reactions = new HashSet<>();


	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Profile profile = new Profile();
}
