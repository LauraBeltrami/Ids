package org.app.Controller;


import org.app.DTO.RegistrationReq;
import jakarta.validation.Valid;
import org.app.Model.Ruolo;
import org.app.Service.RegistrationService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth/register")
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Endpoint unico per tutte le registrazioni.
     * Esempio URL: POST /api/auth/register/VENDITORE
     * Esempio URL: POST /api/auth/register/TRASFORMATORE
     */
    @PostMapping("/{tipoUtente}")
    public String register(@PathVariable String tipoUtente,
                           @Valid @RequestBody RegistrationReq req) {

        // Converte la stringa (es. "venditore") nell'Enum (VENDITORE)
        Ruolo ruolo = Ruolo.parse(tipoUtente);

        if (ruolo == null) {
            return "Errore: Tipo utente non valido. Tipi ammessi: VENDITORE, ACQUIRENTE, ANIMATORE, CURATORE, DISTRIBUTORE, TRASFORMATORE";
        }

        return registrationService.registraUtente(ruolo, req);
    }
}