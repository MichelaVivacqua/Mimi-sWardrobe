package michelavivacqua.Mimi.sWardrobe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import michelavivacqua.Mimi.sWardrobe.enums.Colore;
import michelavivacqua.Mimi.sWardrobe.enums.Tipo;

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


    public Indumento(String image, Colore colore, Tipo tipo, Utente utente) {
        this.image = image;
        this.colore = colore;
        this.tipo = tipo;
        this.utente=utente;
    }
}
