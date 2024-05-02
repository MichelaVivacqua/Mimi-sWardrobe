package michelavivacqua.Mimi.sWardrobe.exceptions;

import michelavivacqua.Mimi.sWardrobe.payloads.ErrorsPayload;
import michelavivacqua.Mimi.sWardrobe.payloads.ErrorsResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsPayload handleBadRequest(BadRequestException ex){
        return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorsPayload handleNotFound(NotFoundException ex){
        return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public ErrorsPayload handleGenericErrors(Exception ex){
        ex.printStackTrace(); // per tenere traccia di cosa ha causato l'errore
        return new ErrorsPayload("Problema lato server! Giuro che lo risolveremo presto! (Scherzo rimarrà così)", LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    // Con questa annotazione indico che questo metodo dovrà gestire le eccezioni di tipo NotFoundException
    public ErrorsResponseDTO handleUnauthorized(UnauthorizedException ex){
        return new ErrorsResponseDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorsResponseDTO handleForbidden(AccessDeniedException ex){
        return new ErrorsResponseDTO("Non hai accesso a questa funzionalità!", LocalDateTime.now());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsPayload handleIllegalArgumentException(IllegalArgumentException ex){
        return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
    }

}
