package com.hastaneotomasyon.doctor_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TestType {
    CBC(1L, "Kan Sayımı"),
    URINE(2L, "İdrar Tahlili"),
    LFT(3L, "Karaciğer Fonksiyon Testleri"),
    KFT(4L, "Böbrek Fonksiyon Testleri"),
    GLUCOSE(5L, "Kan Şekeri"),
    LIPID(6L, "Lipid Profili"),
    ELECTROLYTES(7L, "Elektrolit Paneli"),
    CRP(8L, "CRP"),
    XRAY(9L, "Röntgen"),
    MRI(10L, "MR"),
    PSA(11L, "PSA Testi"),
    VITAMIN_D(13L, "Vitamin D"),
    TSH(14L, "Tiroid Fonksiyon Testleri"),
    EYE_PRESSURE(15L, "Göz Tansiyonu Ölçümü");

    private final Long id;
    private final String testAdi;


    public static TestType fromId(Long id) {
        for (TestType type : values()) {
            if (type.getId().equals(id)) return type;
        }
        throw new IllegalArgumentException("Unknown Test ID: " + id);
    }
}