package com.douglas.oracao24h.time;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrayerTimeService {
  private final PrayerTimeRepository repository;

  public PrayerTimeModel create(String periodId, String name, String timeString) {
    PrayerTimeModel time = new PrayerTimeModel();
    time.setPeriodId(periodId);
    time.setName(name);
    time.setTimeString(timeString);
    time.setTimeId(genTimeId(timeString));

    return repository.save(time);
  }

  private String genTimeId(String time) {
    String sk = "SORT#" + time;
    return sk;
  }
}
