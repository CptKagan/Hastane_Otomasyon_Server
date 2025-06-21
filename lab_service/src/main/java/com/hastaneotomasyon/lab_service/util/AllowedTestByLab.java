package com.hastaneotomasyon.lab_service.util;

import com.hastaneotomasyon.lab_service.enums.TestType;

import java.util.List;
import java.util.Map;

public class AllowedTestByLab {
    public static final Map<String, List<TestType>> TESTS_BY_LAB = Map.of(
            "kan-lab", List.of(
                    TestType.CBC,
                    TestType.GLUCOSE,
                    TestType.LIPID,
                    TestType.ELECTROLYTES,
                    TestType.CRP,
                    TestType.PSA
            ),
            "biyokimya-lab", List.of(
                    TestType.URINE,
                    TestType.KFT,
                    TestType.LFT
            ),
            "hormon-lab", List.of(
                    TestType.VITAMIN_D,
                    TestType.TSH
            ),
            "goruntuleme-lab", List.of(
                    TestType.XRAY,
                    TestType.MRI
            ),
            "goz-lab", List.of(
                    TestType.EYE_PRESSURE
            )
    );

    private AllowedTestByLab() {}
}
