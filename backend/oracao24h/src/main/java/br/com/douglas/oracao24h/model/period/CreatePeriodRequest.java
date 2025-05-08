package br.com.douglas.oracao24h.model.period;

import java.time.LocalDate;

public record CreatePeriodRequest(String nomeDaIgreja, String motivosDeOracao, LocalDate inicioDoPeriodo,
                LocalDate fimDoPeriodo) {

}
