package com.aleksandrov.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aleksandrov.tracker.model.Tariff;
import java.util.Optional;


@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long>{

}