package com.gmart.api.core.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "PROFILE")
@Data
public class Profile implements Serializable{

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
	private String firstname;
	private String lastname;
	private String nickname;
	private String phone;
	private String profileDescription;


	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "profile_id",referencedColumnName = "id")
	private List<Picture> pictures = new ArrayList<>();
	

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "profile_id",referencedColumnName = "id")
	private List<Post> posts = new ArrayList<>();

}
