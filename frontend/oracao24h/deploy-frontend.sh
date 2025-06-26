#!/bin/bash

# ==== Configurações ====
BUCKET_NAME="oracao24h-frontend-$(uuidgen | cut -d'-' -f1)"
REGION="sa-east-1"
BUILD_DIR="dist"

# ==== Etapa 0: build do React ====
echo "🛠️  Rodando build da aplicação React..."
npm run build

# ==== Etapa 1: cria o bucket ====
echo "📦 Criando bucket S3: $BUCKET_NAME"
aws s3 mb s3://$BUCKET_NAME --region $REGION

# ==== Etapa 2: habilita static site hosting ====
echo "🌐 Habilitando site estático no bucket..."
aws s3 website s3://$BUCKET_NAME/ --index-document index.html --error-document index.html

# ==== Etapa 2.5: desbloqueia políticas públicas ====
echo "🔓 Desbloqueando bloqueio de política pública..."
aws s3api put-public-access-block --bucket $BUCKET_NAME --public-access-block-configuration '{
  "BlockPublicAcls": false,
  "IgnorePublicAcls": false,
  "BlockPublicPolicy": false,
  "RestrictPublicBuckets": false
}'

# ==== Etapa 3: aplica política de leitura pública ====
echo "🔐 Aplicando política pública de leitura..."
aws s3api put-bucket-policy --bucket $BUCKET_NAME --policy "{
  \"Version\": \"2012-10-17\",
  \"Statement\": [
    {
      \"Sid\": \"PublicReadGetObject\",
      \"Effect\": \"Allow\",
      \"Principal\": \"*\",
      \"Action\": \"s3:GetObject\",
      \"Resource\": \"arn:aws:s3:::$BUCKET_NAME/*\"
    }
  ]
}"


# ==== Etapa 4: envia os arquivos da build ====
echo "🚀 Enviando arquivos para o S3..."
aws s3 sync $BUILD_DIR/ s3://$BUCKET_NAME/ --delete

# ==== Etapa 5: imprime a URL final ====
echo ""
echo "✅ Deploy concluído!"
echo "🌍 Acesse o site em:"
echo "http://$BUCKET_NAME.s3-website-$REGION.amazonaws.com"
