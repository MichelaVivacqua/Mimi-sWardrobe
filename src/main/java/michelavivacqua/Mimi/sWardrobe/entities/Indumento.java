package michelavivacqua.Mimi.sWardrobe.entities;

import jakarta.persistence.*;
import lombok.*;
import michelavivacqua.Mimi.sWardrobe.enums.Colore;
import michelavivacqua.Mimi.sWardrobe.enums.Tipo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Indumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String image;

    @Enumerated(EnumType.STRING)
    private Colore colore;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    public Indumento(String image, Colore colore, Tipo tipo) {
        this.image = image;
        this.colore = colore;
        this.tipo = tipo;
    }



}
