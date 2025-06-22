package com.banquito.originacion.analisis.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.originacion.analisis.exception.HistorialEstadosNotFoundException;
import com.banquito.originacion.analisis.enums.EstadoHistorialEnum;
import com.banquito.originacion.analisis.model.HistorialEstados;
import com.banquito.originacion.analisis.repository.HistorialEstadosRepository;

@Service
@Transactional
public class HistorialEstadosService {
    
    private final HistorialEstadosRepository historialEstadosRepository;
    
    public HistorialEstadosService(HistorialEstadosRepository historialEstadosRepository) {
        this.historialEstadosRepository = historialEstadosRepository;
    }
    
    public List<HistorialEstados> findAll() {
        return historialEstadosRepository.findAll();
    }
    
    public Page<HistorialEstados> findAllPaginated(Pageable pageable) {
        return historialEstadosRepository.findAll(pageable);
    }
    
    public HistorialEstados findById(Integer idHistorial) {
        Optional<HistorialEstados> historial = historialEstadosRepository.findById(idHistorial);
        if (historial.isEmpty()) {
            throw new HistorialEstadosNotFoundException(idHistorial.toString(), "ID de historial");
        }
        return historial.get();
    }
    
    public List<HistorialEstados> findByIdSolicitud(Integer idSolicitud) {
        return historialEstadosRepository.findByIdSolicitud(idSolicitud);
    }
    
    public List<HistorialEstados> findByEstado(EstadoHistorialEnum estado) {
        return historialEstadosRepository.findByEstado(estado);
    }
    
    public List<HistorialEstados> findByIdSolicitudAndEstado(Integer idSolicitud, EstadoHistorialEnum estado) {
        return historialEstadosRepository.findByIdSolicitudAndEstado(idSolicitud, estado);
    }
    
    public List<HistorialEstados> findByUsuario(String usuario) {
        return historialEstadosRepository.findByUsuario(usuario);
    }
    
    public Optional<HistorialEstados> findLatestByIdSolicitud(Integer idSolicitud) {
        return historialEstadosRepository.findFirstByIdSolicitudOrderByFechaHoraDesc(idSolicitud);
    }
    
    public List<HistorialEstados> findByIdSolicitudOrderByFechaHoraDesc(Integer idSolicitud) {
        return historialEstadosRepository.findByIdSolicitudOrderByFechaHoraDesc(idSolicitud);
    }
    
