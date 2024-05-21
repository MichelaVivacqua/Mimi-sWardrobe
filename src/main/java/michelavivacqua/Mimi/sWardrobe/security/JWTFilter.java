package michelavivacqua.Mimi.sWardrobe.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import michelavivacqua.Mimi.sWardrobe.exceptions.UnauthorizedException;
import michelavivacqua.Mimi.sWardrobe.services.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UtentiService utentiService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Controlliamo se nella richiesta corrente ci sia un Authorization Header
        String authHeader = request.getHeader("Authorization"); // Authorization Header --> Bearer ...

        // Se l'header è assente o non inizia con "Bearer ", lancia un'eccezione
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Per favore inserisci il token nell'Authorization Header");
        }

        // Estrai il token dall'header
        String accessToken = authHeader.substring(7);

        // Verifica il token (signature e expiration date)
        jwtTools.verifyToken(accessToken);

        // Cerco l'utente nel DB tramite id (l'id sta nel token)
        String id = jwtTools.extractIdFromToken(accessToken);
        int utenteId = Integer.parseInt(id);
        Utente currentUtente = this.utentiService.findById(utenteId);

        // Informo Spring Security su chi sia l'utente corrente
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUtente, null, currentUtente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Passa al prossimo elemento della catena
        filterChain.doFilter(request, response);
    }

    // Disabilito il filtro per richieste tipo Login, Register e Swagger
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Escludi dal filtro le richieste a /auth/**, /v3/api-docs e /swagger-ui.html
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return pathMatcher.match("/auth/**", request.getServletPath()) ||
                pathMatcher.match("/v3/api-docs", request.getServletPath()) ||
                pathMatcher.match("/swagger-ui.html", request.getServletPath());
    }
}
