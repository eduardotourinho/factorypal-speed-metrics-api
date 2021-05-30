package com.factorypal.speedmetrics.domain.repositories;

import com.factorypal.speedmetrics.domain.entities.Machine;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends ReactiveMongoRepository<Machine, String> {
}
