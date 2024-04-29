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
        // Il codice di questo metodo verrà eseguito ad ogni richiesta che richieda di essere autenticati
        // Cose da fare:

        // 1. Controlliamo se nella richiesta corrente ci sia un Authorization Header, se non c'è --> 401
        String authHeader = request.getHeader("Authorization"); // Authorization Header --> Bearer ...

        if(authHeader == null || !authHeader.startsWith("Bearer ")) throw new UnauthorizedException("Per favore inserisci il token nell'Authorization Header");

        // 2. Se c'è estraiamo il token dall'header
        String accessToken = authHeader.substring(7);

        // 3. Verifichiamo se il token è stato manipolato (verifica della signature) e se non è scaduto (verifica Expiration Date)
        jwtTools.verifyToken(accessToken);

        // 4. Se tutto è OK andiamo al prossimo elemento della Filter Chain, per prima o poi arrivare all'endpoint

        // 4.1 Cerco l'utente nel DB tramite id (l'id sta nel token..)
        String id = jwtTools.extractIdFromToken(accessToken);
        int utenteId = Integer.parseInt(id);
        Utente currentUtente = this.utentiService.findById(utenteId);

        // 4.2 Devo informare Spring Security su chi sia l'utente corrente che sta effettuando la richiesta. In qualche maniera
        // equivale ad "associare" l'utente alla richiesta corrente
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUtente, null, currentUtente.getAuthorities());
        // OBBLIGATORIO il terzo parametro con la lista ruoli dell'utente se si vuol poter usare i vari @PreAuthorize
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4.3 Vado al prossimo elemento della catena, passandogli gli oggetti request e response

        filterChain.doFilter(request, response); // Vado al prossimo elemento della catena, passandogli gli oggetti request e response
        // 5. Se il token non fosse OK --> 401
    }

    // Sovrascrivendo il seguente metodo disabilito il filtro per determinate richieste tipo Login o Register (esse ovviamente non devono richiedere un token!)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        // Uso questo metodo per specificare in che situazioni NON FILTRARE
        // Se l'URL della richiesta corrente corrisponde a /auth/qualsiasicosa allora non entrare in azione
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
