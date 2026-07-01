package cl.paris.marketplace.ms.administracion.controller;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; 
import org.springframework.web.bind.annotation.*;
 
import cl.paris.marketplace.ms.administracion.dto.AdminAccionRequest;
import cl.paris.marketplace.ms.administracion.dto.AdminAccionResponse;
import cl.paris.marketplace.ms.administracion.dto.ModerarProductoRequest;
import cl.paris.marketplace.ms.administracion.dto.EstadoUsuarioRequest;
import cl.paris.marketplace.ms.administracion.service.AdminService;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
 
import java.util.List;
import java.util.UUID;
 
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Administración", description = "Operaciones de auditoría, moderación de productos y gestión de estado de usuarios")
public class AdminController {
 
    private final AdminService adminService;
 
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
 
    @Operation(summary = "Registrar acción manual", description = "Registra una acción de auditoría ejecutada por un administrador")
    @ApiResponse(responseCode = "201", description = "Acción registrada exitosamente")
    @RequestBody(
        content = @Content(
            examples = @ExampleObject(
                name = "EjemploAdminAccion",
                value = "{\n  \"accion\": \"Bloqueo de categoría\",\n  \"detalle\": \"Se bloqueó la categoría de electrónica temporalmente por revisión\"\n}"
            )
        )
    )
    @PostMapping("/auditoria")
    public ResponseEntity<AdminAccionResponse> registrarAccionManual(
            @Valid @org.springframework.web.bind.annotation.RequestBody AdminAccionRequest request,
            Authentication authentication) {
        
        UUID adminId = UUID.fromString(authentication.getCredentials().toString());
        AdminAccionResponse response = adminService.registrarAccionManual(request, adminId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
 
    @Operation(summary = "Listar historial completo", description = "Obtiene todo el historial de auditoría de los administradores")
    @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente")
    @GetMapping("/auditoria")
    public ResponseEntity<List<AdminAccionResponse>> listarHistorial() {
        return ResponseEntity.ok(adminService.listarHistorial());
    }
 
    @Operation(summary = "Listar acciones por administrador", description = "Obtiene el historial de auditoría filtrado por un ID de administrador específico")
    @ApiResponse(responseCode = "200", description = "Historial del administrador obtenido correctamente")
    @GetMapping("/auditoria/usuario/{usuarioId}")
    public ResponseEntity<List<AdminAccionResponse>> listarPorUsuarioAdmin(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(adminService.listarPorUsuarioAdmin(usuarioId));
    }
 
    @Operation(summary = "Moderar estado de producto", description = "Aprueba o rechaza un producto en el marketplace")
    @ApiResponse(responseCode = "201", description = "Producto moderado correctamente")
    @RequestBody(
        content = @Content(
            examples = @ExampleObject(
                name = "EjemploModerarProducto",
                value = "{\n  \"productoId\": \"123e4567-e89b-12d3-a456-426614174000\",\n  \"estado\": \"APROBADO\",\n  \"motivo\": \"El producto cumple con todas las políticas fotográficas y de descripción\"\n}"
            )
        )
    )
    @PostMapping("/productos/moderar")
    public ResponseEntity<AdminAccionResponse> moderarProducto(
            @Valid @org.springframework.web.bind.annotation.RequestBody ModerarProductoRequest request,
            Authentication authentication) {
        
        UUID adminId = UUID.fromString(authentication.getCredentials().toString());
        AdminAccionResponse response = adminService.moderarProducto(request, adminId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
 
    @Operation(summary = "Cambiar estado de baneo de usuario", description = "Permite banear o desbanear a un usuario del marketplace indicando una razón")
    @ApiResponse(responseCode = "201", description = "Estado del usuario cambiado correctamente")
    @RequestBody(
        content = @Content(
            examples = @ExampleObject(
                name = "EjemploEstadoUsuario",
                value = "{\n  \"usuarioId\": \"123e4567-e89b-12d3-a456-426614174000\",\n  \"baneo\": true,\n  \"razon\": \"Venta de artículos falsificados, reincidente\"\n}"
            )
        )
    )
    @PostMapping("/usuarios/estado")
    public ResponseEntity<AdminAccionResponse> cambiarEstadoUsuario(
            @Valid @org.springframework.web.bind.annotation.RequestBody EstadoUsuarioRequest request,
            Authentication authentication) {
        
        UUID adminId = UUID.fromString(authentication.getCredentials().toString());
        AdminAccionResponse response = adminService.cambiarEstadoUsuario(request, adminId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}