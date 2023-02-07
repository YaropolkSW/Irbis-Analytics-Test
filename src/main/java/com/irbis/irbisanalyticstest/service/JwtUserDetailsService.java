package com.irbis.irbisanalyticstest.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MESSAGE = "User not found with username: ";

    private final static String USERNAME = "yar";
    private final static String PASSWORD = "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6";

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (USERNAME.equals(username)) {
            return new User(USERNAME, PASSWORD, new ArrayList<>());
        } else {
            throw new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE + username);
        }
    }
}