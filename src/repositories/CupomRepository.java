package br.com.carloshenreis.gestcup.repositories;

import br.com.carloshenreis.gestcup.entities.CupomEntity;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableScan
@Repository
public interface CupomRepository extends DynamoDBCrudRepository<CupomEntity, String> {

    List<CupomEntity> findByCodEntReri(String codEntReri);

}
