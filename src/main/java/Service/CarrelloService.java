package Service;

import DTO.CarrelloDTO;
import DTO.CarrelloMapper;
import Exceptions.BusinessException;
import Exceptions.NotFoundException;
import Model.*;
import Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CarrelloService {

    private final CarrelloRepository carrelloRepo;
    private final CarrelloItemRepository carrelloItemRepo;
    private final CarrelloBundleItemRepository carrelloBundleItemRepo;
    private final AcquirenteRepository acquirenteRepo;
    private final ProdottoRepository prodottoRepo;
    private final BundleRepository bundleRepo;

    public CarrelloService(CarrelloRepository carrelloRepo,
                           CarrelloItemRepository carrelloItemRepo,
                           CarrelloBundleItemRepository carrelloBundleItemRepo,
                           AcquirenteRepository acquirenteRepo,
                           ProdottoRepository prodottoRepo,
                           BundleRepository bundleRepo) {
        this.carrelloRepo = carrelloRepo;
        this.carrelloItemRepo = carrelloItemRepo;
        this.carrelloBundleItemRepo = carrelloBundleItemRepo;
        this.acquirenteRepo = acquirenteRepo;
        this.prodottoRepo = prodottoRepo;
        this.bundleRepo = bundleRepo;
    }

    private Carrello loadOrCreateGraph(Long acquirenteId) {
        return carrelloRepo.findGraphByAcquirenteId(acquirenteId).orElseGet(() -> {
            Acquirente a = acquirenteRepo.findById(acquirenteId)
                    .orElseThrow(() -> new NotFoundException("Acquirente non trovato: " + acquirenteId));
            Carrello c = new Carrello();
            c.setAcquirente(a);
            return carrelloRepo.save(c);
        });
    }

