package org.fonke.stockservice.repository;


import org.fonke.stockservice.entity.MvStk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MvStkRepository extends JpaRepository<MvStk, Integer> {
}
