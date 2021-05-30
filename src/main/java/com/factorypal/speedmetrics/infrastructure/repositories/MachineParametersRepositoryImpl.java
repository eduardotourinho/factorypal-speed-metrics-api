package com.factorypal.speedmetrics.infrastructure.repositories;

import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.domain.repositories.MachineParametersRepository;
import com.mongodb.internal.operation.AggregateOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;


@Repository
@Slf4j
public class MachineParametersRepositoryImpl implements MachineParametersRepository {

    private final ReactiveMongoOperations mongoTemplate;

    public MachineParametersRepositoryImpl(ReactiveMongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Parameter> saveAll(Collection<Parameter> parameters) {
        var collectionName = mongoTemplate.getCollectionName(Parameter.class);
        log.info(String.format("Saving %d parameters in collection: %s", parameters.size(), collectionName));

        return mongoTemplate.insertAll(parameters);
    }

    /**
     * Return the newest parameters grouped by machine
     *
     * [
     *   {
     *     '$sort': {
     *       'createdAt': -1
     *     }
     *   }, {
     *     '$group': {
     *       '_id': {
     *         'machineKey': '$machineKey',
     *         'key': '$key'
     *       },
     *       'id': {
     *         '$first': '$_id'
     *       },
     *       'value': {
     *         '$first': '$value'
     *       },
     *       'createdAt': {
     *         '$first': '$createdAt'
     *       }
     *     }
     *   }, {
     *     '$project': {
     *       '_id': '$id',
     *       'machine': '$_id.machineKey',
     *       'key': '$_id.key',
     *       'value': '$value',
     *       'createdAt': '$createdAt'
     *     }
     *   }
     * ]
     *
     * @return Flux<Parameter>
     */
    @Override
    public Flux<Parameter> fetchLatestParameters() {
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC,"createdAt");
        GroupOperation groupBy = Aggregation.group("machineKey", "key")
                .first("_id").as("id")
                .first("value").as("value")
                .first("createdAt"). as("createdAt");
        ProjectionOperation newestParameters = Aggregation.project()
                .andInclude("value", "createdAt")
                .andExpression("_id.machineKey").as("machineKey")
                .andExpression("_id.key").as("key")
                .andExpression("id").as("_id");


        TypedAggregation<Parameter> aggregation = Aggregation.newAggregation(
                Parameter.class,
                sortOperation,
                groupBy,
                newestParameters
        );
        return mongoTemplate.aggregate(aggregation, Parameter.class);
    }
}
