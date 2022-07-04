package org.fonke.stockservice.dto;


import lombok.Builder;
import lombok.Data;
import org.fonke.stockservice.entity.Vente;

import java.util.Date;
import java.util.List;


@Data
@Builder
public class VenteDto {

    private Integer id;

    private String code;

    private Date datevente;

    private String commentaire;


    private List<LigneVenteDto> ligneVenteDtoList;

      public static VenteDto fromEntity(Vente vente){
          if(vente == null){
              return null;
          }

          return VenteDto.builder()
                  .code(vente.getCode())
                 .datevente(vente.getDatevente())
                  .commentaire(vente.getCommentaire())
                  .build();
      }

      public static Vente toEntity(VenteDto venteDto){
          if(venteDto == null){
              return null;
          }

          Vente v = new Vente();
          v.setId(venteDto.getId());
          v.setCode(venteDto.getCode());
          v.setDatevente(venteDto.getDatevente());
          v.setCommentaire(venteDto.getCommentaire());
          return v;
      }
}