    /**
     * REGLA DE NEGOCIO: Crear nuevo historial de estado
     * - Si no se proporciona fecha, se asigna la fecha actual
     * - Si no se proporciona versión, se inicia en 1
     * - Se valida que el estado sea válido
     * - Se registra automáticamente la fecha de creación
     */
    public HistorialEstados save(HistorialEstados historialEstados) {
        // Regla de negocio: Fecha automática si no se proporciona
        if (historialEstados.getFechaHora() == null) {
            historialEstados.setFechaHora(LocalDateTime.now());
        }
        
        // Regla de negocio: Versión inicial si no se proporciona
        if (historialEstados.getVersion() == null) {
            historialEstados.setVersion(BigDecimal.ONE);
        }
        
        // Regla de negocio: Validar que el estado sea válido
        if (historialEstados.getEstado() == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        
        // Regla de negocio: Validar que el usuario no esté vacío
        if (historialEstados.getUsuario() == null || historialEstados.getUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario no puede estar vacío");
        }
        
        // Regla de negocio: Validar que el motivo no esté vacío
        if (historialEstados.getMotivo() == null || historialEstados.getMotivo().trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo no puede estar vacío");
        }
        
        return historialEstadosRepository.save(historialEstados);
    }
    
    /**
     * REGLA DE NEGOCIO: Actualizar historial completo
     * - Se valida que el historial exista
     * - Se incrementa automáticamente la versión
     * - Se mantiene la trazabilidad de cambios
     */
    public HistorialEstados update(Integer idHistorial, HistorialEstados historialEstados) {
        HistorialEstados existingHistorial = findById(idHistorial);
        
        // Regla de negocio: Validar que el estado sea válido
        if (historialEstados.getEstado() == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        
        // Regla de negocio: Validar que el usuario no esté vacío
        if (historialEstados.getUsuario() == null || historialEstados.getUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario no puede estar vacío");
        }
        
        // Regla de negocio: Validar que el motivo no esté vacío
        if (historialEstados.getMotivo() == null || historialEstados.getMotivo().trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo no puede estar vacío");
        }
        
        // Regla de negocio: Incrementar versión automáticamente
        BigDecimal nuevaVersion = existingHistorial.getVersion().add(BigDecimal.ONE);
        
        existingHistorial.setIdSolicitud(historialEstados.getIdSolicitud());
        existingHistorial.setEstado(historialEstados.getEstado());
        existingHistorial.setFechaHora(historialEstados.getFechaHora());
        existingHistorial.setUsuario(historialEstados.getUsuario());
        existingHistorial.setMotivo(historialEstados.getMotivo());
        existingHistorial.setVersion(nuevaVersion);
        
        return historialEstadosRepository.save(existingHistorial);
    }
    
    /**
     * REGLA DE NEGOCIO: Actualizar historial parcialmente
     * - Solo se actualizan los campos proporcionados
     * - Se incrementa automáticamente la versión
     * - Se mantienen los valores existentes para campos no proporcionados
     */
    public HistorialEstados partialUpdate(Integer idHistorial, HistorialEstados historialEstados) {
        HistorialEstados existingHistorial = findById(idHistorial);
        
        // Regla de negocio: Actualizar solo campos proporcionados
        if (historialEstados.getIdSolicitud() != null) {
            existingHistorial.setIdSolicitud(historialEstados.getIdSolicitud());
        }
        if (historialEstados.getEstado() != null) {
            existingHistorial.setEstado(historialEstados.getEstado());
        }
        if (historialEstados.getFechaHora() != null) {
            existingHistorial.setFechaHora(historialEstados.getFechaHora());
        }
        if (historialEstados.getUsuario() != null && !historialEstados.getUsuario().trim().isEmpty()) {
            existingHistorial.setUsuario(historialEstados.getUsuario());
        }
        if (historialEstados.getMotivo() != null && !historialEstados.getMotivo().trim().isEmpty()) {
            existingHistorial.setMotivo(historialEstados.getMotivo());
        }
        
        // Regla de negocio: Incrementar versión automáticamente
        existingHistorial.setVersion(existingHistorial.getVersion().add(BigDecimal.ONE));
        
        return historialEstadosRepository.save(existingHistorial);
    }
    
    /**
     * REGLA DE NEGOCIO: Eliminar historial
     * - Se valida que el historial exista antes de eliminar
     * - Se mantiene la integridad referencial
     */
    public void deleteById(Integer idHistorial) {
        if (!historialEstadosRepository.existsById(idHistorial)) {
            throw new HistorialEstadosNotFoundException(idHistorial.toString(), "ID de historial");
        }
        historialEstadosRepository.deleteById(idHistorial);
    }
    
    public boolean existsById(Integer idHistorial) {
        return historialEstadosRepository.existsById(idHistorial);
    }
    
    /**
     * REGLA DE NEGOCIO: Obtener el estado actual de una solicitud
     * - Retorna el último estado registrado para la solicitud
     * - Útil para conocer el estado actual sin revisar todo el historial
     */
    public EstadoHistorialEnum getEstadoActualSolicitud(Integer idSolicitud) {
        Optional<HistorialEstados> ultimoHistorial = findLatestByIdSolicitud(idSolicitud);
        if (ultimoHistorial.isPresent()) {
            return ultimoHistorial.get().getEstado();
        }
        return null; // No hay historial para esta solicitud
    }
    
    /**
     * REGLA DE NEGOCIO: Validar transición de estado
     * - Verifica si la transición de estado es válida según el flujo de negocio
     * - Previene transiciones no permitidas
     */
    public boolean validarTransicionEstado(EstadoHistorialEnum estadoActual, EstadoHistorialEnum nuevoEstado) {
        // Regla de negocio: Flujo de estados permitidos
        switch (estadoActual) {
            case Borrador:
                return nuevoEstado == EstadoHistorialEnum.EnRevision || 
                       nuevoEstado == EstadoHistorialEnum.Cancelada;
            case EnRevision:
                return nuevoEstado == EstadoHistorialEnum.Aprobada || 
                       nuevoEstado == EstadoHistorialEnum.Rechazada || 
                       nuevoEstado == EstadoHistorialEnum.Borrador;
            case Aprobada:
            case Rechazada:
            case Cancelada:
                return false; // Estados finales, no se pueden cambiar
            default:
                return false;
        }
    }
} 