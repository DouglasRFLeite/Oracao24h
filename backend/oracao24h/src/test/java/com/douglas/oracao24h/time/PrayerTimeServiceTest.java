package com.douglas.oracao24h.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PrayerTimeServiceTest {

  @Mock
  private PrayerTimeRepository repository;

  @InjectMocks
  private PrayerTimeService service;

  @Test
  void testCreate_shouldSaveAndReturnTime() {
    // Arrange
    String periodId = "PERIOD#123";
    String name = "João";
    String time = "10:00";

    when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    PrayerTimeModel result = service.create(periodId, name, time);

    // Assert
    assertEquals(periodId, result.getPeriodId());
    assertEquals(name, result.getName());
    assertEquals(time, result.getTimeString());
    assertEquals("SORT#10:10", result.getTimeId());
  }
}
