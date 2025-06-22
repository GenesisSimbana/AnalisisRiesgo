package com.banquito.originacion.analisis.controller.mapper;

import org.springframework.stereotype.Component;

import com.banquito.originacion.analisis.controller.dto.HistorialEstadoDTO;
import com.banquito.originacion.analisis.model.HistorialEstado;

@Component
public class HistorialEstadoMapper {

    public HistorialEstadoDTO toDTO(HistorialEstado entity) {
        if (entity == null) {
            return null;
        }

        HistorialEstadoDTO dto = new HistorialEstadoDTO();
        dto.setIdHistorial(entity.getIdHistorial());
        dto.setIdSolicitud(entity.getIdSolicitud());
        dto.setEstado(entity.getEstado());
        dto.setFechaHora(entity.getFechaHora());
        dto.setUsuario(entity.getUsuario());
        dto.setMotivo(entity.getMotivo());
        dto.setVersion(entity.getVersion());
        
        return dto;
    }

    public HistorialEstado toEntity(HistorialEstadoDTO dto) {
        if (dto == null) {
            return null;
        }

        HistorialEstado entity = new HistorialEstado();
        entity.setIdHistorial(dto.getIdHistorial());
        entity.setIdSolicitud(dto.getIdSolicitud());
        entity.setEstado(dto.getEstado());
        entity.setFechaHora(dto.getFechaHora());
        entity.setUsuario(dto.getUsuario());
        entity.setMotivo(dto.getMotivo());
        entity.setVersion(dto.getVersion());
        
        return entity;
    }
} 