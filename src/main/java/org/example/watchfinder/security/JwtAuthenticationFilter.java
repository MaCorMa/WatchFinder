package org.example.watchfinder.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    //Cuando construyamos el filter chain, este filtro se lo vamos a pasar y es el que asegura que el user esté autenticado.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            //Aqui se parsea la request, saca el token, y luego el username mediante ese token. Con ese username obitene los datos del usuario
            String jwt = parseJwt(request);

            if(jwt != null && jwtUtil.validateToken(jwt)){
                String username = jwtUtil.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                //Aquí creamos el objeto este, le pasamos los datos del usuario, las credencialles en null porque ya hemos validado el token, y los roles (porque lo dice spring)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //Esto extrae datos adicionales, como la IP
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //Una vez está a punto, le decimos a Spring "para esta petición, este user está autenticado y tiene X permisos"
                //Como decirle al portero "viene conmigo"
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e){
            // MODIFICACIÓN: Imprimir la traza completa de la excepción para diagnosticar
            System.err.println("Error processing JWT authentication: " + e.getMessage());
            e.printStackTrace(); // Esto imprimirá la pila de llamadas completa en los logs del backend.
            // No lanzar la excepción aquí permite que la cadena de filtros continúe,
            // lo que eventualmente llevará al 401 por la regla .authenticated()
        }
        filterChain.doFilter(request, response);
    }

    //Esto es lo que verifica que las peticiones tengan en la cabecera el Bearer (token). Recupera el header de autorización, y
    //si tiene texto y empieza con "Bearer " (espacio final, ojito), saca lo que viene después, que debería ser el token. Y si no, null y pa tu casa.
    private String parseJwt(HttpServletRequest request) {

        String headerAuth = request.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
