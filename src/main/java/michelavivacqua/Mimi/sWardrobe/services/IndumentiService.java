package michelavivacqua.Mimi.sWardrobe.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import michelavivacqua.Mimi.sWardrobe.entities.Indumento;
import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import michelavivacqua.Mimi.sWardrobe.enums.Colore;
import michelavivacqua.Mimi.sWardrobe.enums.Tipo;
import michelavivacqua.Mimi.sWardrobe.payloads.NewImageIndumentoDTO;
import michelavivacqua.Mimi.sWardrobe.payloads.NewIndumentoDTO;
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
import java.util.List;

@Service
public class IndumentiService {

    @Autowired
    private IndumentiDAO indumentiDAO;

    @Autowired
    private UtentiDAO utentiDAO;

    @Autowired
    private Cloudinary cloudinaryUploader;

    public IndumentiService(IndumentiDAO indumentiDAO) {
        this.indumentiDAO = indumentiDAO;
    }

    public List<Indumento> getIndumentiList() {
        return indumentiDAO.findAll();
    }

    public Indumento saveIndumento(NewIndumentoDTO newIndumentoDTO) {
        // Ottengo l'oggetto di autenticazione
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Estraggo l'ID dell'utente e ottengo l'utente dal DAO
        int utenteId = extractUtenteIdFromAuthentication(authentication);
        Utente utente = utentiDAO.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + utenteId));

        // Creo un nuovo indumento e assegno l'utente
        Indumento indumento = new Indumento(
                newIndumentoDTO.id(),
                newIndumentoDTO.image(),
                newIndumentoDTO.colore(),
                newIndumentoDTO.tipo(),
                utente
        );
        System.out.println(indumento);
        System.out.println(newIndumentoDTO);
        return indumentiDAO.save(indumento);
    }

    // Metodo per estrarre l'ID dell'utente dalla classe Utente
    private int extractUtenteIdFromAuthentication(Authentication authentication) {
        // Ottengo l'utente autenticato dall'oggetto di autenticazione
        Utente utente = (Utente) authentication.getPrincipal();
        return utente.getId();
    }


    public Indumento findById(String id) {
        return indumentiDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Indumento findByIdAndUpdate(String id, Indumento updatedIndumento) {
        Indumento found = findById(id);
        found.setImage(updatedIndumento.getImage());
        found.setColore(updatedIndumento.getColore());
        found.setTipo(updatedIndumento.getTipo());
        return indumentiDAO.save(found);
    }

    public void findByIdAndDelete(String indumentoId) {
        indumentiDAO.deleteById(indumentoId);
    }


    public Page<Indumento> getIndumenti(int page, int size, String sortBy){
        if(size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.indumentiDAO.findAll(pageable);
    }

    public String uploadImage(MultipartFile image) throws IOException {
        String url = (String) cloudinaryUploader.uploader().upload(image.getBytes(), ObjectUtils.emptyMap()).get("url");
        return url;
    }

    public NewImageIndumentoDTO uploadIndumentoImage(MultipartFile image, String indumentoId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Utente authenticatedUser = (Utente) authentication.getPrincipal();

//        Indumento found = findById(indumentoId);
//        if (found.getUtente().getId() != authenticatedUser.getId()) {
//            throw new IllegalArgumentException("Non sei autorizzato a caricare un'immagine per questo capo d'abbigliamento..");
//        }

//        found.setImage(uploadImage(image));
//        indumentiDAO.save(found);
        return new NewImageIndumentoDTO (uploadImage(image));
    }


    public List<Indumento> getIndumentiByUtenteId(int utenteId) {
        return indumentiDAO.findByUtenteId(utenteId);
    }

    public List<Indumento> getIndumentiByUtenteIdAndColore(int utenteId, Colore colore) {
        return indumentiDAO.findByUtenteIdAndColore(utenteId, colore);
    }

    public List<Indumento> getIndumentiByUtenteIdAndTipo(int utenteId, Tipo tipo) {
        return indumentiDAO.findByUtenteIdAndTipo(utenteId, tipo);
    }

}
