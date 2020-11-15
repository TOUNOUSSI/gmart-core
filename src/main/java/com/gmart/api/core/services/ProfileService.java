package com.gmart.api.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gmart.api.core.entities.Picture;
import com.gmart.api.core.entities.Profile;
import com.gmart.api.core.entities.enums.PictureTypeEnum;
import com.gmart.api.core.exceptions.FileStorageException;
import com.gmart.api.core.repositories.ProfileRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Service
@Data
@Slf4j
public class ProfileService {
	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private DBFileStorageService fileStorageService;
	
	public Profile getProfileByPseudoname(String pseudoname){
		return profileRepository.findByPseudoname(pseudoname);
	}
	
	public Picture updateProfileCover(Profile profile, MultipartFile file){

		Picture picture = null;
		try {
			picture = this.fileStorageService.storeFile(file);
			picture.setPictureType(PictureTypeEnum.COVER_PICTURE);
			profile.getPictures().add(picture);
			
			this.profileRepository.saveAndFlush(profile);
		} catch (FileStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 profileRepository.saveAndFlush(profile);

		return picture;
	}
	
	public Picture updateProfilePicture(Profile profile, MultipartFile file){

		Picture picture = null;
		try {
			picture = this.fileStorageService.storeFile(file);
			picture.setPictureType(PictureTypeEnum.PROFILE_PICTURE);
			profile.getPictures().add(picture);
			
			this.profileRepository.saveAndFlush(profile);
		} catch (FileStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		profileRepository.saveAndFlush(profile);

		return picture;
	}
}
