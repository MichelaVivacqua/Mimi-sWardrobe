package michelavivacqua.Mimi.sWardrobe.repositories;

import michelavivacqua.Mimi.sWardrobe.entities.Indumento;
import michelavivacqua.Mimi.sWardrobe.enums.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndumentiDAO extends JpaRepository<Indumento, Integer> {
    boolean existsByTipo(Tipo tipo);
    Optional<IndumentiDAO> findBytipo(Tipo tipo);
    List<Indumento> findByUtenteId(int utenteId);

}
