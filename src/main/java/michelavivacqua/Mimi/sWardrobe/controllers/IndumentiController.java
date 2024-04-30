package michelavivacqua.Mimi.sWardrobe.controllers;

import michelavivacqua.Mimi.sWardrobe.entities.Indumento;
import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import michelavivacqua.Mimi.sWardrobe.enums.Colore;
import michelavivacqua.Mimi.sWardrobe.enums.Tipo;
import michelavivacqua.Mimi.sWardrobe.exceptions.BadRequestException;
import michelavivacqua.Mimi.sWardrobe.payloads.NewIndumentoDTO;
import michelavivacqua.Mimi.sWardrobe.payloads.NewIndumentoRespDTO;
import michelavivacqua.Mimi.sWardrobe.services.IndumentiService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/indumenti")
public class IndumentiController {
    @Autowired
    private IndumentiService indumentiService;


    //    1. POST http://localhost:3001/indumenti (+ body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewIndumentoRespDTO saveIndumento(@RequestBody @Validated NewIndumentoDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }
//        Integer utenteId = body.utenteId();
        System.out.println(body);
        return new NewIndumentoRespDTO(this.indumentiService.saveIndumento(body).getId());
    }


    // 2. GET http://localhost:3001/indumenti/{{indumentoId}}
    @GetMapping("/{indumentoId}")
    private Indumento findIndumentoById(@PathVariable int indumentoId){
        return this.indumentiService.findById(indumentoId);
    }

    //    3. GET http://localhost:3001/indumenti
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Indumento> getAllIndumento(){
        return this.indumentiService.getIndumentiList();
    }

    //     GET http://localhost:3001/indumenti/miei
    @GetMapping("/miei")
    @PreAuthorize("hasAuthority('USER')")
    public List<Indumento> getIndumentiUtente(Authentication authentication) {
        Utente utenteAutenticato = (Utente) authentication.getPrincipal();
        int utenteId = utenteAutenticato.getId();
        return indumentiService.getIndumentiByUtenteId(utenteId);
    }


    //    3.1 Paginazione e ordinamento http://localhost:3001/indumenti/page
    @GetMapping("/page")
    public Page<Indumento> getAllIndumenti(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "1") int size,
                                             @RequestParam(defaultValue = "id") String sortBy) {
        return this.indumentiService.getIndumenti(page, size, sortBy);
    }

    // 4. PUT http://localhost:3001/indumenti/{{indumentoId}} (+ body)
    @PutMapping("/{indumentoId}")
    private Indumento findByIdAndUpdate(@PathVariable int indumentoId, @RequestBody Indumento body){
        return this.indumentiService.findByIdAndUpdate(indumentoId, body);
    }



    // 5. DELETE http://localhost:3001/indumenti/{indumentoId}
    @DeleteMapping("/{indumentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIndumentoById(@PathVariable int indumentoId) {
        this.indumentiService.findByIdAndDelete(indumentoId);
    }


//        UPLOAD DI FOTO PER INDUMENTO
//   POST http://localhost:3001/indumenti/upload/{indumentoId}
    @PostMapping("/upload/{indumentoId}")
    public Indumento uploadImage (@RequestParam("image") MultipartFile image, @PathVariable int indumentoId) throws IOException {
        return this.indumentiService.uploadIndumentoImage(image,indumentoId);
    }

//    GET http://localhost:3001/indumenti/miei/colore?colore=BLU
    @GetMapping("/miei/colore")
    public List<Indumento> getIndumentiPerColore(Authentication authentication, @RequestParam Colore colore) {
        Utente utenteAutenticato = (Utente) authentication.getPrincipal();
        int utenteId = utenteAutenticato.getId();
        return indumentiService.getIndumentiByUtenteIdAndColore(utenteId, colore);
    }

    //    GET http://localhost:3001/indumenti/miei/tipo?tipo=PANTALONE
    @GetMapping("miei/tipo")
    public List<Indumento> getIndumentiPerTipo(Authentication authentication, @RequestParam Tipo tipo) {
        Utente utenteAutenticato = (Utente) authentication.getPrincipal();
        int utenteId = utenteAutenticato.getId();
        return indumentiService.getIndumentiByUtenteIdAndTipo(utenteId, tipo);
    }



}
