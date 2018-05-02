package org.cybersapien.watercollection.controller;

import lombok.extern.slf4j.Slf4j;
import org.cybersapien.watercollection.service.datatypes.error.Error;
import org.cybersapien.watercollection.service.datatypes.error.Errors;
import org.cybersapien.watercollection.service.datatypes.error.Source;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle the exceptions for a REST service
 */
@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    /**
     * Handle a WebApplicationException thrown the service
     * @param webApplicationException the exception thrown from the service
     * @param request the web service request
     * @return a ResponseEntity containing the Error response(s)
     */
    @ExceptionHandler(WebApplicationException.class)
    public ResponseEntity<Errors> handleWebApplicationException(WebApplicationException webApplicationException, WebRequest request) {
        log.error("", webApplicationException);

        Response response = webApplicationException.getResponse();

        Source source = new Source();
        source.setPointer(request.getDescription(false));

        Error error = new Error();
        error.setStatus(response.getStatus());
        error.setSource(source);
        error.setTitle(response.getStatusInfo().getReasonPhrase());
        error.setDetail(webApplicationException.getMessage());

        List<Error> listOfError = new ArrayList<>();
        listOfError.add(error);

        Errors errors = new Errors();
        errors.setErrors(listOfError);

        return new ResponseEntity<>(errors, HttpStatus.valueOf(error.getStatus()));
    }

    /**
     * Handle a BindingException thrown from th service
     * @param bindException the exception thrown from the service
     * @param request the web service request
     * @return a ResponseEntity containing the Error response(s)
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Errors> handleBindException(BindException bindException, WebRequest request) {
        log.error("", bindException);

        String pointer = request.getDescription(false);
        String errorTitle = bindException.getClass().getSimpleName();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        List<Error> listOfError = new ArrayList<>();
        for (ObjectError objectError : bindException.getAllErrors()) {
            // NOTE - Parsing the message from a third party is inherently dangerous. The format and content can change at any time.
            String objectErrorMessage = objectError.toString();
            String detailMessage = objectErrorMessage.substring(0, objectErrorMessage.indexOf(";")) + ": " + objectError.getDefaultMessage();

            Source source = new Source();
            source.setPointer(pointer);

            Error error = new Error();
            error.setStatus(httpStatus.value());
            error.setSource(source);
            error.setTitle(errorTitle);
            error.setDetail(detailMessage);

            listOfError.add(error);
        }

        Errors errors = new Errors();
        errors.setErrors(listOfError);

        return new ResponseEntity<>(errors, httpStatus);
    }

    /**
     * Handle a general exception thrown from service
     * @param exception the exception thrown from the service
     * @param request the web service request
     * @return a ResponseEntity containing the Error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Errors> handleGeneralException(Exception exception, WebRequest request) {
        log.error("", exception);

        ResponseEntity<Object> responseEntity = new ResponseEntityExceptionHandler() {}.handleException(exception, request);

        Source source = new Source();
        source.setPointer(request.getDescription(false));

        Error error = new Error();
        error.setStatus(responseEntity.getStatusCodeValue());
        error.setSource(source);
        error.setTitle(exception.getClass().getSimpleName());
        error.setDetail(exception.getMessage());

        List<Error> listOfError = new ArrayList<>();

        listOfError.add(error);

        Errors errors = new Errors();
        errors.setErrors(listOfError);

        return new ResponseEntity<>(errors, HttpStatus.valueOf(error.getStatus()));
    }
}
