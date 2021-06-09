package info.touret.spring.maintenancemode;

import info.touret.spring.maintenancemode.exception.MaintenanceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles all the exceptions thrown by the application
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Indicates that the application is on maintenance
     */
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @ExceptionHandler(MaintenanceException.class)
    public APIError maintenance() {
        return new APIError(HttpStatus.I_AM_A_TEAPOT.value(),"Service currently in maintenance");
    }

    /**
     * Any other exception
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public APIError anyException() {
        return new APIError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"An unexpected server error occured");
    }
}
