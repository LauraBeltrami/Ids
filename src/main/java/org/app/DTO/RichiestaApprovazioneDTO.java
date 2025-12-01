package org.app.DTO;

public record RichiestaApprovazioneDTO(
        Long id,
        String nome,
        String ruolo // "VENDITORE", "ANIMATORE", "CURATORE"
) {}