package com.gmart.api.core.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Null;

import org.hibernate.annotations.GenericGenerator;

import com.gmart.common.enums.core.PostType;

import lombok.Data;

@Entity
@Table(name = "POST")
@Data
public class Post {

	 
		@Id
	    @GeneratedValue(generator = "uuid")
	    @GenericGenerator(name = "uuid", strategy = "uuid2")
	    private String id;

		@Enumerated
		private PostType type;
		
		private Timestamp postDate;
		
		private String description;
		
		@OneToMany(cascade = CascadeType.ALL)
		@JoinColumn(name = "post_id",referencedColumnName = "id")
		@Null
		private List<Comment> comments;
}
