package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public boolean isUser(Authentication authentication){

        boolean isUser = false;

        isUser = authentication.getAuthorities().stream()
                .map(role -> role.getAuthority())
                .anyMatch(role -> role.equals("ROLE_USER"));

        return isUser;
    }

    public String getEmail(Authentication authentication){

        String email = null;

        if(authentication.getPrincipal() instanceof UserDetailsDTO userDetailsDTO){

            email = userDetailsDTO.getUserDTO().getEmail();
        }

        return email;
    }

    public String getNickname(Authentication authentication){

        String nickname = null;

        if(authentication.getPrincipal() instanceof UserDetailsDTO userDetailsDTO){

            nickname = userDetailsDTO.getUserDTO().getNickname();
        }

        return nickname;
    }

    public int getUserId(Authentication authentication){

        int userId = 0;

        if(authentication.getPrincipal() instanceof UserDetailsDTO userDetailsDTO){

            userId = userDetailsDTO.getUserDTO().getUserId();
        }

        return userId;
    }

}
