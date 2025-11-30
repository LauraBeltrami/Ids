package Service;

import DTO.VenditoreIndirizzoDTO;
import Exceptions.BusinessException;
import Exceptions.NotFoundException;
import Model.Venditore;
import Repository.VenditoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // Ottimizza le performance per la lettura
public class MapService {

    private final VenditoreRepository venditoreRepo;

    public MapService(VenditoreRepository venditoreRepo) {
        this.venditoreRepo = venditoreRepo;
    }

    /**Restituisce la lista di tutti gli indirizzi dei venditori approvati.*/
    public List<VenditoreIndirizzoDTO> getAllIndirizzi() {
        return venditoreRepo.findByApprovatoTrue().stream()
                .map(v -> new VenditoreIndirizzoDTO(
                        v.getId(),
                        v.getNome(),
                        v.getIndirizzo(),
                        v.getClass().getSimpleName() // Restituisce "Venditore", "Distributore" o "Trasformatore"
                ))
                .collect(Collectors.toList());
    }

    /*Restituisce l'indirizzo di uno specifico venditore. */

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