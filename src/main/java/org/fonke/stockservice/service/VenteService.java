package org.fonke.stockservice.service;

import org.fonke.stockservice.dto.VenteDto;

import java.util.List;

public interface VenteService {

   VenteDto save(VenteDto venteDto);

    VenteDto  findById(Integer id);

    List<VenteDto > findAll();

    void delete(Integer id);
}
