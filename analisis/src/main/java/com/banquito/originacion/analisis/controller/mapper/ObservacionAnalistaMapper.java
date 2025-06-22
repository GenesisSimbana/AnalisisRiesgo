package com.banquito.originacion.analisis.controller.mapper;

import org.springframework.stereotype.Component;

import com.banquito.originacion.analisis.controller.dto.ObservacionAnalistaDTO;
import com.banquito.originacion.analisis.model.ObservacionAnalista;

@Component
public class ObservacionAnalistaMapper {

    public ObservacionAnalistaDTO toDTO(ObservacionAnalista entity) {
        if (entity == null) {
            return null;
        }

        ObservacionAnalistaDTO dto = new ObservacionAnalistaDTO();
        dto.setIdObservacionAnalista(entity.getIdObservacionAnalista());
        dto.setIdSolicitud(entity.getIdSolicitud());
        dto.setUsuario(entity.getUsuario());
        dto.setFechaHora(entity.getFechaHora());
        dto.setRazonIntervencion(entity.getRazonIntervencion());
        dto.setVersion(entity.getVersion());
        
        return dto;
    }

    public ObservacionAnalista toEntity(ObservacionAnalistaDTO dto) {
        if (dto == null) {
            return null;
        }

        ObservacionAnalista entity = new ObservacionAnalista();
        entity.setIdObservacionAnalista(dto.getIdObservacionAnalista());
        entity.setIdSolicitud(dto.getIdSolicitud());
        entity.setUsuario(dto.getUsuario());
        entity.setFechaHora(dto.getFechaHora());
        entity.setRazonIntervencion(dto.getRazonIntervencion());
        entity.setVersion(dto.getVersion());
        
        return entity;
    }
} 