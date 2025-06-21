package com.hastaneotomasyon.patient_service;

import com.hastaneotomasyon.patient_service.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PatientServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
