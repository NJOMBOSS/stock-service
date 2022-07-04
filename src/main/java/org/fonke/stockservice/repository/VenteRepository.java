package org.fonke.stockservice.repository;


import org.fonke.stockservice.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Integer> {


}
