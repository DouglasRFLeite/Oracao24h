package com.douglas.oracao24h.periodWithTimes;

import java.util.List;

import com.douglas.oracao24h.period.PrayerPeriodModel;
import com.douglas.oracao24h.time.PrayerTimeModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PeriodWithTime {
  private PrayerPeriodModel prayerPeriod;
  private List<PrayerTimeModel> prayerTimeList;
}
