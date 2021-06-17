/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.gmart.api.core.domain.Picture;
import com.gmart.api.core.repository.PictureRepository;

import lombok.Data;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 27 mai 2021
 **/
@Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class, isolation = Isolation.READ_COMMITTED)
@Data
@Service
public class PictureService {

	private PictureRepository pictureRespository;

	@Transactional(readOnly = true)
	public Picture findByID(String ID) {
		return this.pictureRespository.getOne(ID);
	}
}
