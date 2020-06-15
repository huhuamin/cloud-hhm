package com.huhuamin.common.oauth2.controller;

import com.huhuamin.common.oauth2.model.UserJwt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @RequestMapping("/api/profile")
    public ResponseEntity<UserProfile> myProfile() {
        UserJwt username = (UserJwt) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String email = username.getUserId() + "@mailinator.com";


        UserProfile profile = new UserProfile(username.getUsername(), email);

        return ResponseEntity.ok(profile);
    }

    public static class UserProfile {

        private String name;

        private String email;

        public UserProfile(String name, String email) {
            super();
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

    }
}
