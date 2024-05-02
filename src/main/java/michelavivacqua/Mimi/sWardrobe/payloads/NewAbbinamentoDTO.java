package michelavivacqua.Mimi.sWardrobe.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;


public record NewAbbinamentoDTO(
        @NotNull(message = "La lista degli indumenti è obbligatoria")
        Set<Integer> indumenti
) {}