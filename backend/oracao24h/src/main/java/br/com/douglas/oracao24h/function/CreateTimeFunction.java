package br.com.douglas.oracao24h.function;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import br.com.douglas.oracao24h.model.time.CreateTimeRequest;
import br.com.douglas.oracao24h.model.time.CreateTimeResponse;
import br.com.douglas.oracao24h.model.time.PrayerTime;
import br.com.douglas.oracao24h.service.TimeService;
import lombok.RequiredArgsConstructor;

@Component
@Configuration
@RequiredArgsConstructor
public class CreateTimeFunction {

    private final TimeService timeService;

    @Bean
    public Function<CreateTimeRequest, CreateTimeResponse> createTime() {
        return request -> {
            PrayerTime prayerTime = timeService.createTime(request);

            String msg = "Intervalo alocado com sucesso para: " + request.name();
            return new CreateTimeResponse(prayerTime.getTimeString(), msg);
        };
    }
}
