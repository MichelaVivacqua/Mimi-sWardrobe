package michelavivacqua.Mimi.sWardrobe.exceptions;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException{
    private List<ObjectError> errorsList;
    public BadRequestException(String message){
        super(message);
    }

//    public BadRequestException(List<ObjectError> errorsList){
//        super("C'è un errore di compilazione" + errorsList);
//        this.errorsList = errorsList;
//    }

    public BadRequestException(List<ObjectError> errorsList) {
        super(buildErrorMessage(errorsList));
        this.errorsList = errorsList;
    }

    private static String buildErrorMessage(List<ObjectError> errorsList) {
        StringBuilder errorMessage = new StringBuilder("C'è un errore di compilazione: ");
        for (ObjectError error : errorsList) {
            errorMessage.append(error.getDefaultMessage()).append("; ");
        }
        return errorMessage.toString();
    }
}
