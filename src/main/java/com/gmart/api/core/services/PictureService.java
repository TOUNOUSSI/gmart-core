/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.services;

import org.springframework.stereotype.Service;

import com.gmart.api.core.domain.Picture;
import com.gmart.api.core.repositories.PictureRepository;

import lombok.Data;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 27 mai 2021
 **/

@Data
@Service
public class PictureService {

	private PictureRepository pictureRespository;

	public Picture findByID(String ID) {
		return this.pictureRespository.getOne(ID);
	}
}
