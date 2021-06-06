package com.factorypal.speedmetrics.infrastructure.repositories;

import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.domain.repositories.MachineParametersRepository;
import com.factorypal.speedmetrics.domain.entities.MachineParameterStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;


@Repository
@Slf4j
public class MachineParametersRepositoryImpl implements MachineParametersRepository {

    private final ReactiveMongoOperations mongoTemplate;

    public MachineParametersRepositoryImpl(ReactiveMongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Long> count() {
        return mongoTemplate.count(new Query(), Parameter.class);
    }

    @Override
    public Flux<Parameter> saveAll(Collection<Parameter> parameters) {
        return mongoTemplate.insertAll(parameters).log(log.getName());
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
        return mongoTemplate.aggregate(aggregation, Parameter.class).log(log.getName());
    }

    /**
     * Calculates the metrics f
     * [
     *   {
     *     '$match': {
     *       'createdAt': {
     *         '$gte': new Date('Sun, 30 May 2021 00:00:00 GMT')
     *       }
     *     }
     *   }, {
     *     '$sort': {
     *       'machineKey': 1,
     *       'key': 1,
     *       'value': 1
     *     }
     *   }, {
     *     '$group': {
     *       '_id': {
     *         'machineKey': '$machineKey',
     *         'key': '$key'
     *       },
     *       'valueArray': {
     *         '$push': '$value'
     *       }
     *     }
     *   }, {
     *     '$project': {
     *       '_id': 0,
     *       'machineKey': '$_id.machineKey',
     *       'key': '$_id.key',
     *       'valueArray': 1,
     *       'size': {
     *         '$size': '$valueArray'
     *       }
     *     }
     *   }, {
     *     '$project': {
     *       'machineKey': 1,
     *       'key': 1,
     *       'valueArray': 1,
     *       'isEven': {
     *         '$eq': [
     *           {
     *             '$mod': [
     *               '$size', 2
     *             ]
     *           }, 0
     *         ]
     *       },
     *       'middlePoint': {
     *         '$trunc': {
     *           '$divide': [
     *             '$size', 2
     *           ]
     *         }
     *       }
     *     }
     *   }, {
     *     '$project': {
     *       'machineKey': 1,
     *       'key': 1,
     *       'valueArray': 1,
     *       'isEven': 1,
     *       'middlePoint': 1,
     *       'beginMiddle': {
     *         '$subtract': [
     *           '$middlePoint', 1
     *         ]
     *       },
     *       'endMiddle': '$middlePoint'
     *     }
     *   }, {
     *     '$project': {
     *       'machineKey': 1,
     *       'key': 1,
     *       'valueArray': 1,
     *       'isEven': 1,
     *       'middlePoint': 1,
     *       'beginValue': {
     *         '$arrayElemAt': [
     *           '$valueArray', '$beginMiddle'
     *         ]
     *       },
     *       'endValue': {
     *         '$arrayElemAt': [
     *           '$valueArray', '$endMiddle'
     *         ]
     *       }
     *     }
     *   }, {
     *     '$project': {
     *       'machineKey': 1,
     *       'key': 1,
     *       'valueArray': 1,
     *       'isEven': 1,
     *       'middlePoint': 1,
     *       'middleSum': {
     *         '$add': [
     *           '$beginValue', '$endValue'
     *         ]
     *       }
     *     }
     *   }, {
     *     '$project': {
     *       'machineKey': 1,
     *       'key': 1,
     *       'min': {
     *         '$arrayElemAt': [
     *           '$valueArray', 0
     *         ]
     *       },
     *       'max': {
     *         '$arrayElemAt': [
     *           '$valueArray', -1
     *         ]
     *       },
     *       'average': {
     *         '$avg': '$valueArray'
     *       },
     *       'median': {
     *         '$cond': {
     *           'if': '$isEven',
     *           'then': {
     *             '$divide': [
     *               '$middleSum', 2
     *             ]
     *           },
     *           'else': {
     *             '$arrayElemAt': [
     *               '$valueArray', '$middlePoint'
     *             ]
     *           }
     *         }
     *       }
     *     }
     *   }
     * ]
     *
     * @return Flux<MachineParameterStatistics>
     */
    @Override
    public Flux<MachineParameterStatistics> getMachineParametersStatistics(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes * -1);

        MatchOperation matchDocumentsByDate = Aggregation.match(Criteria.where("createdAt").gte(calendar.getTime()));
        SortOperation sortParametersValue = Aggregation.sort(Sort.Direction.ASC,"machineKey", "key", "value");

        GroupOperation groupParameterValues = Aggregation.group("machineKey", "key")
                .push("value").as("valueArray");

        ProjectionOperation  projectValuesSize = Aggregation.project("valueArray")
                .andExpression("_id.machineKey").as("machineKey")
                .andExpression("_id.key").as("key")
                .and(ArrayOperators.Size.lengthOfArray("valueArray")).as("size")
                .andExclude("_id");

        ProjectionOperation projectMiddlePoint = Aggregation.project("machineKey", "key", "valueArray")
                .and(AggregationExpression.from(MongoExpression.create("'$eq': [{'$mod': ['$size', 2]}, 0]")))
                    .as("isEven")
                .and(ArithmeticOperators.Trunc.truncValueOf(
                        ArithmeticOperators.Divide.valueOf("size").divideBy(2)
                )).as("middlePoint");

        ProjectionOperation projectMiddles = Aggregation.project("machineKey", "key", "valueArray", "middlePoint", "isEven")
                .and(ArithmeticOperators.Subtract.valueOf("middlePoint").subtract(1)).as("beginMiddle")
                .andExpression("middlePoint").as("endMiddle");

        ProjectionOperation projectMiddleValues = Aggregation.project("machineKey", "key", "valueArray", "middlePoint", "isEven")
                .and(ArrayOperators.ArrayElemAt.arrayOf("valueArray").elementAt("beginMiddle")).as("beginValue")
                .and(ArrayOperators.ArrayElemAt.arrayOf("valueArray").elementAt("endMiddle")).as("endValue");

        ProjectionOperation projectMiddleSum = Aggregation.project("machineKey", "key", "valueArray", "middlePoint", "isEven")
                .and(ArithmeticOperators.Add.valueOf("beginValue").add("endValue")).as("middleSum");

        MongoExpression medianCond = MongoExpression.create("'$cond': {" +
                "   if:'$isEven', " +
                "   then: {'$divide':['$middleSum', 2] }, " +
                "   else: {'$arrayElemAt':['$valueArray', '$middlePoint']}" +
                "}");
        ProjectionOperation projectFinalValues = Aggregation.project("machineKey")
                .andExpression("key").as("property")
                .and(ArrayOperators.ArrayElemAt.arrayOf("valueArray").elementAt(0)).as("min")
                .and(ArrayOperators.ArrayElemAt.arrayOf("valueArray").elementAt(-1)).as("max")
                .and(AccumulatorOperators.Avg.avgOf("valueArray")).as("average")
                .and(AggregationExpression.from(medianCond)).as("median");

        SortOperation sortByMachine = Aggregation.sort(Sort.Direction.ASC, "machineKey", "property");

        TypedAggregation<Parameter> statisticsAggregation = Aggregation.newAggregation(
                Parameter.class,
                matchDocumentsByDate,
                sortParametersValue,
                groupParameterValues,
                projectValuesSize,
                projectMiddlePoint,
                projectMiddles,
                projectMiddleValues,
                projectMiddleSum,
                projectFinalValues,
                sortByMachine
        );

        return mongoTemplate.aggregate(statisticsAggregation, MachineParameterStatistics.class)
                .log(log.getName());
    }
}
