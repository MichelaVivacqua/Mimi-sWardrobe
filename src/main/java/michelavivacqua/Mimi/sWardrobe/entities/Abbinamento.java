package michelavivacqua.Mimi.sWardrobe.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Abbinamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @ManyToMany
    @JoinTable(
            name = "abbinamento_indumento",
            joinColumns = @JoinColumn(name = "abbinamento_id"),
            inverseJoinColumns = @JoinColumn(name = "indumento_id")
    )
    private Set<Indumento> indumenti = new HashSet<>();

    public Abbinamento(Utente utente, Set<Indumento> indumenti) {
        this.utente = utente;
        this.indumenti = indumenti;
    }
}

