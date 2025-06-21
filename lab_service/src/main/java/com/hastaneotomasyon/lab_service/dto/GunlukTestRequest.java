package com.hastaneotomasyon.lab_service.dto;

import java.util.List;

public record GunlukTestRequest (
        List<Long> izinliTestlerId
) {
}
