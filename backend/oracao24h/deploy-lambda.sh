#!/bin/bash

### Configurações ###
FUNCTION_NAME="oracao24h"
ROLE_NAME="lambda-oracao24h-role"
ZIP_FILE="target/oracao24h-0.0.1-SNAPSHOT-aws.jar"
HANDLER="org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
RUNTIME="java17"
REGION="sa-east-1"
TABLE_NAME="Oracao24h"
REACT_APP_ORIGIN="*" # Substitua pelo domínio da sua aplicação React em produção, se necessário

# 1. Cria a role de execução com trust policy para Lambda
aws iam create-role --role-name $ROLE_NAME --assume-role-policy-document file://<(cat <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": { "Service": "lambda.amazonaws.com" },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
)

# 2. Anexa política gerenciada para logs e cria política personalizada para DynamoDB
aws iam attach-role-policy --role-name $ROLE_NAME \
  --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole

aws iam put-role-policy --role-name $ROLE_NAME --policy-name dynamodb-oracao24h-policy --policy-document file://<(cat <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "dynamodb:GetItem",
        "dynamodb:PutItem",
        "dynamodb:UpdateItem",
        "dynamodb:DeleteItem",
        "dynamodb:Query",
        "dynamodb:Scan"
      ],
      "Resource": "arn:aws:dynamodb:$REGION:*:table/$TABLE_NAME"
    }
  ]
}
EOF
)

# Espera a role propagar
echo "Aguardando role estar disponível..."
sleep 10

# 3. Cria a função Lambda
aws lambda create-function \
  --function-name $FUNCTION_NAME \
  --runtime $RUNTIME \
  --role arn:aws:iam::$(aws sts get-caller-identity --query Account --output text):role/$ROLE_NAME \
  --handler $HANDLER \
  --zip-file fileb://$ZIP_FILE \
  --timeout 30 \
  --memory-size 512 \
  --region $REGION

# 4. Cria a Function URL com configuração CORS
aws lambda create-function-url-config \
  --function-name $FUNCTION_NAME \
  --auth-type NONE \
  --cors "AllowOrigins=$REACT_APP_ORIGIN,AllowMethods=GET,POST,OPTIONS,AllowHeaders=Content-Type,Authorization" \
  --region $REGION

# 5. Verifica se a Function URL foi criada com sucesso e exibe a URL
URL=$(aws lambda get-function-url-config \
  --function-name $FUNCTION_NAME \
  --region $REGION \
  --query FunctionUrl \
  --output text 2>/dev/null)

if [ -z "$URL" ]; then
  echo "❌ Erro: Falha ao criar ou obter a Function URL."
  exit 1
fi

echo ""
echo "🚀 Lambda criada com sucesso!"
echo "🔗 URL da função: $URL"