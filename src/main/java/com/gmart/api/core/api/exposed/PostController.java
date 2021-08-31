/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.core.api.exposed;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.gmart.api.core.domain.Picture;
import com.gmart.api.core.exception.FileStorageException;
import com.gmart.api.core.service.DBFileStorageService;
import com.gmart.common.enums.core.PictureType;
import com.gmart.common.enums.core.PostType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import com.gmart.api.core.api.feigns.AuthorizationServiceClient;
import com.gmart.api.core.domain.Post;
import com.gmart.api.core.domain.UserProfile;
import com.gmart.api.core.service.AccountService;
import com.gmart.api.core.service.PostService;
import com.gmart.common.messages.core.PostDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 29 mars 2021
 **/
@RestController
@RequestMapping("post")
@CrossOrigin(origins = {"*"})
@Scope("session")
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorizationServiceClient tokenProvider;


    @Autowired
    private DBFileStorageService fileStorageService;

    @PostMapping("/add-new-post")
    @ResponseBody
    public Post addNewPost(@RequestParam("post") String postPayload, @RequestParam("file") MultipartFile file
            , RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        Post post = null;
        UserProfile user = null;
        /**
         * @// TODO: 8/31/2021 POST TYPE IS dedicated from the file type
         */
        try {
            ObjectMapper mapper = new ObjectMapper();
            PostDTO postDTO = mapper.readValue(postPayload, PostDTO.class);
            String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
            user = this.accountService.loadUserByUsername(username);

            if (user != null) {
                post = new Post();
                post.setDescription(postDTO.getDescription());
                post.setType(PostType.IMG);
                this.fileStorageService.storeFileAsPicture(file);
                Picture picture = null;
                try {
                    picture = this.fileStorageService.storeFileAsPicture(file);
                    picture.setPictureType(PictureType.POST);
                    post.getPictures().add(picture);

                } catch (FileStorageException e) {
                    log.error(e.getMessage());
                }
                post.setProfile(user.getProfile());
                return this.postService.save(post);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @GetMapping("/all-recent-posts")
    @ResponseBody
    public Set<Post> getRecentPosts(HttpServletRequest request, HttpServletResponse response) {
        UserProfile userProfile = null;
        log.info("Getting list of recent posts");
        try {
            String username = tokenProvider.extractDetailsFromJWT(request.getHeader("Token"));
            userProfile = this.accountService.loadUserByUsername(username);

            if (userProfile != null) {
                return this.postService.findRecentPostByProfile(userProfile.getProfile());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new HashSet<>();
        }

        return new HashSet<>();
    }
}
