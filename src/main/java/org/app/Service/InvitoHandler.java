package org.app.Service;

import org.app.DTO.InvitoDTO;
import org.app.Model.StatoInvito;

public interface InvitoHandler {
    public InvitoDTO rispondiInvito(Long eventoId, Long venditoreId, StatoInvito stato);
}
