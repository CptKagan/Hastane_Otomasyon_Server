package com.hastaneotomasyon.appointment_service.dto;

import java.util.List;

public record TestRequest(
        List<Long> testIdList
) {
}
