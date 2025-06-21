package com.hastaneotomasyon.appointment_service.dto;

import java.util.List;

public record GunlukTestRequest (
        List<Long> izinliTestlerId
) {
}
