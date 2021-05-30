package com.gmart.api.core.services;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gmart.api.core.domain.Picture;
import com.gmart.api.core.exceptions.FileStorageException;
import com.gmart.api.core.repositories.PictureRepository;
import com.gmart.api.core.utils.ImageUtils;

@Service
public class DBFileStorageService {

	@Autowired
	private PictureRepository pictureRepository;

	@Value("${gmart.database.exchange.tmp.path}")
	public String gmartTmpContentLocation;

	/**
	 *
	 * @param file
	 * @return
	 * @throws FileStorageException
	 */
	public Picture storeFile(MultipartFile file) throws FileStorageException {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			Picture dbFile = new Picture(fileName, file.getContentType(), file.getBytes());

			return pictureRepository.save(dbFile);
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	/**
	 *
	 * @param pictureId
	 * @return
	 * @throws FileNotFoundException
	 */
	public Picture getPicture(String pictureId) throws FileNotFoundException {
		return pictureRepository.findById(pictureId)
				.orElseThrow(() -> new FileNotFoundException("File not found with id " + pictureId));

	}

	/**
	 *
	 * @param file
	 * @param scaledWidth
	 * @param scaledHeight
	 * @return
	 * @throws IOException
	 */
	public byte[] resize(MultipartFile file, int scaledWidth, int scaledHeight) throws IOException {
		// reads input image
		String realFileName = StringUtils.cleanPath(file.getOriginalFilename());

		File tmpFile = new File(gmartTmpContentLocation + "\\tmp-" + realFileName);

		try (OutputStream os = new FileOutputStream(tmpFile)) {
			os.write(file.getBytes());
		}

		BufferedImage inputImage = ImageIO.read(tmpFile);

		// creates output image
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();

		// extracts extension of output file
		String formatName = tmpFile.getCanonicalPath().substring(tmpFile.getCanonicalPath().lastIndexOf(".") + 1);

		return ImageUtils.toByteArray(outputImage, formatName);
	}

}