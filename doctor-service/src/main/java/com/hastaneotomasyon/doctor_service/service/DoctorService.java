package com.hastaneotomasyon.doctor_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastaneotomasyon.doctor_service.client.AppointmentClient;
import com.hastaneotomasyon.doctor_service.client.LabClient;
import com.hastaneotomasyon.doctor_service.client.PatientClient;
import com.hastaneotomasyon.doctor_service.dto.*;
import com.hastaneotomasyon.doctor_service.enums.TestType;
import com.hastaneotomasyon.doctor_service.model.Doktor;
import com.hastaneotomasyon.doctor_service.repository.DoctorRepository;
import com.hastaneotomasyon.doctor_service.util.AllowedTestsByBranch;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final KeyCloakClientService keyCloakClientService;
    private final DoctorRepository doctorRepository;
    private final AppointmentClient appointmentClient;
    private final PatientClient patientClient;
    private final LabClient labClient;

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
                    && !rol.equals("doktor")
                    && !rol.equals("default-roles-hastane-otomasyon")
                    && !rol.equals("offline_access")
                    && !rol.equals("uma_authorization")) {
                return rol;
            }
        }
        throw new IllegalArgumentException("Branş rolü bulunamadı.");
    }

    public void fetchAllDoctors() {
        List<DoctorResponse> doctors = keyCloakClientService.fetchAllDoctors();
        for (DoctorResponse doctor : doctors) {
            Doktor doktor = new Doktor(doctor);
            System.out.println(doktor);
            doctorRepository.save(doktor);
        }
    }

    public DoctorRequestHastaService getDoctorById(String doktorId) {
        Optional<Doktor> doctorOpt = doctorRepository.findById(doktorId);
        if (doctorOpt.isEmpty()) {
            throw new NoSuchElementException("Doktor bulunamadı!");
        }
        Doktor doktor = doctorOpt.get();
        return new DoctorRequestHastaService(doktor.getFirstName(), doktor.getLastName());
    }

    public DoctorResponse getDoctorEntity(Jwt jwt) {
        Optional<Doktor> doktorOpt = doctorRepository.findById(jwt.getSubject());
        if (doktorOpt.isEmpty()) {
            throw new NoSuchElementException("Doktor bulunamadı");
        }
        Doktor doktor = doktorOpt.get();
        return new DoctorResponse(doktor.getId(),
                "aasdasd",
                doktor.getFirstName(),
                doktor.getLastName(),
                doktor.getEmail(),
                doktor.getDepartment());
    }

    public List<RandevuResponseHastaIsimSoyisim> gunlukRandevuGoruntule(Jwt jwt) {
        List<RandevuResponseHastaId> randevularHastaId = appointmentClient.gunlukRandevuGoruntule(jwt.getSubject());
        if (randevularHastaId.isEmpty()) {
            return List.of();
        }
        List<RandevuResponseHastaIsimSoyisim> randevularHastaIsim = new ArrayList<>();

        for (RandevuResponseHastaId randevuHastaId : randevularHastaId) {
            randevularHastaIsim.add(new RandevuResponseHastaIsimSoyisim(
                    randevuHastaId,
                    null,
                    patientClient.hastaIsimSoyisimAl(randevuHastaId.hastaId()))
            );
        }

        return randevularHastaIsim;
    }

    public RandevuResponseHastaIsimSoyisim muayeneBaslangici(Jwt jwt, MuayeneBaslangiciRequest muayeneBaslangiciRequest) throws JsonProcessingException {
        RandevuResponseHastaId randevuResponseHastaId = appointmentClient.randevuBaslangiciBildir(muayeneBaslangiciRequest.randevuId(), jwt.getSubject());
        String isimSoyisim = patientClient.hastaIsimSoyisimAl(randevuResponseHastaId.hastaId());

        return new RandevuResponseHastaIsimSoyisim(
                randevuResponseHastaId,
                null,
                isimSoyisim
        );
    }


    public RandevuResponseHastaIsimSoyisim randevuyaPublicNotEkleOrGuncelle(Jwt jwt, Long randevuId, @Valid NotRequest notRequest) {
        RandevuResponseHastaId randevuResponseHastaId = appointmentClient.randevuyaPublicNotEkleOrGuncelle(notRequest, randevuId, jwt.getSubject());
        String isimSoyisim = patientClient.hastaIsimSoyisimAl(randevuResponseHastaId.hastaId());
        String bearerToken = "Bearer " + jwt.getTokenValue();
        List<TestSonucuResponse> testSonucuResponses = labClient.doktorTestSonucuGoruntule(bearerToken, randevuId);
        return new RandevuResponseHastaIsimSoyisim(
                randevuResponseHastaId,
                testSonucuResponses,
                isimSoyisim
        );
    }

    public RandevuResponseHastaIsimSoyisim randevuyaPrivateNotEkleOrGuncelle(Jwt jwt, Long randevuId, @Valid NotRequest notRequest) {
        RandevuResponseHastaId randevuResponseHastaId = appointmentClient.randevuyaPrivateNotEkleOrGuncelle(notRequest, randevuId, jwt.getSubject());
        String isimSoyisim = patientClient.hastaIsimSoyisimAl(randevuResponseHastaId.hastaId());
        String bearerToken = "Bearer " + jwt.getTokenValue();
        List<TestSonucuResponse> testSonucuResponses = labClient.doktorTestSonucuGoruntule(bearerToken, randevuId);
        return new RandevuResponseHastaIsimSoyisim(
                randevuResponseHastaId,
                testSonucuResponses,
                isimSoyisim
        );
    }

    public RandevuResponseHastaIsimSoyisim randevuGoruntuleKod(Jwt jwt, String randevuKodu) {
        RandevuResponseHastaId randevuResponseHastaId = appointmentClient.randevuGoruntuleKod(randevuKodu, jwt.getSubject());
        String isimSoyisim = patientClient.hastaIsimSoyisimAl(randevuResponseHastaId.hastaId());
        String bearerToken = "Bearer " + jwt.getTokenValue();
        List<TestSonucuResponse> testSonucuResponses = labClient.doktorTestSonucuGoruntule(bearerToken, randevuResponseHastaId.randevuId());
        return new RandevuResponseHastaIsimSoyisim(
                randevuResponseHastaId,
                testSonucuResponses,
                isimSoyisim
        );
    }

    public RandevuResponseHastaIsimSoyisim randevuGoruntuleId(Jwt jwt, Long randevuId) {
        RandevuResponseHastaId randevuResponseHastaId = appointmentClient.randevuGoruntuleId(randevuId, jwt.getSubject());
        String isimSoyisim = patientClient.hastaIsimSoyisimAl(randevuResponseHastaId.hastaId());
        String bearerToken = "Bearer " + jwt.getTokenValue();
        List<TestSonucuResponse> testSonucuResponses = labClient.doktorTestSonucuGoruntule(bearerToken, randevuId);
        return new RandevuResponseHastaIsimSoyisim(
                randevuResponseHastaId,
                testSonucuResponses,
                isimSoyisim
        );
    }

    public RandevuResponseHastaIsimSoyisim randevuTestEkleme(Jwt jwt, Long randevuId, TestRequest testRequest) {
        List<String> roller = jwtRolleriAl(jwt);
        String brans = kullaniciBransiniAl(roller);

        List<TestType> izinliTestler = AllowedTestsByBranch.TESTS_ALLOWED_BY_BRANCH.get(brans);

        if (izinliTestler == null) {
            throw new IllegalArgumentException("Branşınıza ait test bulunamadı.");
        }

        for (Long testId : testRequest.testIdList()) {
            TestType test = TestType.fromId(testId);
            if (!izinliTestler.contains(test)) {
                throw new IllegalArgumentException("Bu test branşınıza ait değil: " + test);
            }
        }

        RandevuResponseHastaId randevuResponseHastaId = appointmentClient.randevuTestEkleme(randevuId, testRequest, jwt.getSubject());
        String isimSoyisim = patientClient.hastaIsimSoyisimAl(randevuResponseHastaId.hastaId());
        String bearerToken = "Bearer " + jwt.getTokenValue();
        List<TestSonucuResponse> testSonucuResponses = labClient.doktorTestSonucuGoruntule(bearerToken, randevuId);
        return new RandevuResponseHastaIsimSoyisim(
                randevuResponseHastaId,
                testSonucuResponses,
                isimSoyisim
        );
    }

    public RandevuResponseHastaIsimSoyisim randevuTeshisVeReceteEkleme(Jwt jwt, Long randevuId, RandevuTeshis randevuTeshis) {
        RandevuResponseHastaId randevuResponseHastaId = appointmentClient.randevuTeshisVeReceteEkleme(jwt.getSubject(), randevuId, randevuTeshis);
        String isimSoyisim = patientClient.hastaIsimSoyisimAl(randevuResponseHastaId.hastaId());
        String bearerToken = "Bearer " + jwt.getTokenValue();
        List<TestSonucuResponse> testSonucuResponses = labClient.doktorTestSonucuGoruntule(bearerToken, randevuId);
        return new RandevuResponseHastaIsimSoyisim(randevuResponseHastaId,
                testSonucuResponses,
                isimSoyisim);
    }
}