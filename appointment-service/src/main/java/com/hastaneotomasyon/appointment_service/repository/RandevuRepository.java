package com.hastaneotomasyon.appointment_service.repository;

import com.hastaneotomasyon.appointment_service.model.Randevu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RandevuRepository extends JpaRepository<Randevu, Long> {
    Optional<Randevu> findByTercihEdilenTarihAndDoktorId(LocalDateTime tercihEdilenTarih, String doktorId);
    List<Randevu> findByTercihEdilenTarihAndHastaId(LocalDateTime tercihEdilenTarih, Long hastaId);
    Optional<Randevu> findByRandevuKodu(String randevuKodu);
    List<Randevu> findAllByDoktorIdAndTercihEdilenTarihBetween(String doktorId, LocalDateTime baslangic, LocalDateTime bitis);
    List<Randevu> findAllByTercihEdilenTarihBetween(LocalDateTime baslangic, LocalDateTime bitis);


    Boolean existsByTercihEdilenTarihAndDoktorId(LocalDateTime tercihEdilenTarih, String doktorId);
    Boolean existsByTercihEdilenTarihAndHastaId(LocalDateTime tercihEdilenTarih, Long hastaId);
}
