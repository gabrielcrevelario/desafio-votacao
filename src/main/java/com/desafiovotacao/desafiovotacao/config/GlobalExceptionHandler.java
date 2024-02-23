package com.desafiovotacao.desafiovotacao.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        // Aqui você pode extrair a mensagem da exceção e retorná-la como parte da resposta da API
        String mensagemDeErro = ex.getMessage();

        // Crie um objeto para encapsular a mensagem de erro (opcional)
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("mensagem", mensagemDeErro);

        // Retorna uma resposta com o status HTTP apropriado e a mensagem de erro
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
