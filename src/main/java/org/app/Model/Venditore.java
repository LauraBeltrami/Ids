package org.app.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "venditori", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_venditore", length = 20)
@NoArgsConstructor
@Getter
@Setter
public class Venditore extends AbstractUtente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable = false, unique = true)
    private String nome;

    @OneToMany(mappedBy = "venditore",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, // Se cancelli un venditore, cancelli i suoi inviti
            orphanRemoval = true)
    private Set<InvitoEvento> invitiRicevuti = new HashSet<>();
    @NotBlank
    @Column(nullable = false)
    protected String indirizzo;
    @OneToMany(mappedBy = "venditore", fetch = FetchType.LAZY)
    private Set<Prodotto> prodotti = new HashSet<>();

    public Venditore(Long id, String nome, String indirizzo, String email, String password) {
        super();
        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo; // <-- Assegnazione
        this.email = email;
        this.password = password;
        this.ruolo = "ROLE_VENDITORE";
        this.approvato = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Set<Prodotto> getProdotti() { return prodotti; }
    public void setProdotti(Set<Prodotto> prodotti) { this.prodotti =prodotti;}

}