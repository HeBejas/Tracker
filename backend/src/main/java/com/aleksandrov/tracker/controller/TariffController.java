package com.aleksandrov.tracker.controller;

import com.aleksandrov.tracker.model.Tariff;
import com.aleksandrov.tracker.model.TariffStatus;
import com.aleksandrov.tracker.repository.TariffRepository;
import com.aleksandrov.tracker.repository.TariffStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
@RequiredArgsConstructor
public class TariffController {

    private final TariffRepository tariffRepository;
    private final TariffStatusRepository tariffStatusRepository;

    @GetMapping
    public ResponseEntity<List<Tariff>> getAllTariffs() {
        List<Tariff> tariffs = tariffRepository.findAll();
        return ResponseEntity.ok(tariffs);
    }

    @PostMapping
    public ResponseEntity<?> createTariff(@RequestBody Map<String, Object> request) {
        try {
            Tariff newTariff = new Tariff();
            newTariff.setName((String) request.get("name"));
            newTariff.setMaxProjects((Integer) request.get("maxProjects"));
            newTariff.setMaxUsers((Integer) request.get("maxUsers"));

            Number costNumber = (Number) request.get("cost");
            newTariff.setCost(BigDecimal.valueOf(costNumber.doubleValue()));

            Number statusNumber = (Number) request.get("status");
            if (statusNumber != null) {
                newTariff.setStatus(
                        tariffStatusRepository.findById(statusNumber.longValue())
                                .orElseThrow(() -> new RuntimeException("Статус не найден"))
                );
            };

            newTariff= tariffRepository.save(newTariff);
            return ResponseEntity.ok(newTariff);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Ошибка: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTariff(@PathVariable Long id) {
        try {
            tariffRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTariff(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Tariff tariff = tariffRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Тариф не найден"));

            tariff.setName((String) request.get("name"));
            tariff.setMaxProjects((Integer) request.get("maxProjects"));
            tariff.setMaxUsers((Integer) request.get("maxUsers"));
            tariff.setCost(new BigDecimal(request.get("cost").toString()));

            Object statusId = request.get("status");
            if (statusId != null) {
                TariffStatus status = tariffStatusRepository.findById(((Number) statusId).longValue())
                        .orElseThrow(() -> new RuntimeException("Статус не найден"));
                tariff.setStatus(status);
            }

            Tariff saved = tariffRepository.save(tariff);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка: " + e.getMessage());
        }
    }


}