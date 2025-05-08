package br.com.douglas.oracao24h.repository;

import org.springframework.stereotype.Repository;

import br.com.douglas.oracao24h.model.period.PrayerPeriod;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class PeriodoRepository {
    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<PrayerPeriod> table;

    @PostConstruct
    private void init() {
        this.table = enhancedClient.table("PrayerPeriods", TableSchema.fromBean(PrayerPeriod.class));
    }

    public PrayerPeriod save(PrayerPeriod periodo) {
        table.putItem(periodo);
        return periodo;
    }
}
