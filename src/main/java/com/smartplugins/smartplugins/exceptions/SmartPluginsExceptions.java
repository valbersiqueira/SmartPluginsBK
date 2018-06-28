package com.smartplugins.smartplugins.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class SmartPluginsExceptions extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {


        String messageUser = messageSource.getMessage("message.invalida", null, LocaleContextHolder.getLocale());
        String messageDes = ex.getCause().toString();
        List<Erro> erros = Arrays.asList(new Erro(messageUser, messageDes));

        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, createListErros(ex.getBindingResult()), headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {

        String messageUser = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
        String messageDes = ex.toString();
        List<Erro> erros = Arrays.asList(new Erro(messageUser, messageDes));

        return  handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private List<Erro> createListErros(BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(er -> {
            String messageUser = messageSource.getMessage(er, LocaleContextHolder.getLocale());
            String messageDes = er.toString();
            erros.add(new Erro(messageUser, messageDes));
        });
        return erros;
    }

    public class Erro {
        private String messageUser;
        private String messageDes;

        public Erro(String messageUser, String messageDes) {
            this.messageUser = messageUser;
            this.messageDes = messageDes;
        }

        public String getMessageUser() {
            return messageUser;
        }

        public String getMessageDes() {
            return messageDes;
        }
    }
}
