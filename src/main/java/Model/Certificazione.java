package Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificazioni")
public class Certificazione {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String descrizione;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prodotto_id", nullable = false) // RIMOSSO: unique = true
    private Prodotto prodotto;

    @Column(nullable = false)
    private LocalDateTime dataApprovazione;

    public Certificazione() {}
    public Certificazione(Long id, String descrizione, Prodotto prodotto, LocalDateTime dataApprovazione) {
        this.id = id;
        this.descrizione = descrizione;
        this.prodotto = prodotto;
        this.dataApprovazione = dataApprovazione;
    }

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public Prodotto getProdotto() { return prodotto; }
    public void setProdotto(Prodotto prodotto) { this.prodotto = prodotto; }
    public LocalDateTime getDataApprovazione() { return dataApprovazione; }
    public void setDataApprovazione(LocalDateTime dataApprovazione) { this.dataApprovazione = dataApprovazione; }
}