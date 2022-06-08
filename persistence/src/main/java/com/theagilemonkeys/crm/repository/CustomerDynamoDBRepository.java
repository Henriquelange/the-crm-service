package com.theagilemonkeys.crm.repository;

import com.theagilemonkeys.crm.entity.CustomerPersistence;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface CustomerDynamoDBRepository extends CrudRepository<CustomerPersistence, String> {
}
