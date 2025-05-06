package org.example.watchfinder.dto;

public class ProfileImageUpdateResponse {

    private String profileImageUrl;

    public ProfileImageUpdateResponse(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}