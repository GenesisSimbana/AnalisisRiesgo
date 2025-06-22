package com.banquito.originacion.analisis.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.originacion.analisis.exception.ObservacionAnalistasNotFoundException;
import com.banquito.originacion.analisis.model.ObservacionAnalistas;
import com.banquito.originacion.analisis.repository.ObservacionAnalistasRepository;

@Service
@Transactional
public class ObservacionAnalistasService {
    
    private final ObservacionAnalistasRepository observacionAnalistasRepository;
    
    public ObservacionAnalistasService(ObservacionAnalistasRepository observacionAnalistasRepository) {
        this.observacionAnalistasRepository = observacionAnalistasRepository;
    }
    
    public List<ObservacionAnalistas> findAll() {
        return observacionAnalistasRepository.findAll();
    }
    
    public Page<ObservacionAnalistas> findAllPaginated(Pageable pageable) {
        return observacionAnalistasRepository.findAll(pageable);
    }
    
    public ObservacionAnalistas findById(Integer idObservacionAnalista) {
        return observacionAnalistasRepository.findById(idObservacionAnalista)
            .orElseThrow(() -> new ObservacionAnalistasNotFoundException(
                idObservacionAnalista.toString(), "ID de observación"));
    }
    
    public List<ObservacionAnalistas> findByIdSolicitud(Integer idSolicitud) {
        return observacionAnalistasRepository.findByIdSolicitud(idSolicitud);
    }
    
    public List<ObservacionAnalistas> findByUsuario(String usuario) {
        return observacionAnalistasRepository.findByUsuario(usuario);
    }
    
    public List<ObservacionAnalistas> findByIdSolicitudOrderByFechaHoraDesc(Integer idSolicitud) {
        return observacionAnalistasRepository.findByIdSolicitudOrderByFechaHoraDesc(idSolicitud);
    }
    
    public List<ObservacionAnalistas> findByUsuarioOrderByFechaHoraDesc(String usuario) {
        return observacionAnalistasRepository.findByUsuarioOrderByFechaHoraDesc(usuario);
    }
    
    public ObservacionAnalistas save(ObservacionAnalistas observacionAnalistas) {
        if (observacionAnalistas.getFechaHora() == null) {
            observacionAnalistas.setFechaHora(LocalDateTime.now());
        }
        if (observacionAnalistas.getVersion() == null) {
            observacionAnalistas.setVersion(BigDecimal.ONE);
        }
        return observacionAnalistasRepository.save(observacionAnalistas);
    }
    
    public ObservacionAnalistas update(Integer idObservacionAnalista, ObservacionAnalistas observacionAnalistas) {
        ObservacionAnalistas existingObservacion = findById(idObservacionAnalista);
        
        existingObservacion.setIdSolicitud(observacionAnalistas.getIdSolicitud());
        existingObservacion.setUsuario(observacionAnalistas.getUsuario());
        existingObservacion.setFechaHora(observacionAnalistas.getFechaHora());
        existingObservacion.setRazonIntervencion(observacionAnalistas.getRazonIntervencion());
        existingObservacion.setVersion(observacionAnalistas.getVersion().add(BigDecimal.ONE));
        
        return observacionAnalistasRepository.save(existingObservacion);
    }
    
    public ObservacionAnalistas partialUpdate(Integer idObservacionAnalista, ObservacionAnalistas observacionAnalistas) {
        ObservacionAnalistas existingObservacion = findById(idObservacionAnalista);
        
        if (observacionAnalistas.getIdSolicitud() != null) {
            existingObservacion.setIdSolicitud(observacionAnalistas.getIdSolicitud());
        }
        if (observacionAnalistas.getUsuario() != null) {
            existingObservacion.setUsuario(observacionAnalistas.getUsuario());
        }
        if (observacionAnalistas.getFechaHora() != null) {
            existingObservacion.setFechaHora(observacionAnalistas.getFechaHora());
        }
        if (observacionAnalistas.getRazonIntervencion() != null) {
            existingObservacion.setRazonIntervencion(observacionAnalistas.getRazonIntervencion());
        }
        
        existingObservacion.setVersion(existingObservacion.getVersion().add(BigDecimal.ONE));
        
        return observacionAnalistasRepository.save(existingObservacion);
    }
    
    public void deleteById(Integer idObservacionAnalista) {
        if (!observacionAnalistasRepository.existsById(idObservacionAnalista)) {
            throw new ObservacionAnalistasNotFoundException(
                idObservacionAnalista.toString(), "ID de observación");
        }
        observacionAnalistasRepository.deleteById(idObservacionAnalista);
    }
    
    public boolean existsById(Integer idObservacionAnalista) {
        return observacionAnalistasRepository.existsById(idObservacionAnalista);
    }
} 