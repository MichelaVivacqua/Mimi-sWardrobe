package michelavivacqua.Mimi.sWardrobe.controllers;

import michelavivacqua.Mimi.sWardrobe.entities.Abbinamento;
import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import michelavivacqua.Mimi.sWardrobe.exceptions.BadRequestException;
import michelavivacqua.Mimi.sWardrobe.payloads.NewAbbinamentoDTO;
import michelavivacqua.Mimi.sWardrobe.payloads.NewAbbinamentoRespDTO;
import michelavivacqua.Mimi.sWardrobe.payloads.RatingDTO;
import michelavivacqua.Mimi.sWardrobe.repositories.AbbinamentiDAO;
import michelavivacqua.Mimi.sWardrobe.services.AbbinamentiService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/abbinamenti")
public class AbbinamentiController {
    @Autowired
    private AbbinamentiService abbinamentiService;

    @Autowired
    private AbbinamentiDAO abbinamentiDAO;


    //    1. POST http://localhost:3001/abbinamenti (+ body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewAbbinamentoRespDTO saveAbbinamento(@RequestBody @Validated NewAbbinamentoDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }
        System.out.println(body);
        return new NewAbbinamentoRespDTO(this.abbinamentiService.saveAbbinamento(body).getId());
    }


    // 2. GET http://localhost:3001/abbinamenti/{{abbinamentoId}}
    @GetMapping("/{abbinamentoId}")
    private Abbinamento findAbbinamentoById(@PathVariable int abbinamentoId){
        return this.abbinamentiService.findById(abbinamentoId);
    }

    //    3. GET http://localhost:3001/abbinamenti
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Abbinamento> getAllAbbinamento(){
        return this.abbinamentiService.getAbbinamentiList();
    }

    //     GET http://localhost:3001/abbinamenti/miei
    @GetMapping("/miei")
    @PreAuthorize("hasAuthority('USER')")
    public List<Abbinamento> getAbbinamentiUtente(Authentication authentication) {
        Utente utenteAutenticato = (Utente) authentication.getPrincipal();
        int utenteId = utenteAutenticato.getId();
        return abbinamentiService.getAbbinamentiByUtenteId(utenteId);
    }

    
    //    3.1 Paginazione e ordinamento http://localhost:3001/abbinamenti/page
    @GetMapping("/page")
    public Page<Abbinamento> getAllAbbinamenti(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "1") int size,
                                           @RequestParam(defaultValue = "id") String sortBy) {
        return this.abbinamentiService.getAbbinamenti(page, size, sortBy);
    }

    // 4. PUT http://localhost:3001/abbinamenti/{{abbinamentoId}} (+ body)
    @PutMapping("/{abbinamentoId}")
    private Abbinamento findByIdAndUpdate(@PathVariable int abbinamentoId, @RequestBody Abbinamento body){
        return this.abbinamentiService.findByIdAndUpdate(abbinamentoId, body);
    }



    // 5. DELETE http://localhost:3001/abbinamenti/{abbinamentoId}
    @DeleteMapping("/{abbinamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAbbinamentoById(@PathVariable int abbinamentoId) {
        this.abbinamentiService.findByIdAndDelete(abbinamentoId);
    }


    @PutMapping("/{abbinamentoId}/indossato")
    public ResponseEntity<Abbinamento> markAsWorn(@PathVariable int abbinamentoId) {
        return abbinamentiDAO.findById(abbinamentoId)
                .map(abbinamento -> {
            abbinamento.setIndossato(true);
            abbinamento.setDataIndossato(LocalDate.now());
            abbinamentiDAO.save(abbinamento);
            return ResponseEntity.ok(abbinamento);
        })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{abbinamentoId}/rate")
    public ResponseEntity<Abbinamento> rateAbbinamento(@PathVariable int abbinamentoId, @RequestBody @Validated RatingDTO ratingDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation error");
        }

        Abbinamento updatedAbbinamento = abbinamentiService.rateAbbinamento(abbinamentoId, ratingDTO.valutazione());
        return ResponseEntity.ok(updatedAbbinamento);
    }


}
