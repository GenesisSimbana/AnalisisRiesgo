package com.banquito.originacion.analisis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.HistorialEstado;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {

    List<HistorialEstado> findByIdSolicitud(Integer idSolicitud);
    
    List<HistorialEstado> findByEstado(String estado);
    
    List<HistorialEstado> findByUsuario(String usuario);
    
    List<HistorialEstado> findByIdSolicitudAndEstado(Integer idSolicitud, String estado);
    
    Optional<HistorialEstado> findTopByIdSolicitudOrderByFechaHoraDesc(Integer idSolicitud);
    
    List<HistorialEstado> findByIdSolicitudOrderByFechaHoraDesc(Integer idSolicitud);
} 