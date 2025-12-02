package org.app.Service;
import org.app.DTO.ProdottoDTO;
import org.app.DTO.ProdottoMapper;
import org.app.Model.*;
import org.app.Repository.CertificazioneRepository;
import org.app.Repository.CuratoreRepository;
import org.app.Repository.ProdottoRepository;
import org.app.Repository.VenditoreRepository;
import org.app.Exceptions.BusinessException;
import org.app.Exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProdottoService {

    private final ProdottoRepository prodottoRepo;
    private final VenditoreRepository venditoreRepo;
    private final CuratoreRepository curatoreRepo;
    private final CertificazioneRepository certificazioneRepo;

    public ProdottoService(ProdottoRepository prodottoRepo,
                           VenditoreRepository venditoreRepo,
                           CuratoreRepository curatoreRepo,
                           CertificazioneRepository certificazioneRepo) {
        this.prodottoRepo = prodottoRepo;
        this.venditoreRepo = venditoreRepo;
        this.curatoreRepo = curatoreRepo;
        this.certificazioneRepo = certificazioneRepo;
    }

    public ProdottoDTO creaProdotto(Long venditoreId, String nome, BigDecimal prezzo,int quantita) {
        Venditore v = venditoreRepo.findById(venditoreId)
                .orElseThrow(() -> new NotFoundException("Venditore non trovato: " + venditoreId));
        Prodotto p = new Prodotto();
        p.setNome(nome);
        p.setPrezzo(prezzo);
        p.setVenditore(v);
        p.setStato(StatoProdotto.IN_VALIDAZIONE);
        p.setQuantitaDisponibile(quantita);
        p = prodottoRepo.save(p);

        return ProdottoMapper.toDTO(p);
    }

    public ProdottoDTO getById(Long prodottoId) {
        Prodotto p = prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato: " + prodottoId));
        return ProdottoMapper.toDTO(p);
    }

    public List<ProdottoDTO> listaVendibili() {
        return ProdottoMapper.toDTO(prodottoRepo.findByStato(StatoProdotto.APPROVATO));
    }

    public ProdottoDTO approvaProdotto(Long prodottoId, Long curatoreId) {
        Prodotto p = prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato: " + prodottoId));
        if (p.getStato() == StatoProdotto.APPROVATO) throw new BusinessException("Già approvato.");
        if (p.getStato() == StatoProdotto.RIFIUTATO) throw new BusinessException("Rifiutato: non approvabile.");
        if (certificazioneRepo.existsByProdottoId(prodottoId))
            throw new BusinessException("Certificazione già presente.");

        Curatore c = curatoreRepo.findById(curatoreId)
                .orElseThrow(() -> new NotFoundException("Curatore non trovato: " + curatoreId));


        p.setStato(StatoProdotto.APPROVATO);

        return ProdottoMapper.toDTO(p);
    }

    public ProdottoDTO rifiutaProdotto(Long prodottoId, String motivo) {
        Prodotto p = prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato: " + prodottoId));
        if (p.getStato() == StatoProdotto.APPROVATO) throw new BusinessException("Già approvato.");
        if (certificazioneRepo.existsByProdottoId(prodottoId))
            throw new BusinessException("Certificazione già presente.");
        p.setStato(StatoProdotto.RIFIUTATO);
        return ProdottoMapper.toDTO(p);
    }

    public ProdottoDTO aggiornaDescrizione(Long prodottoId, Long venditoreId, String nuovaDescrizione) {
        // 1. Trova il prodotto
        Prodotto p = prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato: " + prodottoId));

        // 2. Controllo di Ownership (Sicurezza)
        // Verifica che il prodotto appartenga al venditore che sta facendo la richiesta
        if (!p.getVenditore().getId().equals(venditoreId)) {
            throw new BusinessException("Non puoi modificare la descrizione di un prodotto non tuo.");
        }

        // 3. Aggiorna il campo
        p.setDescrizione(nuovaDescrizione);

        // 4. Salva (automatico grazie a @Transactional) e ritorna DTO
        return ProdottoMapper.toDTO(p);
    }

    public ProdottoDTO aggiornaPrezzo(Long prodottoId, BigDecimal nuovoPrezzo) {
        Prodotto p = prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato: " + prodottoId));
        p.setPrezzo(nuovoPrezzo);
        return ProdottoMapper.toDTO(p);
    }

    public ProdottoDTO aggiungiCert(Long prodottoId, Certificazione certificato) {
        Prodotto p = prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato: " + prodottoId));



        return ProdottoMapper.toDTO(p);
    }

    public void elimina(Long prodottoId) {
        prodottoRepo.delete(prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato: " + prodottoId)));
    }

    public ProdottoDTO aggiungiCertificazione(Long prodottoId, String descrizione) {
        Prodotto p = prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato: " + prodottoId));

        // 1. Controllo di Sicurezza (Ownership)
        // Verifichiamo che il venditore che sta chiamando sia il proprietario del prodotto


        // 2. Controllo Stato (Opzionale: se è già approvato, magari vuoi solo aggiungere un'altra cert)
        // if (p.getStato() == StatoProdotto.APPROVATO) ...

        // 3. Crea la Certificazione (SENZA Curatore)
        Certificazione cert = new Certificazione();
        cert.setDescrizione(descrizione);
        cert.setProdotto(p);
        cert.setDataApprovazione(LocalDateTime.now());
        // cert.setCuratoreValidatore(null); // Non c'è curatore

        // 4. Aggiungi alla lista
        p.getCertificazioni().add(cert);

        // 5. Aggiorna stato (Se la certificazione rende il prodotto vendibile)
        p.setStato(StatoProdotto.APPROVATO);

        return ProdottoMapper.toDTO(p);
    }

    public ProdottoDTO rimuoviCertificazione(Long prodottoId, Long certificazioneId) {
        Prodotto p = prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));

        Certificazione certDaRimuovere = p.getCertificazioni().stream()
                .filter(c -> c.getId().equals(certificazioneId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Certificazione non trovata"));

        p.getCertificazioni().remove(certDaRimuovere);
        // certDaRimuovere verrà cancellata grazie a orphanRemoval=true

        if (p.getCertificazioni().isEmpty()) {
            p.setStato(StatoProdotto.IN_VALIDAZIONE);
        }

        return ProdottoMapper.toDTO(p);
    }
}