package com.gmart.api.core.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Null;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gmart.common.enums.core.PictureType;

import lombok.Data;

@Entity
@Table(name = "PICTURE")
@Data
public class Picture {
    
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

	private String fileName;
	
	private PictureType pictureType;

    private String fileType;

    @Lob
    private byte[] data;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "comment_id",referencedColumnName = "id")
	@Null
	private List<Comment> comments;
	
    public Picture() {

    }

    public Picture(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    // Getters and Setters (Omitted for brevity)
}