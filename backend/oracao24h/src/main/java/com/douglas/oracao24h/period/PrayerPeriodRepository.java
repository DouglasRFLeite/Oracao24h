package com.douglas.oracao24h.period;

import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
class PrayerPeriodRepository {
  private final DynamoDbEnhancedClient enhancedClient;
  private DynamoDbTable<PrayerPeriodModel> table;

  @PostConstruct
  private void init() {
    this.table = enhancedClient.table("Oracao24h", TableSchema.fromBean(PrayerPeriodModel.class));
  }

  PrayerPeriodModel save(PrayerPeriodModel periodo) {
    table.putItem(periodo);
    return periodo;
  }
}
