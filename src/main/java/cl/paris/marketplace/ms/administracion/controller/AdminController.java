package cl.paris.marketplace.ms.administracion.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.paris.marketplace.ms.administracion.dto.ActualizarEstadoDocumentoRequest;
import cl.paris.marketplace.ms.administracion.dto.AdminAccionRequest;
import cl.paris.marketplace.ms.administracion.dto.AdminAccionResponse;
import cl.paris.marketplace.ms.administracion.dto.EstadoUsuarioRequest;
import cl.paris.marketplace.ms.administracion.dto.ModerarProductoRequest;
import cl.paris.marketplace.ms.administracion.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Administración", description = "Endpoints para la gestión y administración del marketplace")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Registrar acción manual", description = "Registra una acción manual de auditoría o administración")
    @ApiResponse(responseCode = "200", description = "Acción manual registrada exitosamente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @io.swagger.v3.oas.annotations.media.Content(
            examples = @ExampleObject(value = "{\n  \"accion\": \"BLOQUEO_PREVENTIVO\",\n  \"detalle\": \"Se detectaron múltiples intentos de acceso fallidos\"\n}")
        )
    )
    @PostMapping("/acciones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminAccionResponse> registrarAccionManual(
            @Valid @RequestBody AdminAccionRequest request,
            Authentication authentication) {
        UUID adminId = UUID.fromString(authentication.getCredentials().toString());
        return ResponseEntity.ok(adminService.registrarAccionManual(request, adminId));
    }

    @Operation(summary = "Listar historial de acciones", description = "Obtiene todo el historial de acciones realizadas por administradores")
    @ApiResponse(responseCode = "200", description = "Historial de acciones obtenido correctamente")
    @GetMapping("/acciones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminAccionResponse>> listarHistorial() {
        return ResponseEntity.ok(adminService.listarHistorial());
    }

    @Operation(summary = "Listar acciones por usuario", description = "Obtiene el historial de acciones filtrado por un ID de usuario específico")
    @ApiResponse(responseCode = "200", description = "Historial del usuario obtenido correctamente")
    @GetMapping("/acciones/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminAccionResponse>> listarPorUsuarioAdmin(
            @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(adminService.listarPorUsuarioAdmin(usuarioId));
    }

    @Operation(summary = "Moderar un producto", description = "Permite a un administrador aprobar o rechazar un producto publicado")
    @ApiResponse(responseCode = "200", description = "Producto moderado exitosamente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @io.swagger.v3.oas.annotations.media.Content(
            examples = @ExampleObject(value = "{\n  \"productoId\": \"123e4567-e89b-12d3-a456-426614174000\",\n  \"estado\": \"APROBADO\",\n  \"motivo\": \"El producto cumple con todas las reglas del marketplace\"\n}")
        )
    )
    @PutMapping("/productos/moderar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminAccionResponse> moderarProducto(
            @Valid @RequestBody ModerarProductoRequest request,
            Authentication authentication) {
        UUID adminId = UUID.fromString(authentication.getCredentials().toString());
        return ResponseEntity.ok(adminService.moderarProducto(request, adminId));
    }

    @Operation(summary = "Cambiar estado de un usuario", description = "Permite banear o desbanear a un usuario especificando una razón")
    @ApiResponse(responseCode = "200", description = "Estado del usuario actualizado exitosamente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @io.swagger.v3.oas.annotations.media.Content(
            examples = @ExampleObject(value = "{\n  \"usuarioId\": \"987e6543-e21b-12d3-a456-426614174000\",\n  \"baneo\": true,\n  \"razon\": \"Infracción reiterada de términos y condiciones\"\n}")
        )
    )
    @PutMapping("/usuarios/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminAccionResponse> cambiarEstadoUsuario(
            @Valid @RequestBody EstadoUsuarioRequest request,
            Authentication authentication) {
        UUID adminId = UUID.fromString(authentication.getCredentials().toString());
        return ResponseEntity.ok(adminService.cambiarEstadoUsuario(request, adminId));
    }

    @Operation(summary = "Actualizar estado de un documento", description = "Modifica el estado de revisión de un documento asociado a un usuario/producto")
    @ApiResponse(responseCode = "200", description = "Estado del documento actualizado correctamente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @io.swagger.v3.oas.annotations.media.Content(
            examples = @ExampleObject(value = "{\n  \"estado\": \"RECHAZADO\"\n}")
        )
    )
    @PutMapping("/documentos/{documentoId}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminAccionResponse> actualizarEstadoDocumento(
            @PathVariable UUID documentoId,
            @Valid @RequestBody ActualizarEstadoDocumentoRequest request,
            Authentication authentication) {
        UUID adminId = UUID.fromString(authentication.getCredentials().toString());
        return ResponseEntity.ok(adminService.actualizarEstadoDocumento(documentoId, request, adminId));
    }
}