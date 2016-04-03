package com.github.kgrech.statcollectior.server.config;

import com.github.kgrech.statcollectior.server.exception.WrongFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Defines error code for custom exceptions
 * @author Konstantin G. (kgrech@mail.ru)
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    public static class  ErrorInfo {
        public final String url;
        public final String ex;

        ErrorInfo(String url, Exception ex) {
            this.url = url;
            this.ex = ex.getLocalizedMessage();
        }
    }

    /**
     * Return 400 for WrongFormatException
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongFormatException.class)
    @ResponseBody
    ErrorInfo handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }
}
