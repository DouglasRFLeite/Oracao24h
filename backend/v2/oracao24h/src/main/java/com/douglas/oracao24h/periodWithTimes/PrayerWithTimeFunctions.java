package com.douglas.oracao24h.periodWithTimes;

import java.util.List;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.douglas.oracao24h.period.PrayerPeriodModel;
import com.douglas.oracao24h.time.PrayerTimeModel;

import lombok.RequiredArgsConstructor;

@Component
@Configuration
@RequiredArgsConstructor
public class PrayerWithTimeFunctions {
  private final PeriodWithTimeService service;

  @Bean
  public Function<FetchPeriodRequest, FetchPeriodResponse> fetchPeriod() {
    return request -> {
      PeriodWithTime periodWithTime = service.findPeriodWithTimeById(request.periodId());
      return new FetchPeriodResponse(periodWithTime.getPrayerPeriod(), periodWithTime.getPrayerTimeList());
    };
  }
}

record FetchPeriodRequest(String periodId) {
}

record FetchPeriodResponse(PrayerPeriodModel period, List<PrayerTimeModel> times) {
}