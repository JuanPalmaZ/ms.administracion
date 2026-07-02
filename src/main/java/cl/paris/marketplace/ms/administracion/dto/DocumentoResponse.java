package cl.paris.marketplace.ms.administracion.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentoResponse(

        UUID id,
        String tipoDocumento,
        LocalDateTime fechaSubida,
        String estado

) {}