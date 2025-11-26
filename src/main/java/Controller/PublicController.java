package Controller;

import jakarta.validation.constraints.Positive;
import DTO.VenditoreIndirizzoDTO;
import Exceptions.NotFoundException;
import Model.Prodotto;
import Repository.ProdottoRepository;
import Service.MapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public") // Base path per tutte le chiamate pubbliche
public class PublicController {

    private final MapService venditoreService;
    private final ProdottoRepository prodottoRepo; // Iniettiamo il repo direttamente

    public PublicController(MapService venditoreService, ProdottoRepository prodottoRepo) {
        this.venditoreService = venditoreService;
        this.prodottoRepo = prodottoRepo;
    }

    // --- 1. INDIRIZZI VENDITORI (Tramite Service) ---

    @GetMapping("/venditori/indirizzi")
    public List<VenditoreIndirizzoDTO> getTuttiIndirizzi() {
        return venditoreService.getAllIndirizzi();
    }

    @GetMapping("/venditori/{venditoreId}/indirizzo")
    public VenditoreIndirizzoDTO getIndirizzoSingolo(@PathVariable @Positive Long venditoreId) {
        return venditoreService.getIndirizzoVenditore(venditoreId);
    }

    // --- 2. CONDIVISIONE SOCIAL (Logica nel Controller) ---

    /**
     * Simula la condivisione sui social.
     * Non usa un Service dedicato per la logica, fa tutto qui.
     */
    @PostMapping("/prodotti/{prodottoId}/condividi")
    public ResponseEntity<String> condividiProdotto(@PathVariable @Positive Long prodottoId) {

        // 1. Cerchiamo il prodotto nel DB solo per prendere il nome
        Prodotto prodotto = prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato con ID: " + prodottoId));

        // 2. Creiamo il messaggio (Logica hardcoded nel controller come richiesto)
        String messaggio = "Il prodotto \"" + prodotto.getNome() + "\" è stato condiviso sui social!";

        // (Opzionale) Qui potresti fare un System.out.println per vederlo in console
        System.out.println("📲 SOCIAL MOCK: " + messaggio);

        // 3. Ritorniamo la risposta
        return ResponseEntity.ok(messaggio);
    }
}