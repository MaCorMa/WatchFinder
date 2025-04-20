package org.example.watchfinder.controller;

import org.example.watchfinder.dto.LoginRequest;
import org.example.watchfinder.dto.LoginResponse;
import org.example.watchfinder.dto.PasswordResetRequest;
import org.example.watchfinder.dto.RegisterRequest;
import org.example.watchfinder.model.User;
import org.example.watchfinder.security.JwtUtil;
import org.example.watchfinder.security.UserDetailsServiceImpl;
import org.example.watchfinder.service.EmailService;
import org.example.watchfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserService userService,
                          PasswordEncoder passwordEncoder, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(authentication);
            return ResponseEntity.ok(new LoginResponse(token));
        }  catch (BadCredentialsException e) {
            // Si las credenciales son incorrectas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid username or password");
        } catch (Exception e) {
            // Cualquier otro error durante la autenticación
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Authentication failed - " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {

        if(userService.existsByUsername(registerRequest.getUsername()) || userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Username is already in use");
        }

        User user = new User(registerRequest.getUsername(), passwordEncoder.encode(registerRequest.getPassword()), registerRequest.getEmail());
        Set<String> defaultRoles = new HashSet<>();
        defaultRoles.add("USER");
        user.setRoles(defaultRoles);

        try{
            userService.registerUser(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(Authentication authentication) {
        // Si este método se ejecuta, el filtro JWT ya validó el token.
        // Authentication no será null y estará 'authenticated'.
        // Simplemente devolvemos OK. Opcionalmente, datos del usuario.
        String username = authentication.getName(); // Obtener username del token validado
        // Puedes devolver solo OK, o un objeto con datos básicos.
        return ResponseEntity.ok(Map.of("status", "valid", "username", username));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequest resetRequest) {
        try {
            Optional<User> user = userService.findByEmail(resetRequest.getEmail());

            if (!user.isPresent()) {
                // por seguridad no indica si el mail existe o no
                return ResponseEntity.ok().body("Si el email existe, recibirás un correo con las instrucciones.");
            }

            //genera token de autenticacion
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.get().getUsername(),
                    null,
                    user.get().getAuthorities()
            );
            //genera token de reseteo
            String token = jwtUtil.generateToken(authentication);

            // envía mail con el link y token
            String resetUrl = "https://WatchFinder.com/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(resetRequest.getEmail(), resetUrl);

            return ResponseEntity.ok().body("Instrucciones para restablecer contraseña enviadas al e-mail indicado");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Petición de restablecimiento de contraseña ha fallado - " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest resetRequest) {
        try {
            //valida token y caducidad
            if (!jwtUtil.validateToken(resetRequest.getToken())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Error: Token inválido o expirado");
            }

            String username = jwtUtil.getUserNameFromJwtToken(resetRequest.getToken());
            Optional<User> user = userService.findByUsername(username);

            if (!user.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Error: User not found");
            }

            // reset contraseña
            user.get().setPassword(passwordEncoder.encode(resetRequest.getNewPassword()));
            userService.updateUser(user.get());

            // nuevo token autenticación
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    user.get().getAuthorities()
            );

            // nuevo token jwt
            String newToken = jwtUtil.generateToken(authentication);
            return ResponseEntity.ok(new LoginResponse(newToken));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Password reset failed - " + e.getMessage());
        }
    }
}
