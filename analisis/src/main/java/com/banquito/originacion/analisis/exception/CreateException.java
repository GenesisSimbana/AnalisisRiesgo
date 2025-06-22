package com.banquito.originacion.analisis.exception;

public class CreateException extends RuntimeException {

    private final String entity;
    private final String reason;

    public CreateException(String entity, String reason) {
        super();
        this.entity = entity;
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return "Error al crear la entidad: " + this.entity + ", razón: " + this.reason;
    }
} 