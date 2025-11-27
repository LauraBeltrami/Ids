package Controller;


import DTO.RegistrationReq;
import Model.*;
import Repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import Service.RegistrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
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