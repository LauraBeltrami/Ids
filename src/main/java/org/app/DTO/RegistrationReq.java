package org.app.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record
RegistrationReq(
        @NotBlank String nome,
        String cognome, // Opzionale (usato solo da Acquirente)
        String indirizzo,
        @NotBlank String email,
        @NotBlank @Size(min = 4) String password
) {}
