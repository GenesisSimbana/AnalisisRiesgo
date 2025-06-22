package com.banquito.originacion.analisis.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.originacion.analisis.controller.dto.HistorialEstadoDTO;
import com.banquito.originacion.analisis.controller.mapper.HistorialEstadoMapper;
import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.HistorialEstado;
import com.banquito.originacion.analisis.service.HistorialEstadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/historiales-estados")
@Tag(name = "Historial Estados", description = "API para gestionar el historial de estados de las solicitudes")
public class HistorialEstadoController {

    private final HistorialEstadoService service;
    private final HistorialEstadoMapper mapper;

    public HistorialEstadoController(HistorialEstadoService service, HistorialEstadoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los historiales de estados", 
               description = "Retorna una lista de todos los historiales de estados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de historiales obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron historiales")
    })
    public ResponseEntity<List<HistorialEstadoDTO>> getAllHistorialEstados(
            @Parameter(description = "Filtrar por estado") @RequestParam(required = false) String estado,
            @Parameter(description = "Filtrar por usuario") @RequestParam(required = false) String usuario) {
        
        List<HistorialEstado> historiales;
        
        if (estado != null) {
            historiales = this.service.findByEstado(estado);
        } else if (usuario != null) {
            historiales = this.service.findByUsuario(usuario);
        } else {
            historiales = this.service.findAll();
        }
        
        List<HistorialEstadoDTO> dtos = new ArrayList<>(historiales.size());
        for (HistorialEstado historial : historiales) {
            dtos.add(mapper.toDTO(historial));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener historial de estado por ID", 
               description = "Retorna un historial de estado específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Historial no encontrado")
    })
    public ResponseEntity<HistorialEstadoDTO> getHistorialEstadoById(
            @Parameter(description = "ID del historial de estado") @PathVariable("id") Long id) {
        HistorialEstado historial = this.service.findById(id);
        return ResponseEntity.ok(this.mapper.toDTO(historial));
    }

    @GetMapping("/solicitudes/{idSolicitud}")
    @Operation(summary = "Obtener historial de estados por solicitud", 
               description = "Retorna todos los historiales de estados de una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historiales encontrados exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron historiales para la solicitud")
    })
    public ResponseEntity<List<HistorialEstadoDTO>> getHistorialEstadosBySolicitud(
            @Parameter(description = "ID de la solicitud") @PathVariable("idSolicitud") Integer idSolicitud) {
        List<HistorialEstado> historiales = this.service.findByIdSolicitud(idSolicitud);
        List<HistorialEstadoDTO> dtos = new ArrayList<>(historiales.size());
        
        for (HistorialEstado historial : historiales) {
            dtos.add(mapper.toDTO(historial));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/solicitudes/{idSolicitud}/ultimo")
    @Operation(summary = "Obtener último estado de una solicitud", 
               description = "Retorna el último estado registrado de una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Último estado encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró historial para la solicitud")
    })
    public ResponseEntity<HistorialEstadoDTO> getLastHistorialEstadoBySolicitud(
            @Parameter(description = "ID de la solicitud") @PathVariable("idSolicitud") Integer idSolicitud) {
        HistorialEstado historial = this.service.findLastByIdSolicitud(idSolicitud);
        return ResponseEntity.ok(this.mapper.toDTO(historial));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo historial de estado", 
               description = "Crea un nuevo registro de historial de estado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Historial creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<HistorialEstadoDTO> createHistorialEstado(
            @Valid @RequestBody HistorialEstadoDTO historialEstadoDTO) {
        HistorialEstado historial = this.mapper.toEntity(historialEstadoDTO);
        HistorialEstado createdHistorial = this.service.create(historial);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.toDTO(createdHistorial));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar historial de estado", 
               description = "Actualiza parcialmente un historial de estado existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Historial no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<HistorialEstadoDTO> updateHistorialEstado(
            @Parameter(description = "ID del historial de estado") @PathVariable("id") Long id,
            @Valid @RequestBody HistorialEstadoDTO historialEstadoDTO) {
        historialEstadoDTO.setIdHistorial(id);
        HistorialEstado historial = this.mapper.toEntity(historialEstadoDTO);
        HistorialEstado updatedHistorial = this.service.update(historial);
        return ResponseEntity.ok(this.mapper.toDTO(updatedHistorial));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({CreateException.class, UpdateException.class})
    public ResponseEntity<String> handleCreateUpdateException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
} 