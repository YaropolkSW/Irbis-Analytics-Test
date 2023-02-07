package com.irbis.irbisanalyticstest.jwt;

import java.io.Serializable;

public class JwtRequest implements Serializable {

    private String username;
    private String password;

    public JwtRequest() {}

    public JwtRequest(final String username, final String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}