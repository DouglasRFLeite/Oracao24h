package com.douglas.oracao24h.period;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrayerPeriodService {
  private final PrayerPeriodRepository repository;

  public PrayerPeriodModel createPeriod(String churchName, String prayerReason, LocalDate startDate,
      LocalDate endDate) {
    PrayerPeriodModel period = new PrayerPeriodModel();
    period.setPeriodId(genPeriodIdPK());
    period.setTimeId("META");
    period.setChurch(churchName);
    period.setReason(prayerReason);
    period.setStartDate(startDate);
    period.setEndDate(endDate);

    return repository.save(period);
  }

  private String genPeriodIdPK() {
    long periodId = System.currentTimeMillis();
    String pk = "PERIOD#" + periodId;
    return pk;
  }
}
