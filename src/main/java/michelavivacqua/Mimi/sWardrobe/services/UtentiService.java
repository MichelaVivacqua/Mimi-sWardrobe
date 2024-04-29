package michelavivacqua.Mimi.sWardrobe.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import michelavivacqua.Mimi.sWardrobe.exceptions.BadRequestException;
import michelavivacqua.Mimi.sWardrobe.exceptions.NotFoundException;
import michelavivacqua.Mimi.sWardrobe.payloads.NewUtenteDTO;
import michelavivacqua.Mimi.sWardrobe.repositories.UtentiDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UtentiService {
    @Autowired
    private UtentiDAO utentiDAO;

    @Autowired
    private Cloudinary cloudinaryUploader;

    @Autowired
    private PasswordEncoder bcrypt;

    public UtentiService(UtentiDAO utentiDAO) {
        this.utentiDAO = utentiDAO;
    }

    public List<Utente> getUtentiList() {
        return utentiDAO.findAll();
    }



    public Utente saveUtente(NewUtenteDTO newUtenteDTO) {

        if (utentiDAO.existsByEmail(newUtenteDTO.email())) {
            throw new BadRequestException("L'email " + newUtenteDTO.email() + " è già in uso, quindi l'utente ha già un account!");
        }

        Utente utente = new Utente(newUtenteDTO.username(),newUtenteDTO.name(), newUtenteDTO.surname(), newUtenteDTO.email(), newUtenteDTO.propic(), bcrypt.encode(newUtenteDTO.password()),newUtenteDTO.ruolo());
        System.out.println(utente);
        return utentiDAO.save(utente);
    }


    public Utente findById(int id) {
        return utentiDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Utente findByIdAndUpdate(int id, Utente updatedUtente) {
        Utente found = findById(id);
        found.setName(updatedUtente.getName());
        found.setSurname(updatedUtente.getSurname());
        return utentiDAO.save(found);
    }

    public void findByIdAndDelete(int utenteId) {
        utentiDAO.deleteById(utenteId);
    }

    public String uploadImage(MultipartFile image) throws IOException {
        String url = (String) cloudinaryUploader.uploader().upload(image.getBytes(), ObjectUtils.emptyMap()).get("url");
        return url;
    }

    public Utente uploadUtenteImage(int utenteId, MultipartFile image, Utente utenteAutenticato) throws IOException { Utente found = utentiDAO.findById(utenteId)
            .orElseThrow(() -> new NotFoundException("Utente con ID " + utenteId + " non trovato!"));

        // Imposta la nuova propic solo se l'utente autenticato è lo stesso dell'utente che si sta cercando di modificare
        found.setPropic(this.uploadImage(image));
        return utentiDAO.save(found);
    }


    public Page<Utente> getUtenti(int page, int size, String sortBy){
        if(size > 70) size = 70;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.utentiDAO.findAll(pageable);
    }

    public Utente findByEmail(String email) {
        return utentiDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }

}
