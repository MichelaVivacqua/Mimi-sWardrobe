package michelavivacqua.Mimi.sWardrobe.payloads;

import java.util.List;

public record NewAbbinamentoDTO(
         Long utenteId,
         List<Integer>indumentiId
) {
}
