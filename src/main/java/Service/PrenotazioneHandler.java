package Service;

import DTO.EventoDTO;

public interface PrenotazioneHandler {
    public EventoDTO prenotaEvento(Long eventoId, Long acquirenteId);
    public EventoDTO annullaPrenotazione(Long eventoId, Long acquirenteId);
}