package org.app.Model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("DISTRIBUTORE")
public class Distributore extends Venditore {

    @OneToMany(mappedBy = "distributore", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bundle> bundles = new HashSet<>();

    public Distributore() {super();}

    public Distributore(Long id, String nome, String indirizzo, String email, String password) {
        super(id, nome, indirizzo, email, password);
    }

    public Set<Bundle> getBundles() { return bundles; }
    public void setBundles(Set<Bundle> bundles) { this.bundles = bundles; }
}