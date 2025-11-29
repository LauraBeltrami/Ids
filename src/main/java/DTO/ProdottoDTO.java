package DTO;

import java.math.BigDecimal;
import java.util.List;

public record ProdottoDTO(
        Long id,
        String nome,
        BigDecimal prezzo,
        String stato,          // "IN_VALIDAZIONE" | "APPROVATO" | "RIFIUTATO"
        Long venditoreId,
        String venditoreNome,
        int quantitaDisponibile,
        List<CertificazioneDTO> certificazioni
) {}


