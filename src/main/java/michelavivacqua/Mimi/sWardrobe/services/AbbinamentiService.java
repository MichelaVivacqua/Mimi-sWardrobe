package michelavivacqua.Mimi.sWardrobe.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import michelavivacqua.Mimi.sWardrobe.entities.Abbinamento;
import michelavivacqua.Mimi.sWardrobe.entities.Indumento;
import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import michelavivacqua.Mimi.sWardrobe.enums.Colore;
import michelavivacqua.Mimi.sWardrobe.enums.Tipo;
import michelavivacqua.Mimi.sWardrobe.payloads.NewAbbinamentoDTO;
import michelavivacqua.Mimi.sWardrobe.payloads.NewIndumentoDTO;
import michelavivacqua.Mimi.sWardrobe.repositories.AbbinamentiDAO;
import michelavivacqua.Mimi.sWardrobe.repositories.IndumentiDAO;
import michelavivacqua.Mimi.sWardrobe.repositories.UtentiDAO;
import michelavivacqua.Mimi.sWardrobe.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public List<Abbinamento> getAbbinamentiList() {
        return abbinamentiDAO.findAll();
    }


//    public Abbinamento saveAbbinamento(NewAbbinamentoDTO newAbbinamentoDTO) {
//        Set<Integer> indumentiIds = newAbbinamentoDTO.indumenti();
//        Set<Indumento> indumenti = new HashSet<>();
//
//        for (Integer indumentoId : indumentiIds) {
//            Indumento indumento = indumentiDAO.findById(indumentoId)
//                    .orElseThrow(() -> new IllegalArgumentException("Indumento non trovato con ID: " + indumentoId));
//            indumenti.add(indumento);
//        }
//
//        Abbinamento abbinamento = new Abbinamento(indumenti);
//        System.out.println(abbinamento);
//        return abbinamentiDAO.save(abbinamento);
//    }

    public Abbinamento saveAbbinamento(NewAbbinamentoDTO newAbbinamentoDTO) {
        // Ottengo l'oggetto di autenticazione
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Estraggo l'ID dell'utente e ottengo l'utente dal DAO
        int utenteId = extractUtenteIdFromAuthentication(authentication);
        Utente utente = utentiDAO.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + utenteId));

        // Creo un nuovo abbinamento e assegno l'utente
        Set<Indumento> indumenti = new HashSet<>();
        for (Integer indumentoId : newAbbinamentoDTO.indumenti()) {
            Indumento indumento = indumentiDAO.findById(indumentoId)
                    .orElseThrow(() -> new IllegalArgumentException("Indumento non trovato con ID: " + indumentoId));
            indumenti.add(indumento);
        }
        Abbinamento abbinamento = new Abbinamento(indumenti);
        abbinamento.setUtente(utente);

        // Salvo l'abbinamento
        System.out.println(abbinamento);
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
        indumentiDAO.deleteById(abbinamentoId);
    }


    public Page<Abbinamento> getAbbinamenti(int page, int size, String sortBy){
        if(size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.abbinamentiDAO.findAll(pageable);
    }


    public List<Abbinamento> getAbbinamentiByUtenteId(int utenteId) {
        return abbinamentiDAO.findByUtenteId(utenteId);
    }


}
