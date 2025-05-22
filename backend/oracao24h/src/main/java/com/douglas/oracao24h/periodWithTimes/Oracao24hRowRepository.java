package com.douglas.oracao24h.periodWithTimes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Repository
@RequiredArgsConstructor
public class Oracao24hRowRepository {
  private final DynamoDbEnhancedClient enhancedClient;
  private DynamoDbTable<Oracao24hRow> table;

  @PostConstruct
  private void init() {
    this.table = enhancedClient.table("Oracao24h", TableSchema.fromBean(Oracao24hRow.class));
  }

  List<Oracao24hRow> findByPeriodID(String periodId) {
    QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(periodId).build());

    return table //
        .query(r -> r.queryConditional(queryConditional)) //
        .stream().flatMap(page -> page.items().stream()) //
        .collect(Collectors.toList());
  }
}
