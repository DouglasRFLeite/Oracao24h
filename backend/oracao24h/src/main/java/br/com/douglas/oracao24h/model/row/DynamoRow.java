package br.com.douglas.oracao24h.model.row;

import java.time.LocalDate;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
public class DynamoRow {
    private String periodId;
    private String timeId;
    private String church;
    private LocalDate dateEnd;
    private LocalDate dateStart;
    private String name;
    private String reason;
    private String timeString;

    @DynamoDbPartitionKey
    public String getPeriodId() {
        return this.periodId;
    }

    @DynamoDbSortKey
    public String getTimeId() {
        return this.timeId;
    }
}
