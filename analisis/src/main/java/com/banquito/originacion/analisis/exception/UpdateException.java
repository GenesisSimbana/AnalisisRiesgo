package com.banquito.originacion.analisis.exception;

public class UpdateException extends RuntimeException {

    private final String entity;
    private final String reason;

    public UpdateException(String entity, String reason) {
        super();
        this.entity = entity;
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return "Error al actualizar la entidad: " + this.entity + ", razón: " + this.reason;
    }
} 