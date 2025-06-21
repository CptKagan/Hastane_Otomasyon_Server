package com.hastaneotomasyon.lab_service.service;

import com.hastaneotomasyon.lab_service.client.AppointmentClient;
import com.hastaneotomasyon.lab_service.dto.*;
import com.hastaneotomasyon.lab_service.enums.TestResultStatus;
import com.hastaneotomasyon.lab_service.enums.TestType;
import com.hastaneotomasyon.lab_service.model.Lab;
import com.hastaneotomasyon.lab_service.model.TestResult;
import com.hastaneotomasyon.lab_service.repository.LabRepository;
import com.hastaneotomasyon.lab_service.repository.TestResultRepository;
import com.hastaneotomasyon.lab_service.util.AllowedTestByLab;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LabService {
    private final LabRepository labRepository;
    private final KeycloakClientService keycloakClientService;
    private final AppointmentClient appointmentClient;
    private final TestResultRepository testResultRepository;

    public List<String> jwtRolleriAl(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) {
            return List.of();
        }
        return (List<String>) realmAccess.getOrDefault("roles", List.of());
    }

    public String kullaniciBransiniAl(List<String> roller) {
        for (String rol : roller) {
            if (!rol.startsWith("ROLE_")
                    && !rol.equals("lab")
                    && !rol.equals("default-roles-hastane-otomasyon")
                    && !rol.equals("offline_access")
                    && !rol.equals("uma_authorization")) {
                return rol;
            }
        }
        throw new IllegalArgumentException("Branş rolü bulunamadı.");
    }

    public void fetchAllLabUsers() {
        List<LabResponse> labUsers = keycloakClientService.fetchAllLabUsers();
        for (LabResponse labResponse : labUsers) {
            Lab lab = new Lab(labResponse);
            System.out.println(lab);
            labRepository.save(lab);
        }
    }

    public List<TestResponse> gunlukTestgoruntule(Jwt jwt) {
        List<String> roller = jwtRolleriAl(jwt);
        String brans = kullaniciBransiniAl(roller);

        List<TestType> izinliTestler = AllowedTestByLab.TESTS_BY_LAB.get(brans);
        if (izinliTestler == null) {
            throw new IllegalArgumentException("Branşınıza ait test bulunamadı.");
        }

        List<Long> izinliTestlerId = new ArrayList<>();

        for (TestType testType : izinliTestler) {
            izinliTestlerId.add(testType.getId());
        }

        GunlukTestRequest gunlukTestRequest = new GunlukTestRequest(izinliTestlerId);

        List<TestResponse> testResponsesGelen = appointmentClient.gunlukTestGoruntule(gunlukTestRequest);
        List<TestResponse> testResponseGidecek = new ArrayList<>();
        for (TestResponse testResponse : testResponsesGelen) {
            TestResponse testResponseGidecekSingle = new TestResponse(testResponse.randevuId(), testResponse.doktorId(), testResponse.hastaId(), testResponse.testId(), TestType.fromId(testResponse.testId()), testResponse.tercihEdilenTarih());
            testResponseGidecek.add(testResponseGidecekSingle);
        }
        return testResponseGidecek;
    }

    public TestSonucuResponse testSonucuEkle(Jwt jwt, TestSonucuRequest testSonucuRequest) {
        List<String> roller = jwtRolleriAl(jwt);
        String brans = kullaniciBransiniAl(roller);

        List<TestType> izinliTestler = AllowedTestByLab.TESTS_BY_LAB.get(brans);
        if (izinliTestler == null) {
            throw new IllegalArgumentException("Branşınıza ait test bulunamadı.");
        }

        boolean izinliMi = false;

        for (TestType testType : izinliTestler) {
            if (testType.getId().equals(testSonucuRequest.testId())) {
                izinliMi = true;
            }
        }

        if (!izinliMi) {
            throw new IllegalArgumentException("Bu test bu laboratuvara ait değil.");
        }

        if(testResultRepository.findByRandevuIdAndTestId(testSonucuRequest.randevuId(), testSonucuRequest.testId()).isPresent()){
            throw new IllegalStateException("Bu testin sonucu halihazırda girilmiş.");
        }

        TestResult testResult = new TestResult(
                testSonucuRequest.randevuId(),
                testSonucuRequest.testId(),
                testSonucuRequest.sonuc(),
                jwt.getSubject(),
                LocalDateTime.now(),
                TestResultStatus.ISLENDI
        );

        testResultRepository.save(testResult);

        tumTestlerSonuclanmisMi(testResult.getRandevuId());

        return new TestSonucuResponse(testResult);
    }

    void tumTestlerSonuclanmisMi(Long randevuId){
        int sonuclananTestSayisi = testResultRepository.countByRandevuId(randevuId);
        appointmentClient.tumTestlerSonuclanmisMi(new TumTestlerSonuclanmisMiRequest(randevuId, sonuclananTestSayisi));
    }

    public List<TestSonucuResponse> doktorTestSonucuGoruntule(Long randevuId) {
        List<TestResult> testResults = testResultRepository.findAllByRandevuId(randevuId);

        if(testResults.isEmpty()){
            return List.of();
        }

        List<TestSonucuResponse> testSonucuResponses = new ArrayList<>();
        for(TestResult testResult : testResults){
            testSonucuResponses.add(new TestSonucuResponse(testResult));
        }
        return testSonucuResponses;
    }
}
