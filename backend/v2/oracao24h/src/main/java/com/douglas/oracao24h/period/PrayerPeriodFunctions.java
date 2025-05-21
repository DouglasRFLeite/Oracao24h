package com.douglas.oracao24h.period;

import java.time.LocalDate;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Configuration
@RequiredArgsConstructor
public class PrayerPeriodFunctions {

  private final PrayerPeriodService service;

  @Bean
  public Function<CreatePeriodRequest, CreatePeriodResponse> createPeriod() {
    return request -> {
      PrayerPeriodModel createdPeriod = service.createPeriod(request.churchName(), request.prayerReasons(),
          request.startDate(), request.endDate());

      String msg = "Período criado com sucesso para a igreja: " + request.churchName();
      return new CreatePeriodResponse(createdPeriod.getPeriodId(), msg);
    };
  }
}

record CreatePeriodRequest(String churchName, String prayerReasons, LocalDate startDate,
    LocalDate endDate) {
}

record CreatePeriodResponse(String periodId, String message) {
}
