package com.douglas.oracao24h.periodWithTimes;

import java.time.LocalDate;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
public class Oracao24hRow {
  private String periodId;
  private String timeId;
  private String church;
  private LocalDate startDate;
  private LocalDate endDate;
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
