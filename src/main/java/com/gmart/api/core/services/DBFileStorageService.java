package com.gmart.api.core.services;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gmart.api.core.entities.Picture;
import com.gmart.api.core.exceptions.FileStorageException;
import com.gmart.api.core.repositories.PictureRepository;

@Service
public class DBFileStorageService {

    @Autowired
	private PictureRepository pictureRepository;

    public Picture storeFile(MultipartFile file) throws FileStorageException {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Picture dbFile = new Picture(fileName, file.getContentType(), file.getBytes());

            return pictureRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Picture getPicture(String pictureId) throws FileNotFoundException {
			return pictureRepository.findById(pictureId)
			        .orElseThrow(() -> new FileNotFoundException("File not found with id " + pictureId));
	
    }
}