package br.com.douglas.oracao24h.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import br.com.douglas.oracao24h.model.row.DynamoRow;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Repository
@RequiredArgsConstructor
public class RowRepository {
    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<DynamoRow> table;

    @PostConstruct
    private void init() {
        this.table = enhancedClient.table("PrayerPeriods", TableSchema.fromBean(DynamoRow.class));
    }

    public List<DynamoRow> findByPeriodId(String periodId) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(periodId).build());

        return table //
                .query(r -> r.queryConditional(queryConditional)) //
                .stream().flatMap(page -> page.items().stream()) //
                .collect(Collectors.toList());
    }
}
