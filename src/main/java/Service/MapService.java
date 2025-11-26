package Service;
import Exceptions.BusinessException;
import Exceptions.NotFoundException;
import Model.Venditore;
import Repository.VenditoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true) // Ottimizza le performance per la lettura
public class MapService {

    private final VenditoreRepository venditoreRepo;

    public MapService(VenditoreRepository venditoreRepo) {
        this.venditoreRepo = venditoreRepo;
    }


    public VenditoreIndirizzoDTO getIndirizzoVenditore(Long venditoreId) {
        Venditore v = venditoreRepo.findById(venditoreId)
                .orElseThrow(() -> new NotFoundException("Venditore non trovato: " + venditoreId));

        // Controllo opzionale: mostriamo l'indirizzo solo se è approvato?
        if (!v.isApprovato()) {
            throw new BusinessException("Venditore non attivo o non approvato.");
        }

        return new VenditoreIndirizzoDTO(
                v.getId(),
                v.getNome(),
                v.getIndirizzo(),
                v.getClass().getSimpleName()
                );
    }
}