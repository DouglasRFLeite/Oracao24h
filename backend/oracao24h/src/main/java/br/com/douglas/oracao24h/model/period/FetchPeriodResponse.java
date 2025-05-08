package br.com.douglas.oracao24h.model.period;

import java.util.List;

import br.com.douglas.oracao24h.model.time.PrayerTime;

public record FetchPeriodResponse(PrayerPeriod period, List<PrayerTime> times) {

}
