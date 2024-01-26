#!/bin/bash

items=(
'{
    "cod_cupo_desc_coml": {"S": "MIT20"},
    "cod_cupo_desc": {"S": "DESC20"},
    "period_cupo": {"N": "2"},
    "dat_inc": {"S": "2024-01-22"},
    "restricao": {"S": "11111"},
    "cod_ent_reri": {"S": "F5"},
    "quantidade_maxima": {"N": "10"},
    "quantidade_utilizada": {"N": "0"},
    "listavel": {"S": "true"},
    "hist_cupom": {"S": "ATUAL#MIT20#2024-01-22"}
}'
'{
    "cod_cupo_desc_coml": {"S": "MIT10"},
    "cod_cupo_desc": {"S": "DESC10"},
    "period_cupo": {"N": "30"},
    "dat_inc": {"S": "2024-01-25"},
    "restricao": {"S": "11111"},
    "cod_ent_reri": {"S": "F5"},
    "quantidade_maxima": {"N": "10"},
    "quantidade_utilizada": {"N": "0"},
    "listavel": {"S": "true"},
    "hist_cupom": {"S": "ATUAL#MIT10#2024-01-25"}
}'
'{
    "cod_cupo_desc_coml": {"S": "FIAT10"},
    "cod_cupo_desc": {"S": "DESC10"},
    "period_cupo": {"N": "40"},
    "dat_inc": {"S": "2024-01-25"},
    "restricao": {"S": "22222"},
    "cod_ent_reri": {"S": "F5"},
    "quantidade_maxima": {"N": "10"},
    "quantidade_utilizada": {"N": "0"},
    "listavel": {"S": "true"},
    "hist_cupom": {"S": "ATUAL#FIAT10#2024-01-25"}
}'
'{
    "cod_cupo_desc_coml": {"S": "FIAT30"},
    "cod_cupo_desc": {"S": "DESC30"},
    "period_cupo": {"N": "40"},
    "dat_inc": {"S": "2024-01-25"},
    "restricao": {"S": "22222"},
    "cod_ent_reri": {"S": "F5"},
    "quantidade_maxima": {"N": "10"},
    "quantidade_utilizada": {"N": "0"},
    "listavel": {"S": "false"},
    "hist_cupom": {"S": "ATUAL#FIAT30#2024-01-25"}
}'
)

for item in "${items[@]}"
do
    aws dynamodb put-item \
        --endpoint-url http://localhost:4566 \
        --table-name cupo_desc \
        --item "$item"
done