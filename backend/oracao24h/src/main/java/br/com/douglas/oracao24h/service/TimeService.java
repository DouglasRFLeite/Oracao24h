package br.com.douglas.oracao24h.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.douglas.oracao24h.model.row.DynamoRow;
import br.com.douglas.oracao24h.model.time.CreateTimeRequest;
import br.com.douglas.oracao24h.model.time.PrayerTime;
import br.com.douglas.oracao24h.repository.TimeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeService {
    private final TimeRepository timeRepository;

    public PrayerTime createTime(CreateTimeRequest request) {
        PrayerTime time = new PrayerTime();
        time.setPeriodId(request.periodId());
        time.setName(request.name());
        time.setTimeString(request.time());
        time.setTimeId(genTimeId(request.time()));

        return timeRepository.save(time);
    }

    private String genTimeId(String time) {
        String sk = "SORT#" + time;
        return sk;
    }

    public List<PrayerTime> parseTimes(List<DynamoRow> rows) {
        List<DynamoRow> timeRow = rows //
                .stream() //
                .filter(row -> row.getTimeId().startsWith("SORT#")) //
                .collect(Collectors.toList());

        List<PrayerTime> prayerTimes = timeRow.stream().map(row -> {
            PrayerTime time = new PrayerTime();
            time.setPeriodId(row.getPeriodId());
            time.setTimeId(row.getTimeId());
            time.setTimeString(row.getTimeString());
            time.setName(row.getName());

            return time;
        }).collect(Collectors.toList());

        return prayerTimes;
    }
}
