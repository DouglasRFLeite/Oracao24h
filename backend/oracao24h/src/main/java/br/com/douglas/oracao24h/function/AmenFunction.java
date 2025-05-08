package br.com.douglas.oracao24h.function;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import br.com.douglas.oracao24h.model.amen.AmenRequest;
import br.com.douglas.oracao24h.model.amen.AmenResponse;

@Component
public class AmenFunction {

    @Bean
    public Function<AmenRequest, AmenResponse> amen() {
        return request -> {
            String msg = "Amen, " + request.name() + ", God bless you!";
            return new AmenResponse("OK", msg);
        };
    }

}