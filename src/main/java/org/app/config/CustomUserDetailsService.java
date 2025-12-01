package org.app.config;

import org.app.Model.*;
import org.app.Repository.*;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AcquirenteRepository acquirenteRepo;
    private final VenditoreRepository venditoreRepo;
    private final AnimatoreRepository animatoreRepo;
    private final CuratoreRepository curatoreRepo;
    private final GestoreRepository gestoreRepo;

    public CustomUserDetailsService(AcquirenteRepository acquirenteRepo,
                                    VenditoreRepository venditoreRepo,
                                    AnimatoreRepository animatoreRepo,
                                    CuratoreRepository curatoreRepo,
                                    GestoreRepository gestoreRepo) {
        this.acquirenteRepo = acquirenteRepo;
        this.venditoreRepo = venditoreRepo;
        this.animatoreRepo = animatoreRepo;
        this.curatoreRepo = curatoreRepo;
        this.gestoreRepo = gestoreRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 0. Gestore piattaforma (admin)
        Optional<GestorePiattaforma> gestore = gestoreRepo.findByEmail(email);
        if (gestore.isPresent()) {
            return buildUser(gestore.get());
        }

        // 1. Acquirente
        Optional<Acquirente> acq = acquirenteRepo.findByEmail(email);
        if (acq.isPresent()) {
            return buildUser(acq.get());
        }

        // 2. Venditore
        Optional<Venditore> ven = venditoreRepo.findByEmail(email);
        if (ven.isPresent()) {
            return buildUser(ven.get());
        }

        // 3. Animatore
        Optional<Animatore> anim = animatoreRepo.findByEmail(email);
        if (anim.isPresent()) {
            return buildUser(anim.get());
        }

        // 4. Curatore
        Optional<Curatore> cur = curatoreRepo.findByEmail(email);
        if (cur.isPresent()) {
            return buildUser(cur.get());
        }

        throw new UsernameNotFoundException("Utente non trovato con email: " + email);
    }

    private UserDetails buildUser(AbstractUtente utente) {
        // es: utente.getRuolo() = "ACQUIRENTE", "VENDITORE", "GESTORE_PIATTAFORMA", ...
        String role = utente.getRuolo();

        // Spring Security si aspetta authorities tipo "ROLE_ACQUIRENTE"
        String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;

        return User.builder()
                .username(utente.getEmail())
                .password(utente.getPassword())
                .authorities(authority)
                .disabled(!utente.isApprovato())
                .build();
    }
}
