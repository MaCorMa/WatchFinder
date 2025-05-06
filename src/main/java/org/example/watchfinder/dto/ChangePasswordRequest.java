package org.example.watchfinder.dto;

//se podria usar el mismo que el que se usa al cambiar contraseña con el "olvidé mi contraseña", para mayor claridad se separa
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
