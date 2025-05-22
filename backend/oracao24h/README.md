## AWS DynamoDB Table Creation

```
aws dynamodb create-table \
  --table-name Oracao24h \
  --attribute-definitions \
    AttributeName=periodId,AttributeType=S \
    AttributeName=timeId,AttributeType=S \
  --key-schema \
    AttributeName=periodId,KeyType=HASH \
    AttributeName=timeId,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST \
  --region sa-east-1
```

## AWS IAM Role Creation

## AWS Lambda Creation

```
aws lambda create-function \
  --function-name oracao24h \
  --runtime java17 \
  --handler org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest \
  --memory-size 512 \
  --timeout 30 \
  --zip-file fileb://target/oracao24h-0.0.1-SNAPSHOT-aws.jar \
  --role arn:aws:iam::851725254651:role/lambda-execution-role

```
