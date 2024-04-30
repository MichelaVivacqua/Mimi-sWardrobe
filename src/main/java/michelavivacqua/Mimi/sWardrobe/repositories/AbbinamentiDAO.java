package michelavivacqua.Mimi.sWardrobe.repositories;

import michelavivacqua.Mimi.sWardrobe.entities.Abbinamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbbinamentiDAO extends JpaRepository<Abbinamento, Integer> {
    List<Abbinamento> findByUtenteId(int utenteId);
}
