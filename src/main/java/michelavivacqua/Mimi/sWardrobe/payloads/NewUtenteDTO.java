package michelavivacqua.Mimi.sWardrobe.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import michelavivacqua.Mimi.sWardrobe.enums.Ruolo;
import org.hibernate.validator.constraints.URL;

public record NewUtenteDTO(
        @NotEmpty(message = "È obbligatorio avere un username")
        @Size(min = 8, max = 30, message = "L'username deve essere compreso tra gli 8 e i 30 caratteri")
        String username,
        @NotEmpty(message = "Il nome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il nome deve essere compreso tra i 3 e i 30 caratteri")
        String name,
        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il cognome deve essere compreso tra i 3 e i 30 caratteri")
        String surname,
        @NotEmpty(message = "L'email è obbligatoria")
        @Email(message = "L'email inserita non è valida")
        String email,
//        @NotEmpty(message = "L'avatar è obbligatorio")
//        @URL(message="L'URL inserito non è valido")
        String propic,
        @NotEmpty(message = "La password è obbligatoria")
        String password,
        Ruolo ruolo
) {
}
