package com.hastaneotomasyon.appointment_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hastaneotomasyon.appointment_service.dto.RandevuRequest;
import com.hastaneotomasyon.appointment_service.dto.RandevuResponse;
import com.hastaneotomasyon.appointment_service.dto.RandevuTarihKontrolRequest;
import com.hastaneotomasyon.appointment_service.service.RandevuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ControllerTests {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Mock
    private RandevuService randevuService;

    @InjectMocks
    private RandevuController randevuController;

    @BeforeEach
    void setUp(){
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(randevuController)
                .setValidator(localValidatorFactoryBean)
                .build();
    }

    @Test
    void postTarihKontrol_whenValid_then200AndTrue() throws Exception {
        // Arrange
        RandevuTarihKontrolRequest randevuTarihKontrolRequest = new RandevuTarihKontrolRequest("doktor-123", LocalDateTime.of(2025,6,20,10,0), 5L);
        String json = objectMapper.writeValueAsString(randevuTarihKontrolRequest);
        when(randevuService.tarihKontrol(any())).thenReturn(true); // içerik önemli değil, controller işini yapıyor mu?

        // Act & Assert
        mockMvc.perform(post("/api/randevu/tarihkontrol")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(randevuService).tarihKontrol(any(RandevuTarihKontrolRequest.class));
    }
}
