package org.example.watchfinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "password_reset_tokens")
public class PasswordResetToken {
    //Clase para los reset token
    private static final int EXPIRATION = 10; // 10 min

    @Id
    private String id;
    private String token;
    private String userId;
    private Date expiryDate;

    public PasswordResetToken() {}

    public PasswordResetToken(String token, String userId) {
        this.token = token;
        this.userId = userId;
        this.expiryDate = new Date(System.currentTimeMillis() + EXPIRATION * 60 * 1000);
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
