package com.banquito.originacion.analisis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.ObservacionAnalista;

@Repository
public interface ObservacionAnalistaRepository extends JpaRepository<ObservacionAnalista, Long> {

    List<ObservacionAnalista> findByIdSolicitud(Integer idSolicitud);
    
    List<ObservacionAnalista> findByUsuario(String usuario);
    
    List<ObservacionAnalista> findByIdSolicitudAndUsuario(Integer idSolicitud, String usuario);
    
    List<ObservacionAnalista> findByIdSolicitudOrderByFechaHoraDesc(Integer idSolicitud);
    
    List<ObservacionAnalista> findByUsuarioOrderByFechaHoraDesc(String usuario);
} 