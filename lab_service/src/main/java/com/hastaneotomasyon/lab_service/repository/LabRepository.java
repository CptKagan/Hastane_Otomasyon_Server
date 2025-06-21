package com.hastaneotomasyon.lab_service.repository;

import com.hastaneotomasyon.lab_service.dto.TestSonucuResponse;
import com.hastaneotomasyon.lab_service.model.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabRepository extends JpaRepository<Lab, String> {
}
