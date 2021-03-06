package org.fonke.stockservice.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "lignecommandefournisseur")
public class LigneCommandeFournisseur extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name="idarticle")
    private Article article;

    @ManyToOne
    @JoinColumn(name="idcommandefournisseur")
    private CommandeFournisseur  commandeFournisseurs;

    @Column(name="quantite")
    private BigDecimal quantite;

    @Column(name="prixunitaire")
    private BigDecimal prixunitaire;

    @Column(name = "identreprise")
    private Integer idEntreprise;


}
