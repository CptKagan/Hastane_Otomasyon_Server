package com.hastaneotomasyon.lab_service.repository;

import com.hastaneotomasyon.lab_service.dto.TestSonucuResponse;
import com.hastaneotomasyon.lab_service.model.TestResult;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
     Optional<TestResult> findByRandevuIdAndTestId(Long randevuId, Long testId);
     List<TestResult> findAllByRandevuId(Long randevuId);

     int countByRandevuId(Long randevuId);
}
