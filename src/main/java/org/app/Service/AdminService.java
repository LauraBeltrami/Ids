package org.app.Service;

import jakarta.transaction.Transactional;
import org.app.DTO.RichiestaApprovazioneDTO;
import org.app.Exceptions.NotFoundException;
import org.app.Model.Animatore;
import org.app.Model.Curatore;
import org.app.Model.Venditore;
import org.app.Repository.AnimatoreRepository;
import org.app.Repository.CuratoreRepository;
import org.app.Repository.VenditoreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class AdminService {

    private final VenditoreRepository venditoreRepo;
    private final AnimatoreRepository animatoreRepo;
    private final CuratoreRepository curatoreRepo;

    public AdminService(VenditoreRepository venditoreRepo,
                        AnimatoreRepository animatoreRepo,
                        CuratoreRepository curatoreRepo) {
        this.venditoreRepo = venditoreRepo;
        this.animatoreRepo = animatoreRepo;
        this.curatoreRepo = curatoreRepo;
    }

    public List<RichiestaApprovazioneDTO> getUtentiInAttesa() {
        List<RichiestaApprovazioneDTO> lista = new ArrayList<>();

        // 1. Prendi i Venditori
        venditoreRepo.findByApprovatoFalse().forEach(v ->
                lista.add(new RichiestaApprovazioneDTO(v.getId(), v.getNome(), "VENDITORE"))
        );

        // 2. Prendi gli Animatori
        animatoreRepo.findByApprovatoFalse().forEach(a ->
                lista.add(new RichiestaApprovazioneDTO(a.getId(), a.getNome(), "ANIMATORE"))
        );

        // 3. Prendi i Curatori
        curatoreRepo.findByApprovatoFalse().forEach(c ->
                lista.add(new RichiestaApprovazioneDTO(c.getId(), c.getNome(), "CURATORE"))
        );

        return lista;
    }

    public void approvaUtente(Long id, String ruolo) {
        switch (ruolo.toUpperCase()) {
            case "VENDITORE":
            case "DISTRIBUTORE": // Se i distributori stanno nella tabella venditori
                Venditore v = venditoreRepo.findById(id)
                        .orElseThrow(() -> new NotFoundException("Venditore non trovato"));
                v.setApprovato(true);
                break;

            case "ANIMATORE":
                Animatore a = animatoreRepo.findById(id)
                        .orElseThrow(() -> new NotFoundException("Animatore non trovato"));
                a.setApprovato(true);
                break;

            case "CURATORE":
                Curatore c = curatoreRepo.findById(id)
                        .orElseThrow(() -> new NotFoundException("Curatore non trovato"));
                c.setApprovato(true);
                break;

            default:
                throw new IllegalArgumentException("Ruolo non valido: " + ruolo);
        }
    }
}
