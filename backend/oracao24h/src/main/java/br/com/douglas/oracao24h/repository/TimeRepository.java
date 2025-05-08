package br.com.douglas.oracao24h.repository;

import org.springframework.stereotype.Repository;

import br.com.douglas.oracao24h.model.time.PrayerTime;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class TimeRepository {
    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<PrayerTime> table;

    @PostConstruct
    private void init() {
        this.table = enhancedClient.table("PrayerPeriods", TableSchema.fromBean(PrayerTime.class));
    }

    public PrayerTime save(PrayerTime time) {
        table.putItem(time);
        return time;
    }
}
