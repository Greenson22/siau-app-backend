package com.sttis.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handler untuk menampilkan halaman error custom (misal, untuk web non-API)
    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception ex, WebRequest request) {
        
        // Cek jika request untuk API (URL mengandung /api/)
        if (request.getDescription(false).contains("/api/")) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            body.put("error", "Internal Server Error");
            body.put("message", ex.getMessage());
            body.put("path", request.getDescription(false).replace("uri=", ""));

            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Jika bukan request API, tampilkan halaman HTML
        ModelAndView modelAndView = new ModelAndView("error-page"); // Nama file HTML template
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        modelAndView.addObject("error", "Terjadi Kesalahan Internal");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    // Anda bisa menambahkan handler untuk exception spesifik lainnya
    // contoh: @ExceptionHandler(ResourceNotFoundException.class)
}