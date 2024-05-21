package michelavivacqua.Mimi.sWardrobe.controllers;

import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import michelavivacqua.Mimi.sWardrobe.exceptions.BadRequestException;
import michelavivacqua.Mimi.sWardrobe.payloads.NewUtenteDTO;
import michelavivacqua.Mimi.sWardrobe.payloads.NewUtenteRespDTO;
import michelavivacqua.Mimi.sWardrobe.services.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/utenti")
public class UtentiController {
    @Autowired
    private UtentiService utentiService;


    //    1. POST http://localhost:3001/utenti (+ body)
    @Operation(summary = "Crea nuovo utente", responses = {
            @ApiResponse(responseCode = "201", description = "Creato con successo"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewUtenteRespDTO saveUtente(@Parameter(description = "creazione oggetto utente", required = true)@RequestBody @Validated NewUtenteDTO body, BindingResult validation){

        if(validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }
        System.out.println(body);
        return new NewUtenteRespDTO(this.utentiService.saveUtente(body).getId());}


    @Operation(summary = "Profilo dell'utente autenticato", responses = {
            @ApiResponse(responseCode = "200", description = "Profilo recuperato con successo")
    })
    @GetMapping("/me")
    public Utente getProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente){
        // @AuthenticationPrincipal mi consente di accedere all'utente attualmente autenticato
        // Questa cosa è resa possibile dal fatto che precedentemente a questo endpoint (ovvero nel JWTFilter)
        // ho estratto l'id dal token e sono andato nel db per cercare l'utente ed "associarlo" a questa richiesta
        return currentAuthenticatedUtente;
    }

    @Operation(summary = "Modifica il profilo dell'utente autenticato", responses = {
            @ApiResponse(responseCode = "200", description = "Profilo modificato con successo")
    })
    @PutMapping("/me")
    public Utente updateProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente, @RequestBody Utente updatedUtente){
        return this.utentiService.findByIdAndUpdate(currentAuthenticatedUtente.getId(), updatedUtente);
    }

    @Operation(summary = "Elimina il profilo dell'utente autenticato", responses = {
            @ApiResponse(responseCode = "204", description = "Profilo eliminato con successo")
    })
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente){
        this.utentiService.findByIdAndDelete(currentAuthenticatedUtente.getId());
    }


    // 2. GET http://localhost:3001/utenti/{{utenteId}}
    @Operation(summary = "Trova l'utente mediante l'ID", responses = {
            @ApiResponse(responseCode = "200", description = "Utente trovato con successo"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato")
    })
    @GetMapping("/{utenteId}")
    private Utente findUtenteById(@Parameter(description = "ID dell'utente da cercare", required = true)@PathVariable int utenteId){
        return this.utentiService.findById(utenteId);
    }

    //    3. GET http://localhost:3001/utenti
    @Operation(summary = "Trova tutti gli utenti", responses = {
            @ApiResponse(responseCode = "200", description = "Utenti trovati con successo")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // PreAuthorize serve per poter dichiarare delle regole di accesso
    // all'endpoint basandoci sul ruolo dell'utente. In questo caso solo gli ADMIN possono accedere
    public List<Utente> getAllUtenti(){
        return this.utentiService.getUtentiList();
    }

    //    3.1 Paginazione e ordinamento http://localhost:3001/utenti/page
    @Operation(summary = "Trova tutti gli utenti con paginazione e ordinamento", responses = {
            @ApiResponse(responseCode = "200", description = "Utenti trovati con successo")
    })
    @GetMapping("/page")
    public Page<Utente> getAllUtenti( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size,@RequestParam(defaultValue = "id") String sortBy) {

        return this.utentiService.getUtenti(page, size, sortBy);
    }


    // 4. PUT http://localhost:3001/utenti/{{utenteId}} (+ body)
    @Operation(summary = "Modifica un utente mediante ID", responses = {
            @ApiResponse(responseCode = "200", description = "Utente modificato con successo")
    })
    @PutMapping("/{utenteId}")
    public Utente findByIdAndUpdate(@Parameter(description = "ID dell'utente da modificare", required = true)@PathVariable int utenteId, @RequestBody Utente body){
        return this.utentiService.findByIdAndUpdate(utenteId, body);
    }



    // 5. DELETE http://localhost:3001/utenti/{utenteId}
    @Operation(summary = "Elimina un utente mediante ID", responses = {
            @ApiResponse(responseCode = "204", description = "Utente eliminato con successo")
    })
    @DeleteMapping("/{utenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUtenteById(@Parameter(description  = "ID of the user to delete", required = true)@PathVariable int utenteId) {
        this.utentiService.findByIdAndDelete(utenteId);
    }



    //    UPLOAD DI FOTO PER UTENTE
    //   POST http://localhost:3001/utenti/upload + authorization con bear token dell'utente di cui si vuole cambiare l'immagine del profilo
    @Operation(summary = "Carica immagine del profilo per l'utente autenticato", responses = {
            @ApiResponse(responseCode = "200", description = "Immagine caricata con successo")
    })
    @PostMapping("/upload")
    public Utente uploadUtenteImage(Authentication authentication,@Parameter(description = "Profile picture file", required = true) @RequestParam("propic") MultipartFile image) throws IOException {
        // Ottengo l'utente autenticato
        Utente utenteAutenticato = (Utente) authentication.getPrincipal();

        // Passo l'utente autenticato, l'immagine e l'ID dell'utente autenticato al service per l'aggiornamento della propic
        return this.utentiService.uploadUtenteImage(utenteAutenticato.getId(), image, utenteAutenticato);
    }




}
