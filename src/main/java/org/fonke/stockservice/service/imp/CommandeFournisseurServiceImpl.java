package org.fonke.stockservice.service.imp;


import lombok.extern.slf4j.Slf4j;
import org.fonke.stockservice.dto.CommandeFournisseurDto;
import org.fonke.stockservice.dto.LigneCommandeFournisseurDto;
import org.fonke.stockservice.entity.Article;
import org.fonke.stockservice.entity.CommandeFournisseur;
import org.fonke.stockservice.entity.Fournisseur;
import org.fonke.stockservice.entity.LigneCommandeFournisseur;
import org.fonke.stockservice.exeception.EntityNotFoundException;
import org.fonke.stockservice.exeception.ErrorCodes;
import org.fonke.stockservice.exeception.InvalidEntityException;
import org.fonke.stockservice.repository.ArticleRepository;
import org.fonke.stockservice.repository.CommandeFournisseurRepository;
import org.fonke.stockservice.repository.FournisseurRepository;
import org.fonke.stockservice.repository.LigneCommandeFournisseurRepository;
import org.fonke.stockservice.service.CommandeFournisseurService;
import org.fonke.stockservice.validator.CommandeFournisseurValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

    private CommandeFournisseurRepository commandeFournisseurRepository;
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    private FournisseurRepository fournisseurRepository;
    private ArticleRepository articleRepository;


    @Autowired
    public CommandeFournisseurServiceImpl(CommandeFournisseurRepository commandeFournisseurRepository,
                                          LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository,
                                          FournisseurRepository fournisseurRepository,
                                          ArticleRepository articleRepository) {
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public CommandeFournisseurDto save(CommandeFournisseurDto commandeFournisseurDto) {
        List<String> errors = CommandeFournisseurValidator.validate(commandeFournisseurDto);
        if(!errors.isEmpty()){
            log.error("Commande fournisseur n'est pas valide");
            throw new InvalidEntityException("La commande fournisseur n'est pas valide", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID, errors);
        }

        Optional<Fournisseur> fournisseur= fournisseurRepository.findById(commandeFournisseurDto.getFournisseurDto().getId());
        if(fournisseur.isEmpty()){
            log.warn("Fournisseur  with ID {} was not found in the BD",commandeFournisseurDto.getFournisseurDto().getId());
            throw new EntityNotFoundException("Aucun fournisseur avec l'ID"+ commandeFournisseurDto.getFournisseurDto().getId()
                    + "n'a ete trouve dans la BDD",ErrorCodes.FOUNISSEUR_NOT_FOUND);
        }

        List<String> articleErrors = new ArrayList<>();

        if(commandeFournisseurDto.getLigneCommandeFournisseurDto()!= null){
            commandeFournisseurDto.getLigneCommandeFournisseurDto().forEach(ligCmdClt->{
                if(ligCmdClt.getArticleDto() != null){
                    Optional<Article> article = articleRepository.findById(ligCmdClt.getArticleDto().getId());
                    if (article.isEmpty()){
                        articleErrors.add("L'article avec l'ID" + ligCmdClt.getArticleDto().getId() + "n'existe pas");
                    }
                } else {
                    articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
                }
            });
        }

        if(!articleErrors.isEmpty()){
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BDD",ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }

        CommandeFournisseur saveCmdFns = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseurDto));

        if(commandeFournisseurDto.getLigneCommandeFournisseurDto()!= null){
            commandeFournisseurDto.getLigneCommandeFournisseurDto().forEach(ligCmdFns ->{
               LigneCommandeFournisseur ligneCommandeFournisseur = LigneCommandeFournisseurDto.toEntity(ligCmdFns);
               ligneCommandeFournisseur.setCommandeFournisseurs(saveCmdFns);
                ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
            });
        }

        return CommandeFournisseurDto.fromEntity(saveCmdFns);
    }


    @Override
    public CommandeFournisseurDto findById(Integer id) {
        if(id == null){
            log.error("Commande fournisseur ID is NULL");
            return null;
        }
        return commandeFournisseurRepository.findById(id)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(()-> new  EntityNotFoundException(
                        "Aucune commande fournisseur n' a ete trouve avec l'ID" + id, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND)
                );
    }
/*
    @Override
    public CommandeFournisseurDto findByCodeCommandeClient(String codeCommandeFournisseur) {
        if(!StringUtils.hasLength(codeCommandeFournisseur)) {
            log.error("Commande client CODE is null");
            return null;
        }

        return commandeFournisseurRepository.findCommandeFournisseurByCode(codeCommandeFournisseur)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Aucune commande fournisseur n'a ete trouve avec le CODE" + codeCommandeFournisseur,
                        ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND)
                );
    }
*/
    @Override
    public List<CommandeFournisseurDto> findAll() {
        return commandeFournisseurRepository.findAll().stream()
                .map(CommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {

        if(id == null){
            log.error("Commande fournisseur ID is NULL");
            return ;
        }
        commandeFournisseurRepository.deleteById(id);
    }
}
