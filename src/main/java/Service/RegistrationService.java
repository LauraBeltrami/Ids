package Service;

import DTO.RegistrationReq;
import Model.*;
import Repository.*;
import factory.UtenteFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegistrationService {

    private final UtenteFactory utenteFactory;
    private final AnimatoreRepository animatoreRepo;
    private final VenditoreRepository venditoreRepo;
    private final AcquirenteRepository acquirenteRepo;
    private final CuratoreRepository curatoreRepo;

    public RegistrationService(UtenteFactory utenteFactory, AnimatoreRepository animatoreRepo,
                               VenditoreRepository venditoreRepo, AcquirenteRepository acquirenteRepo,
                               CuratoreRepository curatoreRepo) {
        this.utenteFactory = utenteFactory;
        this.animatoreRepo = animatoreRepo;
        this.venditoreRepo = venditoreRepo;
        this.acquirenteRepo = acquirenteRepo;
        this.curatoreRepo = curatoreRepo;
    }

    public String registraUtente(Ruolo ruolo, RegistrationReq req) {
        // 1. USA LA FACTORY per creare l'oggetto
        AbstractUtente utente = utenteFactory.creaUtente(ruolo, req);

        // 2. Salva nel repository corretto
        // (Dato che usiamo MappedSuperclass e non SingleTable su AbstractUtente,
        // dobbiamo smistare manualmente il salvataggio)
        if (utente instanceof Acquirente a) {
            acquirenteRepo.save(a);
            return "Acquirente registrato! Benvenuto.";
        }
        else if (utente instanceof Animatore a) {
            animatoreRepo.save(a);
            return "Animatore registrato! Attendi approvazione.";
        }
        else if (utente instanceof Curatore c) {
            curatoreRepo.save(c);
            return "Curatore registrato! Attendi approvazione.";
        }
        else if (utente instanceof Venditore v) {
            // Questo copre VENDITORE, DISTRIBUTORE e TRASFORMATORE (che sono tutti Venditori)
            venditoreRepo.save(v);
            return "Partner commerciale (" + ruolo + ") registrato! Attendi approvazione.";
        }

        throw new IllegalStateException("Repository non trovato per il tipo: " + utente.getClass().getName());
    }
}
