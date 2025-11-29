package DTO;


import Model.Certificazione;
import Model.Prodotto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class ProdottoMapper {
    private ProdottoMapper() {}

    public static ProdottoDTO toDTO(Prodotto p) {
        if (p == null) return null;

        // Gestione della lista: se è null, restituisci lista vuota per evitare errori
        List<CertificazioneDTO> certificazioniDTO =
                (p.getCertificazioni() == null) ? Collections.emptyList() :
                        p.getCertificazioni().stream()
                                .map(c -> new CertificazioneDTO(
                                        c.getId(),
                                        c.getDescrizione()
                                ))
                                .collect(Collectors.toList());

        return new ProdottoDTO(
                p.getId(),
                p.getNome(),
                p.getPrezzo(),
                p.getStato().name(),
                p.getVenditore().getId(),
                p.getVenditore().getNome(),
                p.getQuantitaDisponibile(), // Ho aggiunto anche la quantità
                certificazioniDTO // <-- Passi la lista convertita
        );
    }

    // Helper per liste di prodotti
    public static List<ProdottoDTO> toDTO(List<Prodotto> prodotti) {
        if (prodotti == null) return Collections.emptyList();
        return prodotti.stream().map(ProdottoMapper::toDTO).collect(Collectors.toList());
    }
}
