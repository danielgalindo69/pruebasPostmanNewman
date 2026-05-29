package com.ejemploPostman.demo.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException exception) {
		Map<String, String> errores = new HashMap<>();
		exception.getBindingResult().getFieldErrors()
				.forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errores);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> manejarNoEncontrado(IllegalArgumentException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("mensaje", exception.getMessage()));
	}
}
