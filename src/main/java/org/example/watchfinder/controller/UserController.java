package org.example.watchfinder.controller;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.example.watchfinder.dto.Item;
import org.example.watchfinder.dto.ProfileImageUpdateResponse;
import org.example.watchfinder.dto.UserData;
import org.example.watchfinder.model.User;
import org.example.watchfinder.security.JwtUtil;
import org.example.watchfinder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.GrantedAuthority;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/users")
public class UserController {

    //para debug
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;

    @org.springframework.beans.factory.annotation.Value("${gcp.bucket.name}")
    private String bucketName;

    @org.springframework.beans.factory.annotation.Value("${gcp.project.id}")
    private String projectId;

    @org.springframework.beans.factory.annotation.Value("${gcp.credentials.path}")
    private String credentialsPath;

    private Storage getStorage() throws IOException {
        //debug
        logger.info("Attempting to load credentials from path: {}", credentialsPath);
        ClassPathResource resource = new ClassPathResource(credentialsPath);
        logger.info("ClassPathResource: exists={}, URL={}, path={}", resource.exists(), resource.getURL(), resource.getPath()); // Added logging
        if (!resource.exists()) {
            logger.error("Credentials file not found at path: {}", credentialsPath);
            throw new IOException("Credentials file not found: " + credentialsPath);
        }
        
        Credentials credentials = GoogleCredentials.fromStream(new ClassPathResource(credentialsPath).getInputStream());
        return StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
    }

    private String uploadImageToGCloud(byte[] imageBytes, String fileName) throws IOException {
        Storage storage = getStorage();

        BlobId blobId = BlobId.of(bucketName, "profile-images/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();

        logger.info("uploadImageToGCloud: blobId={}", blobId);
        logger.info("uploadImageToGCloud: blobInfo={}", blobInfo);

        try {
            storage.create(blobInfo, imageBytes);
            logger.info("uploadImageToGCloud: Image uploaded successfully to GCloud");
        }
        catch (com.google.cloud.storage.StorageException e) {
            logger.error("uploadImageToGCloud: StorageException during upload", e);
            throw new IOException("Error interacting with Cloud Storage: " + e.getMessage(), e);
        }
        catch (Exception e) {
            logger.error("uploadImageToGCloud: Unexpected error during upload", e);
            throw new IOException("Unexpected error uploading image: " + e.getMessage(), e);
        }

        String imageUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, blobId.getName());
        logger.info("uploadImageToGCloud: Generated imageUrl={}", imageUrl);

        // CRITICAL LOGS:
        logger.info("uploadImageToGCloud: bucketName={}", bucketName);
        logger.info("uploadImageToGCloud: blobId.getName()={}", blobId.getName());
        logger.info("uploadImageToGCloud: Final imageUrl={}", imageUrl);

        return imageUrl;
    }

    private String saveImageAndGenerateUrl(byte[] imageBytes) throws IOException {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        logger.info("saveImageAndGenerateUrl: Generated fileName={}", fileName); // Added log
        String imageUrl = uploadImageToGCloud(imageBytes, fileName);
        logger.info("saveImageAndGenerateUrl: imageUrl from uploadImageToGCloud={}", imageUrl); // Added log
        return imageUrl;
    }




