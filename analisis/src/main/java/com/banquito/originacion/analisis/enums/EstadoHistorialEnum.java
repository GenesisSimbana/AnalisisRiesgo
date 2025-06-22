package com.banquito.originacion.analisis.enums;

public enum EstadoHistorialEnum {
    PENDIENTE("PENDIENTE"),
    EN_ANALISIS("EN_ANALISIS"),
    APROBADO("APROBADO"),
    RECHAZADO("RECHAZADO"),
    CANCELADO("CANCELADO"),
    SUSPENDIDO("SUSPENDIDO");
    
    private final String valor;
    
    EstadoHistorialEnum(String valor) {
        this.valor = valor;
    }
    
    public String getValor() {
        return valor;
    }
    
    public static EstadoHistorialEnum fromString(String text) {
        for (EstadoHistorialEnum estado : EstadoHistorialEnum.values()) {
            if (estado.valor.equalsIgnoreCase(text)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + text);
    }
} 