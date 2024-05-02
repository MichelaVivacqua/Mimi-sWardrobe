package michelavivacqua.Mimi.sWardrobe.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import michelavivacqua.Mimi.sWardrobe.enums.Colore;
import michelavivacqua.Mimi.sWardrobe.enums.Tipo;
import org.hibernate.validator.constraints.URL;

public record NewIndumentoDTO(
        @NotEmpty(message = "L'immagine dell'indumento è obbligatoria")
        @URL(message="L'URL inserito non è valido")
        String image,

        @NotNull(message = "È obbligatorio inserire il colore")
        Colore colore,

        @NotNull(message = "È obbligatorio inserire il tipo")
        Tipo tipo
) {
}
