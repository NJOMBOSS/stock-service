package org.fonke.stockservice.service;


import org.fonke.stockservice.dto.CommandeClientDto;

import java.util.List;

public interface CommandeClientService {

    CommandeClientDto save(CommandeClientDto  commandeClientDto);

    CommandeClientDto  findById(Integer id);

    List<CommandeClientDto > findAll();

    void delete(Integer id);
}
