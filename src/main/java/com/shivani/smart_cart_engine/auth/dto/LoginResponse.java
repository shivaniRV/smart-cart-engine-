
package com.shivani.smart_cart_engine.auth.dto;

public class LoginResponse {

    private String token;
    private String tokenType;

    public LoginResponse() {
    }

    public LoginResponse(String token) {
        this.token = token;
        this.tokenType = "Bearer";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
