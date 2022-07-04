package org.fonke.stockservice.service;

import org.fonke.stockservice.dto.CommandeFournisseurDto;

import java.util.List;

public interface CommandeFournisseurService {

    CommandeFournisseurDto save(CommandeFournisseurDto commandeFournisseurDto);

    CommandeFournisseurDto  findById(Integer id);

    List<CommandeFournisseurDto > findAll();

    void delete(Integer id);
}
