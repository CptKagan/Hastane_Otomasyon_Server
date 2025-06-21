package com.hastaneotomasyon.doctor_service.util;

import com.hastaneotomasyon.doctor_service.enums.TestType;

import java.util.List;
import java.util.Map;

public class AllowedTestsByBranch {
    public static final Map<String, List<TestType>> TESTS_ALLOWED_BY_BRANCH = Map.ofEntries(
            Map.entry("dahiliye", List.of(TestType.CBC, TestType.URINE, TestType.LFT, TestType.KFT, TestType.GLUCOSE, TestType.LIPID, TestType.ELECTROLYTES, TestType.CRP, TestType.VITAMIN_D, TestType.TSH)),
            Map.entry("kardiyoloji", List.of(TestType.CBC, TestType.URINE, TestType.KFT, TestType.LIPID, TestType.ELECTROLYTES)),
            Map.entry("gogus-hastaliklari", List.of(TestType.CBC, TestType.URINE, TestType.CRP)),
            Map.entry("nefroloji", List.of(TestType.CBC, TestType.URINE, TestType.KFT, TestType.LIPID)),
            Map.entry("psikiyatri", List.of(TestType.CBC, TestType.URINE, TestType.TSH)),
            Map.entry("radyoloji", List.of(TestType.XRAY, TestType.MRI)),
            Map.entry("uroloji", List.of(TestType.CBC, TestType.URINE, TestType.PSA)),
            Map.entry("onkoloji", List.of(TestType.CBC, TestType.URINE, TestType.LFT, TestType.KFT, TestType.GLUCOSE, TestType.LIPID, TestType.CRP, TestType.XRAY, TestType.MRI, TestType.VITAMIN_D)),
            Map.entry("acil-servis", List.of(TestType.CBC, TestType.URINE, TestType.GLUCOSE, TestType.ELECTROLYTES, TestType.CRP, TestType.VITAMIN_D)),
            Map.entry("aile-hekimliği", List.of(TestType.CBC, TestType.URINE, TestType.LFT, TestType.KFT, TestType.VITAMIN_D)),
            Map.entry("genel-cerrahi", List.of(TestType.CBC, TestType.URINE, TestType.LFT, TestType.GLUCOSE)),
            Map.entry("noroloji", List.of(TestType.CBC, TestType.URINE, TestType.MRI, TestType.TSH)),
            Map.entry("oftalmoloji", List.of(TestType.EYE_PRESSURE)),
            Map.entry("pediatri", List.of(TestType.CBC, TestType.URINE, TestType.LFT, TestType.CRP)),
            Map.entry("dermatoloji", List.of(TestType.CBC, TestType.URINE)),
            Map.entry("kalp-damar-cerrahisi", List.of(TestType.CBC, TestType.URINE, TestType.ELECTROLYTES)),
            Map.entry("kbb", List.of(TestType.CBC, TestType.URINE))
    );

    private AllowedTestsByBranch() {}
}
