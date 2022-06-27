/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.project.app.pulse.pulse;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.rmi.UnknownHostException;
import static java.time.ZoneId.systemDefault;
import java.time.ZonedDateTime;
import static java.time.ZonedDateTime.ofInstant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;

/**
 *
 * @author armena
 */
@Configuration
@Slf4j
public class MongoConfig extends AbstractMongoClientConfiguration {

//
//    @Bean
//    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
//        return new MongoTransactionManager(dbFactory);
//    }
    @Bean(name = "mongoTransactionManager")
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory dbFactory) {
        TransactionOptions transactionOptions = TransactionOptions.builder().readConcern(ReadConcern.LOCAL).writeConcern(WriteConcern.W1).build();
        return new MongoTransactionManager(dbFactory, transactionOptions);

    }

    @Override
    protected String getDatabaseName() {
        return "pulseDB";
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    @Override
    public MongoClient mongoClient() {
        ///mongodb://masterx:masterx@mongo2:27017/pulseDB?replicaSet=dbrs&readPreference=secondaryPreferred&authSource=admin
        final ConnectionString connectionString = new ConnectionString("mongodb://masterx:masterx@mongo1:27017/pulseDB?replicaSet=dbrs&readPreference=primaryPreferred&authSource=admin");

        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .readConcern(ReadConcern.LOCAL)
                .applyConnectionString(connectionString)
                .writeConcern(WriteConcern.W1)
                .readPreference(ReadPreference.primary())
                .retryWrites(true)
                ///.serverSelectionTimeout(120000)

                //.maxWaitTime(120000)
                //.connectionsPerHost(10)
                //.connectTimeout(120000)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    @Override
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new DateToZonedDateTimeConverter());
        converters.add(new ZonedDateTimeToDateConverter());
        return new MongoCustomConversions(converters);
    }

    static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

        @Override
        public ZonedDateTime convert(Date source) {
            return !Optional.ofNullable(source).isPresent() ? null : ofInstant(source.toInstant(), systemDefault());
        }
    }

    static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

        @Override
        public Date convert(ZonedDateTime source) {
            return !Optional.ofNullable(source).isPresent() ? null : Date.from(source.toInstant());
        }
    }

}


///https://blog.clairvoyantsoft.com/mongodb-transaction-management-884f82f62767