package michelavivacqua.Mimi.sWardrobe.services;

import michelavivacqua.Mimi.sWardrobe.entities.Indumento;
import michelavivacqua.Mimi.sWardrobe.payloads.NewIndumentoDTO;
import michelavivacqua.Mimi.sWardrobe.repositories.IndumentiDAO;
import michelavivacqua.Mimi.sWardrobe.repositories.UtentiDAO;
import michelavivacqua.Mimi.sWardrobe.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndumentiService {

    @Autowired
    private IndumentiDAO indumentiDAO;

    @Autowired
    private UtentiDAO utentiDAO;

    public IndumentiService(IndumentiDAO indumentiDAO) {
        this.indumentiDAO = indumentiDAO;
    }

    public List<Indumento> getIndumentiList() {
        return indumentiDAO.findAll();
    }



    public Indumento saveIndumento(NewIndumentoDTO newIndumentoDTO) {

        Indumento indumento = new Indumento(newIndumentoDTO.image(),newIndumentoDTO.colore(),newIndumentoDTO.tipo());
        System.out.println("Sto salvando l'indumento come:"+ indumento);
        return indumentiDAO.save(indumento);
    }



    public Indumento findById(int id) {
        return indumentiDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Indumento findByIdAndUpdate(int id, Indumento updatedIndumento) {
        Indumento found = findById(id);
        found.setImage(updatedIndumento.getImage());
        found.setColore(updatedIndumento.getColore());
        found.setTipo(updatedIndumento.getTipo());
        return indumentiDAO.save(found);
    }

    public void findByIdAndDelete(int indumentoId) {
        indumentiDAO.deleteById(indumentoId);
    }


    public Page<Indumento> getIndumenti(int page, int size, String sortBy){
        if(size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.indumentiDAO.findAll(pageable);
    }
}
