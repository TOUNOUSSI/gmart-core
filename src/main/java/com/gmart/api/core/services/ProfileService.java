package com.gmart.api.core.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gmart.api.core.domain.Picture;
import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.exceptions.FileStorageException;
import com.gmart.api.core.repositories.ProfileRepository;
import com.gmart.common.enums.core.PictureType;

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
			picture.setPictureType(PictureType.COVER_PICTURE);
			profile.getPictures().add(picture);

			this.profileRepository.saveAndFlush(profile);
		} catch (FileStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 profileRepository.saveAndFlush(profile);

		return picture;
	}

	/**
	 * Update Profile picture
	 * @param profile
	 * @param file
	 * @return Picture, see {@link Picture}
	 */
	public Picture updateProfilePicture(Profile profile, MultipartFile file){

		Picture picture = null;
		try {
			picture = this.fileStorageService.storeFile(file);
			picture.setPictureType(PictureType.PROFILE_PICTURE);
			profile.getPictures().add(picture);
			profile.setAvatarPayload(this.fileStorageService.resize(file, 25, 25));
			profileRepository.saveAndFlush(profile);

		} catch (FileStorageException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}


		return picture;
	}
}
