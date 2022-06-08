package com.theagilemonkeys.crm.repository;

import com.theagilemonkeys.crm.entity.ConsumerPersistence;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface ConsumerDynamoDBRepository extends CrudRepository<ConsumerPersistence, String> {
}
