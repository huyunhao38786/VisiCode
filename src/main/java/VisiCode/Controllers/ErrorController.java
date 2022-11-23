package VisiCode.Controllers;

import VisiCode.Domain.Exceptions.VisiCodeException;
import VisiCode.Domain.Note;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorController {
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(VisiCodeException.class)
    public VisiCodeException.VisiCodeExceptionDto handleEntityException(VisiCodeException e) {
        return e.toDto(HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SizeLimitExceededException.class)
    public VisiCodeException.VisiCodeExceptionDto handleEntityException(SizeLimitExceededException e) {
        throw new Note.BlobSizeException(e.getActualSize());
    }
}
