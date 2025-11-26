package Controller;

import jakarta.validation.constraints.*;
import DTO.InvitoDTO;
import DTO.ProdottoDTO;
import Model.StatoInvito;
import Service.InvitoHandler;
import Service.ProdottoService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/venditori/{venditoreId}/prodotti")
public class VenditoreController {

    private final ProdottoService prodottoService;

    private final InvitoHandler eventoService;

    public VenditoreController(ProdottoService prodottoService,
                               InvitoHandler eventoService) {
        this.prodottoService = prodottoService;
        this.eventoService = eventoService;
    }

    public static record CreaProdottoReq(@NotBlank String nome, @NotNull @DecimalMin("0.00") BigDecimal prezzo, @Min(0) int quantita) {}

    @PostMapping
    public ProdottoDTO crea(@PathVariable Long venditoreId, @Valid @RequestBody CreaProdottoReq req) {
        return prodottoService.creaProdotto(venditoreId, req.nome(), req.prezzo(), req.quantita());
    }

    public static record AggiornaPrezzoReq(@NotNull @DecimalMin("0.00") BigDecimal prezzo) {}

    @PatchMapping("/{prodottoId}/prezzo")
    public ProdottoDTO aggiornaPrezzo(@PathVariable Long venditoreId,
                                      @PathVariable Long prodottoId,
                                      @Valid @RequestBody AggiornaPrezzoReq req) {
        return prodottoService.aggiornaPrezzo(prodottoId, req.prezzo());
    }

    @DeleteMapping("/{prodottoId}")
    public void elimina(@PathVariable Long venditoreId, @PathVariable Long prodottoId) {
        prodottoService.elimina(prodottoId);
    }
    public record AggiornaDescrizioneReq(@NotBlank String descrizione) {}

    /**
     * Modifica la descrizione di un prodotto.
     * URL: PATCH /api/venditori/{venditoreId}/prodotti/{prodottoId}/descrizione
     */
    @PatchMapping("/{prodottoId}/descrizione")
    public ProdottoDTO aggiornaDescrizione(
            @PathVariable Long venditoreId,
            @PathVariable Long prodottoId,
            @Valid @RequestBody AggiornaDescrizioneReq req) {

        return prodottoService.aggiornaDescrizione(prodottoId, venditoreId, req.descrizione());
    }

    @PostMapping("/{eventoId}/accetta")
    public InvitoDTO accettaInvito(
            @PathVariable @Positive Long venditoreId,
            @PathVariable @Positive Long eventoId,
            @PathVariable @NotNull StatoInvito statoInvito) {

        return eventoService.rispondiInvito(eventoId, venditoreId, statoInvito);
    }

    public record CertificaReq(@NotBlank String descrizione) {}


    @PostMapping("/{prodottoId}/certificazioni")
    @PreAuthorize("hasAuthority('TRASFORMATORE')") // <--- SOLO TRASFORMATORI
    public ProdottoDTO aggiungiCertificazione(
            @PathVariable Long prodottoId,
            @Valid @RequestBody CertificaReq req) {

        return prodottoService.aggiungiCertificazione(prodottoId, req.descrizione());
    }

    @DeleteMapping("/{prodottoId}/certificazioni/{certificazioneId}")
    @PreAuthorize("hasAuthority('TRASFORMATORE')") // <--- SOLO TRASFORMATORI
    public ProdottoDTO rimuoviCertificazione(
            @PathVariable Long prodottoId,
            @PathVariable Long certificazioneId) {

        return prodottoService.rimuoviCertificazione(prodottoId, certificazioneId);
    }
}