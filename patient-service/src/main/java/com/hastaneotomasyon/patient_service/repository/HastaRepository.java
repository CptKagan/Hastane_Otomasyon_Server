package com.hastaneotomasyon.patient_service.repository;

import com.hastaneotomasyon.patient_service.model.Hasta;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HastaRepository extends JpaRepository<Hasta, Long> {

    Optional<Hasta> findByTcKimlik(String tcKimlik);

    Optional<Hasta> findByIsimAndSoyIsimAndTcKimlik(String isim, String soyIsim, String tcKimlik);
}
