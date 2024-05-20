package michelavivacqua.Mimi.sWardrobe.payloads;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RatingDTO(
        @Min(1)
        @Max(5)
       int valutazione
) {
}
