package com.douglas.oracao24h.time;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
public class PrayerTimeModel {
  private String periodId;
  private String timeId;
  private String timeString;
  private String name;

  @DynamoDbPartitionKey
  public String getPeriodId() {
    return this.periodId;
  }

  @DynamoDbSortKey
  public String getTimeId() {
    return this.timeId;
  }
}
