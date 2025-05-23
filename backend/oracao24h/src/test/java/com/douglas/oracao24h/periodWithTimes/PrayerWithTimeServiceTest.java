package com.douglas.oracao24h.periodWithTimes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PeriodWithTimeServiceTest {

  @Mock
  private Oracao24hRowRepository repository;

  @InjectMocks
  private PeriodWithTimeService service;

  @Test
  void testFindPeriodWithTimeById_shouldReturnPeriodWithTimes() {
    // Arrange
    String periodId = "PERIOD#123";
    Oracao24hRow metaRow = new Oracao24hRow();
    metaRow.setPeriodId(periodId);
    metaRow.setTimeId("META");
    metaRow.setChurch("Igreja");
    metaRow.setReason("Motivo");
    metaRow.setStartDate(LocalDate.now());
    metaRow.setEndDate(LocalDate.now().plusDays(1));

    Oracao24hRow timeRow = new Oracao24hRow();
    timeRow.setPeriodId(periodId);
    timeRow.setTimeId("SORT#10:00");
    timeRow.setName("Maria");
    timeRow.setTimeString("10:00");

    when(repository.findByPeriodID(periodId)).thenReturn(List.of(metaRow, timeRow));

    // Act
    PeriodWithTime result = service.findPeriodWithTimeById(periodId);

    // Assert
    assertEquals("Igrej", result.getPrayerPeriod().getChurch());
    assertEquals(1, result.getPrayerTimeList().size());
    assertEquals("Maria", result.getPrayerTimeList().get(0).getName());
  }

  @Test
  void testFindPeriodWithTimeById_shouldThrowWhenNoMeta() {
    // Arrange
    String periodId = "PERIOD#999";
    when(repository.findByPeriodID(periodId)).thenReturn(List.of());

    // Assert
    assertThrows(NoSuchElementException.class, () -> {
      service.findPeriodWithTimeById(periodId);
    });
  }
}
