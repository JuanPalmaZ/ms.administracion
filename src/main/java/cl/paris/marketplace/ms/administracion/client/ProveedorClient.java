package cl.paris.marketplace.ms.administracion.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cl.paris.marketplace.ms.administracion.dto.ActualizarEstadoDocumentoRequest;
import cl.paris.marketplace.ms.administracion.dto.DocumentoResponse;

@FeignClient(name = "ms-proveedores", configuration = FeignClientConfig.class)
public interface ProveedorClient {

    @PutMapping("/api/proveedores/documentos/{documentoId}/estado")
    DocumentoResponse actualizarEstadoDocumento(
            @PathVariable("documentoId") UUID documentoId,
            @RequestBody ActualizarEstadoDocumentoRequest request);

}