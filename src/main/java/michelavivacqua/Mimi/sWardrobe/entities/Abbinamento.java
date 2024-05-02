package michelavivacqua.Mimi.sWardrobe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Abbinamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "abbinamento_indumento",
            joinColumns = @JoinColumn(name = "abbinamento_id"),
            inverseJoinColumns = @JoinColumn(name = "indumento_id")
    )
    private Set<Indumento> indumenti = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    public Abbinamento(Set<Indumento> indumenti) {
        this.indumenti=indumenti;
    }
}