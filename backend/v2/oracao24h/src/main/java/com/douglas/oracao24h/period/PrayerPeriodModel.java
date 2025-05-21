package com.douglas.oracao24h.period;

import java.time.LocalDate;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
public class PrayerPeriodModel {
  private String periodId;
  private String timeId;
  private String church;
  private LocalDate startDate;
  private LocalDate endDate;
  private String reason;

  @DynamoDbPartitionKey
  public String getPeriodId() {
    return this.periodId;
  }

  @DynamoDbSortKey
  public String getTimeId() {
    return this.timeId;
  }
}