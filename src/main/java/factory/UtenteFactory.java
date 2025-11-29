package factory;

import DTO.RegistrationReq;
import Model.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UtenteFactory {

    private final PasswordEncoder passwordEncoder;

    public UtenteFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * FACTORY METHOD: Crea l'istanza corretta in base al Ruolo.
     */
    public AbstractUtente creaUtente(Ruolo ruolo, RegistrationReq req) {
        String encodedPwd = passwordEncoder.encode(req.password());

        switch (ruolo) {
            case ACQUIRENTE:
                if (req.cognome() == null)
                    throw new IllegalArgumentException("Il cognome è obbligatorio per gli acquirenti.");
                return new Acquirente(null, req.nome(), req.cognome(), req.email(), encodedPwd);

            case VENDITORE:
                // Controllo veloce (opzionale ma utile)
                if (req.indirizzo() == null) throw new IllegalArgumentException("Indirizzo obbligatorio per venditori");
                return new Venditore(null, req.nome(), req.indirizzo(), req.email(), encodedPwd);

            case DISTRIBUTORE:
                if (req.indirizzo() == null) throw new IllegalArgumentException("Indirizzo obbligatorio");
                return new Distributore(null, req.nome(), req.indirizzo(), req.email(), encodedPwd);

            case TRASFORMATORE:
                if (req.indirizzo() == null) throw new IllegalArgumentException("Indirizzo obbligatorio");
                Venditore t = new Venditore(null, req.nome(), req.indirizzo(), req.email(), encodedPwd);
                t.setRuolo("ROLE_TRASFORMATORE");
                return t;

            case ANIMATORE:
                return new Animatore(null, req.nome(), req.email(), encodedPwd);

            case CURATORE:
                return new Curatore(null, req.nome(), req.email(), encodedPwd);

            default:
                throw new IllegalArgumentException("Tipo utente non supportato: " + ruolo);
        }
    }
}