    @PostMapping("/addtolist")
    public ResponseEntity<String> addToList(@RequestBody Item item, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }
        String username = auth.getName();
        if(userService.addItem(username, item)){
            return ResponseEntity.status(HttpStatus.CREATED).body("Item added");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding new item");
        }
    }

    @GetMapping("/getprofile")
    public ResponseEntity<UserData> getProfile(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String username = auth.getName();
        Optional<User> userOpt = userService.findByUsername(username);

        if (!userOpt.isPresent()) {
            // Consistent error handling
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User user = userOpt.get();

        // Map the User entity to the UserProfileDto
        UserData profileDto = new UserData(
                user.getUsername(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getLikedMovies(),
                user.getDislikedMovies(),
                user.getLikedSeries(),
                user.getDislikedSeries(),
                user.getSeenMovies(),
                user.getSeenSeries(),
                user.getFavMovies(),
                user.getFavSeries()
        );
        return ResponseEntity.ok(profileDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserData> updateProfile(@RequestBody UserData updatedUserData, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String username = auth.getName();
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User user = userOpt.get();

        if (updatedUserData.getUsername() != null) {
            user.setUsername(updatedUserData.getUsername());
        }
        if (updatedUserData.getEmail() != null) {
            user.setEmail(updatedUserData.getEmail());
        }
        
        try {
            userService.updateUser(user); // guarda los cambios en bbdd

            //para que el nuevo user pueda autenticar
            try {
                UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(user.getUsername());
                Authentication newAuth = new UsernamePasswordAuthenticationToken(
                        updatedUserDetails,
                        auth.getCredentials(),
                        updatedUserDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(newAuth);
                logger.info("SecurityContextHolder actualizado con nuevo username: {}", user.getUsername());

            } catch (Exception e) {
                logger.error("Error actualizando SecurityContextHolder", e);
            }

            //genera nuevo token para las nuevas credenciales
            String newUsername = user.getUsername();
            String roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            String newToken = jwtUtil.generateTokenFromUsername(newUsername, roles);

            UserData responseDto = new UserData(
                    user.getUsername(),
                    user.getEmail(),
                    user.getProfileImageUrl()
            );

            //incluye nuevo token en la respuesta
            return ResponseEntity.ok().header("Authorization", "Bearer " + newToken).body(responseDto);
        } catch (Exception e) {
            logger.error("Error updating profile for user: {}", username, e); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/profile/image")
    // Using ResponseEntity<?> as the return type because we return different types (DTO or String)
    public ResponseEntity<?> updateProfileImage(Authentication auth, @RequestPart("image") MultipartFile imageFile) {
        if (auth == null || !auth.isAuthenticated()) {
            // Return plain string error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }

        String username = auth.getName();
        Optional<User> userOpt = userService.findByUsername(username);

        if (!userOpt.isPresent()) {
            // Return plain string error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        User user = userOpt.get();

        try {
            byte[] imageBytes = imageFile.getBytes();
            logger.info("updateProfileImage: Image bytes obtained, size={}", imageBytes.length); // Added log
            String imageUrl = saveImageAndGenerateUrl(imageBytes);
            logger.info("updateProfileImage: imageUrl from saveImageAndGenerateUrl={}", imageUrl);

            if (imageUrl == null) {
                // Return plain string error
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen");
            }

            // Update the URL in the database
            logger.info("updateProfileImage: Calling userService.updateProfileImageUrl with imageUrl={}", imageUrl); // ADD THIS LOG
            userService.updateProfileImageUrl(user.getUsername(), imageUrl);
            logger.info("updateProfileImage: userService.updateProfileImageUrl() called successfully");



            // --- Success Case: Return JSON object using the dedicated success DTO ---
            ProfileImageUpdateResponse response = new ProfileImageUpdateResponse(imageUrl);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            logger.error("Error processing profile image for user " + username + ": " + e.getMessage(), e); // Log with logger and exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la imagen: " + e.getMessage());
        } catch (StorageException e) {  // Catch StorageException specifically (if applicable)
            logger.error("GCloud Storage error for user " + username + ": " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al interactuar con el almacenamiento en la nube: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error updating profile image for user " + username, e); // Log with logger and exception
            e.printStackTrace(); // Still print stack trace for thorough debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la URL de la imagen de perfil");
        }
    }
    
    private static class ImageUrlResponse {
        private String imageUrl;

        public ImageUrlResponse(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
    @GetMapping("/profile/image-url")
    public ResponseEntity<?> getProfileImageUrl(Authentication auth) {
        logger.info("getProfileImageUrl: Endpoint called");
        if (auth == null || !auth.isAuthenticated()) {
            logger.warn("getProfileImageUrl: Unauthorized access");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"No autorizado\"}");
        }
        String username = auth.getName();
        logger.info("getProfileImageUrl: username from auth: {}", username);
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            logger.warn("getProfileImageUrl: User not found for username: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Usuario no encontrado\"}");
        }
        User user = userOpt.get();
        logger.info("getProfileImageUrl: User found: {}", user);
        String imageUrl = user.getProfileImageUrl();
        logger.info("getProfileImageUrl: Image URL: {}", imageUrl);
        if (imageUrl == null || imageUrl.isEmpty()) {
            logger.warn("getProfileImageUrl: Image URL not found for user: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Image URL not found for user\"}");
        }
        logger.info("getProfileImageUrl: Returning image URL: {}", imageUrl);
        return ResponseEntity.ok(new ImageUrlResponse(imageUrl));
    }


    
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }

        String username = auth.getName();
        if (userService.deleteUser(username)) {
            return ResponseEntity.ok("Cuenta eliminada exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

}

