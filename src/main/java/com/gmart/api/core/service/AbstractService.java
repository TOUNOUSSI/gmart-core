/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.service;

import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

/**
 * @param <T>
 * @param <ID>
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 10 juin 2021
 **/
public interface AbstractService<T, ID> {

	@Transactional(readOnly = true)
	T findById(ID id);

	@Transactional(readOnly = true)
	Set<T> findAll();

	@Transactional(readOnly = true)
	Set<T> findAllById(ID id);

	T saveContent(T subject);

}
