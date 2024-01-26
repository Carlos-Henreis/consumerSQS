package br.com.carloshenreis.gestcup.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "cupo_desc")
public class CupomEntity {

    @DynamoDBHashKey(attributeName = "cod_cupo_desc_coml")
    private String codCupoDescComl;

    @DynamoDBAttribute(attributeName = "cod_cupo_desc")
    private String codCupoDesc;

    @DynamoDBAttribute(attributeName = "period_cupo")
    private Integer periodCupo;

    @DynamoDBAttribute(attributeName = "dat_inc")
    private String datInc;

    @DynamoDBAttribute(attributeName = "restricao")
    private String restricao;

    @DynamoDBHashKey(attributeName = "cod_ent_reri")  // Adicionando índice GSI
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "cod_ent_reri_index")
    private String codEntReri;

    @DynamoDBAttribute(attributeName = "quantidade_maxima")
    private Integer quantidadeMaxima;

    @DynamoDBAttribute(attributeName = "quantidade_utilizada")
    private Integer quantidadeUtilizada;

    @DynamoDBAttribute(attributeName = "listavel")
    private String listavel;

}
