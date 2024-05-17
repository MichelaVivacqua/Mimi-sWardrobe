package michelavivacqua.Mimi.sWardrobe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import michelavivacqua.Mimi.sWardrobe.enums.Colore;
import michelavivacqua.Mimi.sWardrobe.enums.Tipo;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "utente")
@Entity
public class Indumento {
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String image;

    @Enumerated(EnumType.STRING)
    private Colore colore;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @JsonIgnore
    @ManyToMany(mappedBy = "indumenti", cascade = CascadeType.REMOVE)
    private Set<Abbinamento> abbinamenti = new HashSet<>();


    public Indumento(String image, Colore colore, Tipo tipo, Utente utente) {
        this.image = image;
        this.colore = colore;
        this.tipo = tipo;
        this.utente=utente;
    }

    // Costruttore personalizzato
    public Indumento(String id, String image, Colore colore, Tipo tipo, Utente utente) {
        this.id = id;
        this.image = image;
        this.colore = colore;
        this.tipo = tipo;
        this.utente = utente;
    }

    @PreRemove
    private void preRemove() {
        for (Abbinamento abbinamento : abbinamenti) {
            abbinamento.getIndumenti().remove(this);
            if (abbinamento.getIndumenti().isEmpty()) {
                abbinamento.getUtente().getAbbinamenti().remove(abbinamento);
            }
        }
    }
}

