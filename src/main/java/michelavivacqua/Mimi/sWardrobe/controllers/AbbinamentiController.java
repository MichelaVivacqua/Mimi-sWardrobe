package michelavivacqua.Mimi.sWardrobe.controllers;

import jakarta.validation.Valid;
import michelavivacqua.Mimi.sWardrobe.entities.Abbinamento;
import michelavivacqua.Mimi.sWardrobe.payloads.NewAbbinamentoDTO;
import michelavivacqua.Mimi.sWardrobe.services.AbbinamentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/abbinamenti")
public class AbbinamentiController {

    @Autowired
    private AbbinamentiService abbinamentiService;

    public AbbinamentiController(AbbinamentiService abbinamentiService) {
        this.abbinamentiService = abbinamentiService;
    }

    @GetMapping("/utente/{utenteId}")
    public List<Abbinamento> getAbbinamentiUtente(@PathVariable int utenteId) {
        return abbinamentiService.getAbbinamentiByUtenteId(utenteId);
    }

    //    1. POST http://localhost:3001/abbinamenti (+ body)
    @PostMapping
    public ResponseEntity<Abbinamento> creaNuovoAbbinamento(@RequestBody @Valid NewAbbinamentoDTO newAbbinamentoDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        // Ottieni l'oggetto di autenticazione
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Crea il nuovo abbinamento
        Abbinamento nuovoAbbinamento = abbinamentiService.saveAbbinamento(newAbbinamentoDTO);

        // Ritorna la risposta con l'abbinamento creato e lo status code appropriato
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovoAbbinamento);
    }

}

