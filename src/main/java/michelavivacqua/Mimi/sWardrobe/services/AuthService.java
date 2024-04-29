package michelavivacqua.Mimi.sWardrobe.services;

import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import michelavivacqua.Mimi.sWardrobe.exceptions.UnauthorizedException;
import michelavivacqua.Mimi.sWardrobe.payloads.UtenteLoginDTO;
import michelavivacqua.Mimi.sWardrobe.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UtentiService utentiService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUserAndGenerateToken(UtenteLoginDTO payload){
        // 1. Controllo le credenziali
        // 1.1 Cerco nel db tramite l'email l'utente
        Utente utente = this.utentiService.findByEmail(payload.email());
        // 1.2 Verifico se la password combacia con quella ricevuta nel payload
        if (bcrypt.matches(payload.password(), utente.getPassword())) {
            // 2. Se è tutto OK, genero un token e lo torno
            return jwtTools.createToken(utente);
        } else {
            // 3. Se le credenziali invece non fossero OK --> 401 (Unauthorized)
            throw new UnauthorizedException("Credenziali non valide! Effettua di nuovo il login!");
        }


    }
}
