package com.douglas.oracao24h.periodWithTimes;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.douglas.oracao24h.period.PrayerPeriodModel;
import com.douglas.oracao24h.time.PrayerTimeModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PeriodWithTimeService {
  private final Oracao24hRowRepository repository;

  public PeriodWithTime findPeriodWithTimeById(String periodId) throws NoSuchElementException {
    List<Oracao24hRow> rows = repository.findByPeriodID(periodId);

    Oracao24hRow periodRow = rows
        .stream()
        .filter(row -> row.getTimeId().equals("META"))
        .findFirst()
        .orElseThrow();

    PrayerPeriodModel prayerPeriod = new PrayerPeriodModel();
    prayerPeriod.setPeriodId(periodId);
    prayerPeriod.setTimeId(periodRow.getTimeId());
    prayerPeriod.setChurch(periodRow.getChurch());
    prayerPeriod.setStartDate(periodRow.getStartDate());
    prayerPeriod.setEndDate(periodRow.getEndDate());
    prayerPeriod.setReason(periodRow.getReason());

    List<PrayerTimeModel> prayerTimeList = rows
        .stream()
        .filter(row -> row.getTimeId().startsWith("SORT#"))
        .map(row -> {
          PrayerTimeModel prayerTimeModel = new PrayerTimeModel();
          prayerTimeModel.setPeriodId(periodId);
          prayerTimeModel.setTimeId(row.getTimeId());
          prayerTimeModel.setTimeString(row.getTimeString());
          prayerTimeModel.setName(row.getName());

          return prayerTimeModel;
        }).collect(Collectors.toList());

    return new PeriodWithTime(prayerPeriod, prayerTimeList);
  }
}
