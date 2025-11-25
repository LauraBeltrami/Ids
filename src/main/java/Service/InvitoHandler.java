package Service;

import DTO.InvitoDTO;
import Model.StatoInvito;

public interface InvitoHandler {
    public InvitoDTO rispondiInvito(Long eventoId, Long venditoreId, StatoInvito stato);
}
