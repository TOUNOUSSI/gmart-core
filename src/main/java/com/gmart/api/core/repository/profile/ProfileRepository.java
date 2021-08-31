/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.repository.profile;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.gmart.api.core.domain.Profile;

@Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class, isolation = Isolation.READ_COMMITTED)
@Repository
public interface ProfileRepository extends TUserProfileRepository<Profile> {
    Profile findByPseudoname(String pseudoname);

    /**
     * Return AvatarPayload
     *
     * @param username
     * @return
     */
    @Query(name = "findAvatarPayloadByUsername", value = "SELECT avatarPayload FROM Profile WHERE username =:username ")
    byte[] findAvatarPayloadByUsername(@Param("username") String username);
}
