package br.com.douglas.oracao24h.function;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import br.com.douglas.oracao24h.model.period.FetchPeriodRequest;
import br.com.douglas.oracao24h.model.period.FetchPeriodResponse;
import br.com.douglas.oracao24h.service.PeriodoService;
import lombok.RequiredArgsConstructor;

@Component
@Configuration
@RequiredArgsConstructor
public class FetchPeriodFunction {
    private final PeriodoService periodoService;

    @Bean
    public Function<FetchPeriodRequest, FetchPeriodResponse> fetchPeriod() {
        return request -> {
            FetchPeriodResponse response = periodoService.fetchPeriod(request.periodId());
            return response;
        };
    }
}
