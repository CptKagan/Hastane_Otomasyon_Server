package com.hastaneotomasyon.doctor_service.repository;

import com.hastaneotomasyon.doctor_service.model.Doktor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doktor, String> {
}
