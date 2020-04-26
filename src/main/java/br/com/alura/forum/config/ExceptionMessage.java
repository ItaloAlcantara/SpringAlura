package br.com.alura.forum.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExceptionMessage extends Exception {

    public ExceptionMessage(){}

    public ExceptionMessage(String mensagem){ super(mensagem); }
}
