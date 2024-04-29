package michelavivacqua.Mimi.sWardrobe.controllers;

import michelavivacqua.Mimi.sWardrobe.exceptions.BadRequestException;
import michelavivacqua.Mimi.sWardrobe.payloads.NewUtenteDTO;
import michelavivacqua.Mimi.sWardrobe.payloads.NewUtenteRespDTO;
import michelavivacqua.Mimi.sWardrobe.payloads.UtenteLoginDTO;
import michelavivacqua.Mimi.sWardrobe.payloads.UtenteLoginRespDTO;
import michelavivacqua.Mimi.sWardrobe.services.AuthService;
import michelavivacqua.Mimi.sWardrobe.services.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UtentiService utentiService;

//    POST http://localhost:3001/auth/login
    @PostMapping("/login")
    public UtenteLoginRespDTO login(@RequestBody UtenteLoginDTO payload){
        return new UtenteLoginRespDTO(this.authService.authenticateUserAndGenerateToken(payload));
    }

//    POST http://localhost:3001/auth/register
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUtenteRespDTO saveUser(@RequestBody @Validated NewUtenteDTO body, BindingResult validation){
        // @Validated valida il payload in base ai validatori utilizzati nella classe NewUserDTO
        // BindingResult validation ci serve per valutare il risultato di questa validazione
        if(validation.hasErrors()) { // Se ci sono stati errori di validazione dovrei triggerare un 400 Bad Request
            throw new BadRequestException(validation.getAllErrors()); // Inviamo la lista degli errori all'Error Handler opportuno
        }
        // Altrimenti se non ci sono stati errori posso salvare tranquillamente lo user
        return new NewUtenteRespDTO(this.utentiService.saveUtente(body).getId());
    }

}
