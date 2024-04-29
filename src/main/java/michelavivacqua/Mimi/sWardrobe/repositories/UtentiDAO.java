package michelavivacqua.Mimi.sWardrobe.repositories;

import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtentiDAO extends JpaRepository<Utente, Integer> {
    boolean existsByEmail(String email);
    Optional<Utente> findByEmail(String nome);

}
