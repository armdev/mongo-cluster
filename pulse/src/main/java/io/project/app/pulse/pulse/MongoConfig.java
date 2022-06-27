/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.project.app.pulse.pulse;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

/**
 *
 * @author armena
 */
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration{

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Override
    protected String getDatabaseName() {
        return "pulseDB";
    }

    @Override
    public MongoClient mongoClient() {
        ///mongodb://masterx:masterx@mongo2:27017/pulseDB?replicaSet=dbrs&readPreference=secondaryPreferred&authSource=admin
        final ConnectionString connectionString = new ConnectionString("mongodb://masterx:masterx@mongo2:27017/pulseDB?replicaSet=dbrs&readPreference=primaryPreferred&authSource=admin");
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        return MongoClients.create(mongoClientSettings);
    }
}
