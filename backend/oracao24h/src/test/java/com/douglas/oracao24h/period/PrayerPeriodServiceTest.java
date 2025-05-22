package com.douglas.oracao24h.period;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PrayerPeriodServiceTest {

  @Mock
  private PrayerPeriodRepository repository;

  @InjectMocks
  private PrayerPeriodService service;

  @Test
  void testCreatePeriod_shouldSaveAndReturnModel() {
    // Arrange
    String church = "Igreja Exemplo";
    String reason = "Motivos de oração";
    LocalDate start = LocalDate.now();
    LocalDate end = start.plusDays(7);

    when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    PrayerPeriodModel saved = service.createPeriod(church, reason, start, end);

    assertEquals(church, saved.getChurch());
    assertEquals(reason, saved.getReason());
    assertEquals(start, saved.getStartDate());
    assertEquals(end, saved.getEndDate());
    assertNotNull(saved.getPeriodId());
    assertEquals("META", saved.getTimeId());
  }
}
