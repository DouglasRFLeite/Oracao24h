package br.com.douglas.oracao24h.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import br.com.douglas.oracao24h.model.period.CreatePeriodRequest;
import br.com.douglas.oracao24h.model.period.FetchPeriodResponse;
import br.com.douglas.oracao24h.model.period.PrayerPeriod;
import br.com.douglas.oracao24h.model.row.DynamoRow;
import br.com.douglas.oracao24h.repository.PeriodoRepository;
import br.com.douglas.oracao24h.repository.RowRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PeriodoService {
    private final PeriodoRepository periodRepository;
    private final TimeService timeService;
    private final RowRepository rowRepository;

    public PrayerPeriod createPeriodo(CreatePeriodRequest request) {
        PrayerPeriod periodo = new PrayerPeriod();
        periodo.setPeriodId(genPeriodIdPK());
        periodo.setTimeId("META");
        periodo.setChurch(request.nomeDaIgreja());
        periodo.setReason(request.motivosDeOracao());
        periodo.setDateStart(request.inicioDoPeriodo());
        periodo.setDateEnd(request.fimDoPeriodo());

        return periodRepository.save(periodo);
    }

    private String genPeriodIdPK() {
        long periodId = System.currentTimeMillis();
        String pk = "PERIOD#" + periodId;
        return pk;
    }

    public FetchPeriodResponse fetchPeriod(String periodId) {
        List<DynamoRow> rows = rowRepository.findByPeriodId(periodId);

        return new FetchPeriodResponse(parsePeriod(rows), timeService.parseTimes(rows));
    }

    private PrayerPeriod parsePeriod(List<DynamoRow> rows) throws NoSuchElementException {
        DynamoRow periodRow = rows //
                .stream() //
                .filter(row -> row.getTimeId().equals("META")) //
                .findFirst() //
                .orElseThrow();

        PrayerPeriod period = new PrayerPeriod();
        period.setPeriodId(periodRow.getPeriodId());
        period.setTimeId(periodRow.getTimeId());
        period.setChurch(periodRow.getChurch());
        period.setDateStart(periodRow.getDateStart());
        period.setDateEnd(periodRow.getDateEnd());
        period.setReason(periodRow.getReason());

        return period;
    }
}
