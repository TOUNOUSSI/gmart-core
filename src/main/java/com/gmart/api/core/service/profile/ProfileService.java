package com.gmart.api.core.service.profile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gmart.api.core.domain.Picture;
import com.gmart.api.core.domain.Profile;
import com.gmart.api.core.exception.FileStorageException;
import com.gmart.api.core.repository.profile.ProfileRepository;
import com.gmart.api.core.service.DBFileStorageService;
import com.gmart.common.enums.core.PictureType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@Slf4j
@EqualsAndHashCode(callSuper=false)
public class ProfileService extends AbstractUserProfileService<Profile, String>{

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private DBFileStorageService fileStorageService;

	@Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class, isolation = Isolation.READ_COMMITTED)
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
			log.error(e.getMessage());
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

	@Override
	public Profile findById(String id) {
		Optional<Profile> optionalProfile = profileRepository.findById(id);
		if (optionalProfile.isPresent()) {
			return optionalProfile.get();
		}
		return null;
	}

	@Override
	public Set<Profile> findAll() {
		return new HashSet<>(profileRepository.findAll());
	}

	@Override
	public Set<Profile> findAllById(String id) {
		return profileRepository.findAllById(id);
	}
}
