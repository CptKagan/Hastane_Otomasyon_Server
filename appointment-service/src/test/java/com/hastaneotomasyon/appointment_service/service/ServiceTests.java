package com.hastaneotomasyon.appointment_service.service;

import com.hastaneotomasyon.appointment_service.dto.*;
import com.hastaneotomasyon.appointment_service.enums.RandevuDurum;
import com.hastaneotomasyon.appointment_service.model.Randevu;
import com.hastaneotomasyon.appointment_service.repository.RandevuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {
    @Mock
    private RandevuRepository randevuRepository;

    @InjectMocks
    private RandevuService randevuService;

    // ---------- TARİHKONTROL TEST BAŞLANGIÇ ----------
    @Test
    void tarihKontrol_allValid_ReturnsTrue(){
        // Arrange
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        var req = new RandevuTarihKontrolRequest("doktor-123", future, 1L);

        // Prerequests
        when(randevuRepository.existsByTercihEdilenTarihAndDoktorId(future, "doktor-123")).thenReturn(false);
        when(randevuRepository.existsByTercihEdilenTarihAndHastaId(future, 1L)).thenReturn(false);

        // Act
        boolean result = randevuService.tarihKontrol(req);

        // Assert
        assertTrue(result, "Tüm kontroller sağlandığında tarihKontrol TRUE dönmeli.");
    }

    @Test
    void tarihKontrol_whenDateInPast_ReturnsFalse(){
        // Arrange
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        var req = new RandevuTarihKontrolRequest("doktor-123", past, 1L);

        // Act
        boolean result = randevuService.tarihKontrol(req);

        // Assert
        assertFalse(result, "Geçmiş tarih için tarihKontrol FALSE dönmeli");
    }

    @Test
    void tarihKontrol_whenDoctorBusy_ReturnsFalse(){
        // Arrange
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        var req = new RandevuTarihKontrolRequest("doktor-123", future, 1L);

        // Prerequests
        when(randevuRepository.existsByTercihEdilenTarihAndDoktorId(future, "doktor-123")).thenReturn(false);
        when(randevuRepository.existsByTercihEdilenTarihAndHastaId(future, 1L)).thenReturn(true);

        // Act
        boolean result = randevuService.tarihKontrol(req);

        // Assert
        assertFalse(result, "Doktor müsait olmadığı için tarihKontrol FALSE dönmeli");
    }
    @Test
    void tarihKontrol_whenPatientBusy_ReturnsFalse(){
        // Arrange
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        var req = new RandevuTarihKontrolRequest("doktor-123", future, 1L);

        // Prerequests
        when(randevuRepository.existsByTercihEdilenTarihAndDoktorId(future, "doktor-123")).thenReturn(true);
        when(randevuRepository.existsByTercihEdilenTarihAndHastaId(future, 1L)).thenReturn(false);

        // Act
        boolean result = randevuService.tarihKontrol(req);

        // Arrange
        assertFalse(result, "Hasta müsait olmadığı için tarihKontrol FALSE dönmeli");
    }
    // ---------- TARİHKONTROL TEST BİTİŞ ----------


    // ---------- RANDEVUAL TEST BAŞLANGIÇ ----------
    @Test
    void randevuAl_shouldSaveAndReturnResponse(){
        // Arrange
        LocalDateTime expectedTime = LocalDateTime.now().plusDays(1);
        String doktorId = "doktor-123";
        Long hastaId = 1L;
        RandevuDurum expectedDurum = RandevuDurum.VAKTI_GELMEDI;
        RandevuRequest randevuRequest = new RandevuRequest(expectedTime, doktorId, hastaId);

        // Prerequests
        when(randevuRepository.save(any(Randevu.class))).thenAnswer(inv -> {
            Randevu randevu = inv.getArgument(0);
            randevu.setId(99L);
            randevu.setRandevuDurum(expectedDurum);
            return randevu;
        });

        // Act
        RandevuResponse randevuResponse = randevuService.randevuAl(randevuRequest);

        // then
        ArgumentCaptor<Randevu> cap = ArgumentCaptor.forClass(Randevu.class);
        verify(randevuRepository).save(cap.capture());
        Randevu savedRandevu = cap.getValue();

        // Arrange
        // Gönderilenler doğru mu?
        assertEquals(hastaId, savedRandevu.getHastaId());
        assertEquals(doktorId, savedRandevu.getDoktorId());
        assertEquals(expectedTime, savedRandevu.getTercihEdilenTarih());

        // Set edilen ID ve durum doğru mu?
        assertEquals(99L, savedRandevu.getId());
        assertEquals(expectedDurum, savedRandevu.getRandevuDurum());

        // Tüm alanlar eşleşiyor mu?
        assertEquals(99L, randevuResponse.randevuId());
        assertEquals(doktorId, randevuResponse.doktorId());
        assertEquals(expectedTime, randevuResponse.randevuZamani());
        assertEquals(hastaId, randevuResponse.hastaId());
        assertEquals(expectedDurum.toString(), randevuResponse.randevuDurumu());
        assertEquals(savedRandevu.getRandevuKodu(), randevuResponse.randevuKodu());

        // Null olmamalı
        assertNotNull(randevuResponse.randevuKodu());
    }
    // ---------- RANDEVUAL TEST BİTİŞ ----------


    // ---------- RANDEVUGORUNTULEKOD BAŞLANGIÇ ----------
    @Test
    void randevuGoruntuleKod_whenRandevuExists_thenReturnResponse(){
        // Arrange
        LocalDateTime expectedTime = LocalDateTime.now().plusDays(1);

        String kod = "ABC-123";
        Randevu randevu = new Randevu();
        randevu.setId(99L);
        randevu.setHastaId(1L);
        randevu.setDoktorId("doktor123");
        randevu.setTercihEdilenTarih(expectedTime);
        randevu.setRandevuKodu(kod);
        randevu.setRandevuDurum(RandevuDurum.VAKTI_GELMEDI);

        // Prerequests
        when(randevuRepository.findByRandevuKodu(kod)).thenReturn(Optional.of(randevu));

        // Act
        RandevuResponse randevuResponse = randevuService.randevuGoruntuleKod(kod);

        // Assert
        verify(randevuRepository).findByRandevuKodu(kod);

        assertEquals("doktor123", randevuResponse.doktorId());
        assertEquals(expectedTime, randevuResponse.randevuZamani());
        assertEquals(99L, randevuResponse.randevuId());
        assertEquals(kod, randevuResponse.randevuKodu());
        assertEquals(1L, randevuResponse.hastaId());
        assertEquals(RandevuDurum.VAKTI_GELMEDI.toString(), randevuResponse.randevuDurumu());
    }

    @Test
    void randevuGoruntuleKod_whenRandevuNotExists_thenThrow(){
        // Arrange
        String kod = "-";
        when(randevuRepository.findByRandevuKodu(kod)).thenReturn(Optional.empty());

        // Arrange
        assertThrows(NoSuchElementException.class, () -> randevuService.randevuGoruntuleKod(kod));
        verify(randevuRepository).findByRandevuKodu(kod);
    }
    // ---------- RANDEVUGORUNTULEKOD BİTİŞ ----------


    // ---------- GUNLUKRANDEVUGORUNTULE BAŞLANGIÇ ----------
    @Test
    void gunlukRandevuGoruntule_whenNoRandevu_thenReturnEmptyList(){
        // Arrange
        String doktorId = "doktor123";
        when(randevuRepository.findAllByDoktorIdAndTercihEdilenTarihBetween(
                eq(doktorId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(Collections.emptyList());

        // Act
        List<DoktorRandevuResponse> result = randevuService.gunlukRandevuGoruntule(doktorId);

        // Assert
        assertTrue(result.isEmpty());
        verify(randevuRepository).findAllByDoktorIdAndTercihEdilenTarihBetween(
                eq(doktorId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
    }

    @Test
    void gunlukRandevuGoruntule_whenRandevuExists_thenReturnResponseList(){
        // Arrange
        String doktorId = "doktor123";
        LocalDateTime expectedTime = LocalDateTime.now();
        Randevu randevu = new Randevu();
        randevu.setId(99L);
        randevu.setHastaId(1L);
        randevu.setDoktorId(doktorId);
        randevu.setTercihEdilenTarih(expectedTime);
        randevu.setRandevuKodu("kod");
        randevu.setRandevuDurum(RandevuDurum.VAKTI_GELMEDI);
        when(randevuRepository.findAllByDoktorIdAndTercihEdilenTarihBetween(
                eq(doktorId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(randevu));

        // Act
        List<DoktorRandevuResponse> randevuResponses = randevuService.gunlukRandevuGoruntule(doktorId);

        // Assert
        assertEquals(1, randevuResponses.size());
        DoktorRandevuResponse randevuResponse = randevuResponses.getFirst();
        assertEquals(doktorId, randevuResponse.doktorId());
        assertEquals(expectedTime, randevuResponse.tercihEdilenTarih());
        assertEquals(99L, randevuResponse.randevuId());
        assertEquals("kod", randevuResponse.randevuKodu());
        assertEquals(1L, randevuResponse.hastaId());
        assertEquals(RandevuDurum.VAKTI_GELMEDI.toString(), randevuResponse.randevuDurum());
    }
    // ---------- GUNLUKRANDEVUGORUNTULE BİTİŞ ----------


    // ---------- RANDEVUIPTAL BAŞLANGIÇ ----------
    @Test
    void randevuIptal_whenRandevuNotExists_thenThrows(){
        // Arrange
        Long randevuId = 99L;
        when(randevuRepository.findById(randevuId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> randevuService.randevuIptal(randevuId));

        verify(randevuRepository).findById(randevuId);
    }

    @Test
    void randevuIptal_whenRandevuExists_andWhenDateInPast_thenThrows(){
        // Arrange
        String doktorId = "doktor123";
        LocalDateTime expectedTime = LocalDateTime.now().minusDays(5);
        Randevu randevu = new Randevu();
        randevu.setId(99L);
        randevu.setHastaId(1L);
        randevu.setDoktorId(doktorId);
        randevu.setTercihEdilenTarih(expectedTime);
        randevu.setRandevuKodu("kod");
        randevu.setRandevuDurum(RandevuDurum.VAKTI_GELMEDI);

        when(randevuRepository.findById(99L)).thenReturn(Optional.of(randevu));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> randevuService.randevuIptal(99L));

        verify(randevuRepository).findById(99L);
    }

    @ParameterizedTest
    @ValueSource(strings = {"VAKTI_GELMEDI", "SURUYOR"})
    void randevuIptal_allValid_statusValid_thenSaves(String durum){
        // Arrange
        String doktorId = "doktor123";
        LocalDateTime expectedTime = LocalDateTime.now().plusDays(2);
        Randevu randevu = new Randevu();
        randevu.setId(99L);
        randevu.setHastaId(1L);
        randevu.setDoktorId(doktorId);
        randevu.setTercihEdilenTarih(expectedTime);
        randevu.setRandevuKodu("kod");
        randevu.setRandevuDurum(RandevuDurum.valueOf(durum));

        when(randevuRepository.findById(99L)).thenReturn(Optional.of(randevu));

        // Act
        randevuService.randevuIptal(99L);

        // Assert
        ArgumentCaptor<Randevu> cap = ArgumentCaptor.forClass(Randevu.class);
        verify(randevuRepository).save(cap.capture());

        Randevu saved = cap.getValue();
        assertEquals(RandevuDurum.IPTAL, saved.getRandevuDurum());
    }

    @ParameterizedTest
    @ValueSource(strings = {"IPTAL", "KAPANDI", "TAHLIL_BEKLENIYOR"})
    void randevuIptal_statusInvalid_thenThrows(String durum){
        // Arrange
        String doktorId = "doktor123";
        LocalDateTime expectedTime = LocalDateTime.now().plusDays(2);

        Randevu randevu = new Randevu();
        randevu.setId(99L);
        randevu.setHastaId(1L);
        randevu.setDoktorId(doktorId);
        randevu.setTercihEdilenTarih(expectedTime);
        randevu.setRandevuKodu("kod");
        randevu.setRandevuDurum(RandevuDurum.valueOf(durum));

        when(randevuRepository.findById(99L)).thenReturn(Optional.of(randevu));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> randevuService.randevuIptal(99L));

        verify(randevuRepository).findById(99L);
    }
    // ---------- RANDEVUIPTAL BİTİŞ ----------


    // ---------- RANDEVUBASLANGICIBILDIR BAŞLANGIÇ ----------
    @Test
    void randevuBaslangiciBildir_noRandevuExists_thenThrows(){
        // Arrange
        Long randevuId = 99L;

        when(randevuRepository.findById(randevuId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> randevuService.randevuBaslangiciBildir(randevuId, "doktor-123"));

        verify(randevuRepository).findById(99L);
    }

    @ParameterizedTest
    @ValueSource(strings = {"IPTAL", "KAPANDI", "TAHLIL_BEKLENIYOR", "SURUYOR"})
    void randevuBaslangiciBildir_randevuExists_andStatusInvalid_thenThrows(String durum){
        // Arrange
        // Arrange
        String doktorId = "doktor123";
        LocalDateTime expectedTime = LocalDateTime.now().plusDays(2);

        Randevu randevu = new Randevu();
        randevu.setId(99L);
        randevu.setHastaId(1L);
        randevu.setDoktorId(doktorId);
        randevu.setTercihEdilenTarih(expectedTime);
        randevu.setRandevuKodu("kod");
        randevu.setRandevuDurum(RandevuDurum.valueOf(durum));

        when(randevuRepository.findById(99L)).thenReturn(Optional.of(randevu));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> randevuService.randevuBaslangiciBildir(99L, doktorId));

        verify(randevuRepository).findById(99L);
    }

    @ParameterizedTest
    @ValueSource(strings = {"VAKTI_GELMEDI"})
    void randevuBaslangiciBildir_randevuExists_andStatusValid_thenSaves(String durum){
        // Arrange
        String doktorId = "doktor123";
        LocalDateTime expectedTime = LocalDateTime.now().plusDays(2);

        Randevu randevu = new Randevu();
        randevu.setId(99L);
        randevu.setHastaId(1L);
        randevu.setDoktorId(doktorId);
        randevu.setTercihEdilenTarih(expectedTime);
        randevu.setRandevuKodu("kod");
        randevu.setRandevuDurum(RandevuDurum.valueOf(durum));
        randevu.setPublicNote("denemenotupublic");

        when(randevuRepository.findById(99L)).thenReturn(Optional.of(randevu));

        // Act
        DoktorRandevuResponse doktorRandevuResponse = randevuService.randevuBaslangiciBildir(99L, doktorId);

        // Assert
        assertEquals(1L, doktorRandevuResponse.hastaId());
        assertEquals("doktor123", doktorRandevuResponse.doktorId());
        assertEquals(99L, doktorRandevuResponse.randevuId());
        assertEquals(expectedTime, doktorRandevuResponse.tercihEdilenTarih());
        assertEquals("kod", doktorRandevuResponse.randevuKodu());
        assertEquals(RandevuDurum.SURUYOR.toString(), doktorRandevuResponse.randevuDurum());
        assertEquals("denemenotupublic", doktorRandevuResponse.publicNote());


        verify(randevuRepository).save(randevu);
    }
    // ---------- RANDEVUBASLANGICIBILDIR BİTİŞ ----------


    // ---------- RANDEVUGORUNTULEID BAŞLANGIÇ ----------
    @Test
    void randevuGoruntuleId_noRandevuExists_thenThrows(){
        // Arrange
        Long randevuId = 99L;

        when(randevuRepository.findById(randevuId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> randevuService.randevuGoruntuleId(randevuId));

        verify(randevuRepository).findById(99L);
    }

    @Test
    void randevuGoruntuleId_randevuExists_thenReturns(){
        // Arrange
        String doktorId = "doktor123";
        LocalDateTime expectedTime = LocalDateTime.now().plusDays(2);

        Randevu randevu = new Randevu();
        randevu.setId(99L);
        randevu.setHastaId(1L);
        randevu.setDoktorId(doktorId);
        randevu.setTercihEdilenTarih(expectedTime);
        randevu.setRandevuKodu("kod");
        randevu.setRandevuDurum(RandevuDurum.VAKTI_GELMEDI);
        randevu.setPublicNote("denemenotupublic");

        when(randevuRepository.findById(99L)).thenReturn(Optional.of(randevu));

        // Act
        RandevuResponse randevuResponse = randevuService.randevuGoruntuleId(randevu.getId());

        // Assert
        assertEquals(1L, randevuResponse.hastaId());
        assertEquals("doktor123", randevuResponse.doktorId());
        assertEquals(99L, randevuResponse.randevuId());
        assertEquals(expectedTime, randevuResponse.randevuZamani());
        assertEquals("kod", randevuResponse.randevuKodu());
        assertEquals(RandevuDurum.VAKTI_GELMEDI.toString(), randevuResponse.randevuDurumu());


        verify(randevuRepository).findById(99L);
    }
    // ---------- RANDEVUGORUNTULEID BİTİŞ ----------


    // ---------- RANDEVUPUBLICNOTEKLEME BAŞLANGIÇ ----------
    @ParameterizedTest
    @ValueSource(strings = {"IPTAL", "KAPANDI", "VAKTI_GELMEDI"})
    void randevuPublicNotEkleme_statusInvalid_thenThrows(String durum){
        // Arrange
        String doktorId = "doktor123";
        LocalDateTime expectedTime = LocalDateTime.now().plusDays(2);

        NotRequest notRequest = new NotRequest("Bu bir deneme notudur.");

        Randevu randevu = new Randevu();
        randevu.setId(99L);
        randevu.setHastaId(1L);
        randevu.setDoktorId(doktorId);
        randevu.setTercihEdilenTarih(expectedTime);
        randevu.setRandevuKodu("kod");
        randevu.setRandevuDurum(RandevuDurum.valueOf(durum));

        when(randevuRepository.findById(99L)).thenReturn(Optional.of(randevu));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> randevuService.randevuPublicNotEkleme(randevu.getId(), notRequest, doktorId));
        verify(randevuRepository).findById(99L);
    }

    @ParameterizedTest
    @ValueSource(strings = {"SURUYOR", "TAHLIL_BEKLENIYOR"})
    void randevuPublicNotEkleme_statusValid_thenThrows(String durum){
        // Arrange
        String doktorId = "doktor123";
        LocalDateTime expectedTime = LocalDateTime.now().plusDays(2);

        NotRequest notRequest = new NotRequest("Bu bir deneme notudur.");

        Randevu randevu = new Randevu();
        randevu.setId(99L);
        randevu.setHastaId(1L);
        randevu.setDoktorId(doktorId);
        randevu.setTercihEdilenTarih(expectedTime);
        randevu.setRandevuKodu("kod");
        randevu.setRandevuDurum(RandevuDurum.valueOf(durum));

        when(randevuRepository.findById(99L)).thenReturn(Optional.of(randevu));

        // Act
        DoktorRandevuResponse doktorRandevuResponsePrivateAccess = randevuService.randevuPublicNotEkleme(randevu.getId(), notRequest, doktorId);

        // Assert
        assertEquals(1L, doktorRandevuResponsePrivateAccess.hastaId());
        assertEquals("doktor123", doktorRandevuResponsePrivateAccess.doktorId());
        assertEquals(99L, doktorRandevuResponsePrivateAccess.randevuId());
        assertEquals(expectedTime, doktorRandevuResponsePrivateAccess.tercihEdilenTarih());
        assertEquals("kod", doktorRandevuResponsePrivateAccess.randevuKodu());
        assertEquals(durum, doktorRandevuResponsePrivateAccess.randevuDurum());
        assertEquals("Bu bir deneme notudur.", doktorRandevuResponsePrivateAccess.publicNote());

        verify(randevuRepository).findById(99L);
        verify(randevuRepository).save(randevu);
    }
    // ---------- RANDEVUPUBLICNOTEKLEME BİTİŞ ----------
}
