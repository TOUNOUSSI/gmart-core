/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 30 mai 2021
 **/

public class ImageUtils {

	private ImageUtils() {
	}

	// convert BufferedImage to byte[]
	public static byte[] toByteArray(BufferedImage bi, String format) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, format, baos);
		return baos.toByteArray();

	}

	// convert byte[] to BufferedImage
	public static BufferedImage toBufferedImage(byte[] bytes) throws IOException {
		InputStream is = new ByteArrayInputStream(bytes);
		return ImageIO.read(is);
	}

}
