package michelavivacqua.Mimi.sWardrobe.services;

import michelavivacqua.Mimi.sWardrobe.entities.Abbinamento;
import michelavivacqua.Mimi.sWardrobe.entities.Indumento;
import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import michelavivacqua.Mimi.sWardrobe.exceptions.NotFoundException;
import michelavivacqua.Mimi.sWardrobe.payloads.NewAbbinamentoDTO;
import michelavivacqua.Mimi.sWardrobe.repositories.AbbinamentiDAO;
import michelavivacqua.Mimi.sWardrobe.repositories.IndumentiDAO;
import michelavivacqua.Mimi.sWardrobe.repositories.UtentiDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AbbinamentiService {

    @Autowired
    private AbbinamentiDAO abbinamentiDAO;

    @Autowired
    private UtentiDAO utentiDAO;

    @Autowired
    private IndumentiDAO indumentiDAO;

    public AbbinamentiService(AbbinamentiDAO abbinamentiDAO) {
        this.abbinamentiDAO = abbinamentiDAO;
    }

    public List<Abbinamento> getAbbinamentiByUtenteId(int utenteId) {
        return abbinamentiDAO.findByUtenteId(utenteId);
    }

    public Abbinamento saveAbbinamento(NewAbbinamentoDTO newAbbinamentoDTO) {
        // Ottengo l'oggetto di autenticazione
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Estraggo l'ID dell'utente e ottengo l'utente dal DAO
        int utenteId = extractUtenteIdFromAuthentication(authentication);
        Utente utente = utentiDAO.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + utenteId));

        // Creo un set di indumenti utilizzando gli ID forniti nel DTO
        Set<Indumento> indumenti = new HashSet<>();
        for (int indumentoId : newAbbinamentoDTO.indumentiId()) {
            Indumento indumento = indumentiDAO.findById(indumentoId)
                    .orElseThrow(() -> new IllegalArgumentException("Indumento non trovato con ID: " + indumentoId));
            indumenti.add(indumento);
        }

        // Creo un nuovo abbinamento con l'utente e il set di indumenti
        Abbinamento abbinamento = new Abbinamento(utente, indumenti);

        // Salvo l'abbinamento nel repository e lo restituisco
        return abbinamentiDAO.save(abbinamento);
    }


    // Metodo per estrarre l'ID dell'utente dalla classe Utente
    private int extractUtenteIdFromAuthentication(Authentication authentication) {
        // Ottengo l'utente autenticato dall'oggetto di autenticazione
        Utente utente = (Utente) authentication.getPrincipal();
        return utente.getId();
    }


    public Abbinamento findById(int id) {
        return abbinamentiDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Abbinamento findByIdAndUpdate(int id, Abbinamento updatedAbbinamento) {
        Abbinamento found = findById(id);
        found.setIndumenti(updatedAbbinamento.getIndumenti());
        return abbinamentiDAO.save(found);
    }

    public void findByIdAndDelete(int abbinamentoId) {
        abbinamentiDAO.deleteById(abbinamentoId);
    }


    public Page<Abbinamento> getAbbinamenti(int page, int size, String sortBy){
        if(size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.abbinamentiDAO.findAll(pageable);
    }


}

