package com.douglas.oracao24h.time;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Configuration
@RequiredArgsConstructor
public class PrayerTimeFunctions {
  private final PrayerTimeService service;

  @Bean
  public Function<CreateTimeRequest, CreateTimeResponse> createTime() {
    return request -> {
      PrayerTimeModel prayerTimeModel = service.create(request.periodId(), request.name(), request.time());

      String msg = "Intervalo alocado com sucesso para: " + request.name();
      return new CreateTimeResponse(prayerTimeModel.getTimeId(), msg);
    };
  }
}

record CreateTimeRequest(String periodId, String time, String name) {
}

record CreateTimeResponse(String timeId, String message) {

}