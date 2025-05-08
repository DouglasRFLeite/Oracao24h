package br.com.douglas.oracao24h.function;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import br.com.douglas.oracao24h.model.period.CreatePeriodRequest;
import br.com.douglas.oracao24h.model.period.CreatePeriodResponse;
import br.com.douglas.oracao24h.model.period.PrayerPeriod;
import br.com.douglas.oracao24h.service.PeriodoService;
import lombok.RequiredArgsConstructor;

@Component
@Configuration
@RequiredArgsConstructor
public class CreatePeriodFunction {

    private final PeriodoService periodoService;

    @Bean
    public Function<CreatePeriodRequest, CreatePeriodResponse> createPeriod() {
        return request -> {
            PrayerPeriod periodoCriado = periodoService.createPeriodo(request);

            String msg = "Período criado com sucesso para a igreja: " + request.nomeDaIgreja();
            return new CreatePeriodResponse(periodoCriado.getPeriodId(), msg);
        };
    }

}
