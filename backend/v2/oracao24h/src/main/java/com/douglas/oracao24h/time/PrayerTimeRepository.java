package com.douglas.oracao24h.time;

import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
class PrayerTimeRepository {
  private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
  private DynamoDbTable<PrayerTimeModel> table;

  @PostConstruct
  private void init() {
    this.table = dynamoDbEnhancedClient.table("Oracao24h", TableSchema.fromBean(PrayerTimeModel.class));
  }

  PrayerTimeModel save(PrayerTimeModel time) {
    table.putItem(time);
    return time;
  }
}
