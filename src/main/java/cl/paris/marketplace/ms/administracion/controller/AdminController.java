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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/acciones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminAccionResponse> registrarAccionManual(
            @Valid @RequestBody AdminAccionRequest request,
            Authentication authentication) {
        UUID adminId = UUID.fromString(authentication.getCredentials().toString());
        return ResponseEntity.ok(adminService.registrarAccionManual(request, adminId));
    }

    @GetMapping("/acciones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminAccionResponse>> listarHistorial() {
        return ResponseEntity.ok(adminService.listarHistorial());
    }

    @GetMapping("/acciones/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminAccionResponse>> listarPorUsuarioAdmin(
            @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(adminService.listarPorUsuarioAdmin(usuarioId));
    }

    @PutMapping("/productos/moderar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminAccionResponse> moderarProducto(
            @Valid @RequestBody ModerarProductoRequest request,
            Authentication authentication) {
        UUID adminId = UUID.fromString(authentication.getCredentials().toString());
        return ResponseEntity.ok(adminService.moderarProducto(request, adminId));
    }

    @PutMapping("/usuarios/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminAccionResponse> cambiarEstadoUsuario(
            @Valid @RequestBody EstadoUsuarioRequest request,
            Authentication authentication) {
        UUID adminId = UUID.fromString(authentication.getCredentials().toString());
        return ResponseEntity.ok(adminService.cambiarEstadoUsuario(request, adminId));
    }

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
