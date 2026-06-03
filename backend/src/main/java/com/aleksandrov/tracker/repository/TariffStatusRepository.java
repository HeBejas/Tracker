package com.aleksandrov.tracker.repository;

import com.aleksandrov.tracker.model.TariffStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TariffStatusRepository extends JpaRepository<TariffStatus, Long> {
}

