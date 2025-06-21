package com.hastaneotomasyon.lab_service.model;

import com.hastaneotomasyon.lab_service.enums.TestResultStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long randevuId;
    private Long testId;
    private String sonuc;

    private String labUserId;
    private LocalDateTime islemZamani;

    @Enumerated(EnumType.STRING)
    private TestResultStatus status;

    public TestResult(Long randevuId, Long testId, String sonuc, String labUserId, LocalDateTime islemZamani, TestResultStatus testResultStatus){
        this.randevuId = randevuId;
        this.testId = testId;
        this.sonuc = sonuc;
        this.labUserId = labUserId;
        this.islemZamani = islemZamani;
        this.status = testResultStatus;
    }
}
