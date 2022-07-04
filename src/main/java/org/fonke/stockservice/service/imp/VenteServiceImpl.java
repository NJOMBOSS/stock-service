package org.fonke.stockservice.service.imp;


import lombok.extern.slf4j.Slf4j;
import org.fonke.stockservice.dto.VenteDto;
import org.fonke.stockservice.entity.Article;
import org.fonke.stockservice.entity.LigneVente;
import org.fonke.stockservice.entity.Vente;
import org.fonke.stockservice.exeception.EntityNotFoundException;
import org.fonke.stockservice.exeception.ErrorCodes;
import org.fonke.stockservice.exeception.InvalidEntityException;
import org.fonke.stockservice.repository.ArticleRepository;
import org.fonke.stockservice.repository.LigneVenteRepository;
import org.fonke.stockservice.repository.VenteRepository;
import org.fonke.stockservice.service.VenteService;
import org.fonke.stockservice.validator.VenteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VenteServiceImpl implements VenteService {

    private ArticleRepository articleRepository;

    private VenteRepository venteRepository;

    private LigneVenteRepository ligneVenteRepository;

    @Autowired
    public VenteServiceImpl(ArticleRepository articleRepository, VenteRepository venteRepository,
                            LigneVenteRepository ligneVenteRepository) {
        this.articleRepository = articleRepository;
        this.venteRepository = venteRepository;
        this.ligneVenteRepository = ligneVenteRepository;
    }

    @Override
    public VenteDto save(VenteDto venteDto) {
        List<String> errors = VenteValidator.validate(venteDto);
        if(!errors.isEmpty()){
            log.error("la vente n'est pas valide");
            throw new InvalidEntityException("", ErrorCodes.VENTE_NOT_VALID, errors);
        }

        List<String> articleErrors = new ArrayList<>();
        venteDto.getLigneVenteDtoList().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticleDto().getId());
            if(article.isEmpty()){
                articleErrors.add("Aucun article avec l'ID" + ligneVenteDto.getArticleDto().getId() + "n'a ete trouve dans la BDD" );
            }
        });

        if(!articleErrors.isEmpty()){
            log.error("One or more articles were not found in te DB {}", errors);
            throw  new InvalidEntityException("un ou plusieurs artilces n'ont pas été trouvé dans la BDD",ErrorCodes.VENTE_NOT_VALID);
        }

        Vente saveVente = venteRepository.save(venteDto.toEntity(venteDto));

        venteDto.getLigneVenteDtoList().forEach(ligneVenteDto -> {
            LigneVente ligneVente = ligneVenteDto.toEntity(ligneVenteDto);
            ligneVenteRepository.save(ligneVente);
        });

        return venteDto.fromEntity(saveVente);
    }

    @Override
    public VenteDto findById(Integer id) {
        if(id == null){
            log.error("vente ID is NULL");
            return  null;
        }
        return venteRepository.findById(id)
                .map(VenteDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException("Aucune vente n'a été trouvé dans la BDD ",
                        ErrorCodes.VENTE_NOT_FOUND));
    }
/*
    @Override
    public VenteDto findByCodeVente(String codeVente) {
        if(!StringUtils.hasLength(codeVente)){
            log.error("Vente code is NULL");
            return  null;
        }
        return venteRepository.findVenteByCodeVente(codeVente)
                .map(VenteDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Aucune vente client n'a été trouvé avec le code " + codeVente, ErrorCodes.VENTE_NOT_VALID
                ));
    }
*/
    @Override
    public List<VenteDto> findAll() {
        return venteRepository.findAll().stream()
                .map(VenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if(id == null){
            log.error("Vente ID is NULL");
            return;
        }
     venteRepository.deleteById(id);
    }
}
