/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.project.app.pulse.pulse;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author armena
 */
@Service
@Slf4j
public class PostService {
    
    @Autowired
    private MongoTemplate mongoTemplate;    
    
    @Transactional(value = "mongoTransactionManager", propagation = Propagation.REQUIRED)
    @Retryable(value = {MongoCommandException.class, MongoException.class}, exclude = {MongoTransactionException.class, UncategorizedMongoDbException.class},
            backoff = @Backoff(delay = 10), maxAttempts = 10)
    public void performingTransactionaOperations(PulseModel document1, PaymentModel document2) {
        try {
            PulseModel insert = mongoTemplate.insert(document1);
            PaymentModel insert1 = mongoTemplate.insert(document2);            
            log.info("Transaction scope");            
        } catch (MongoTransactionException | UncategorizedMongoDbException ex) {
            MongoException mongoException = null;
            if (ex.getCause() instanceof MongoException) {
                mongoException = (MongoException) ex.getCause();
            } else if (ex.getCause() instanceof MongoCommandException) {
                mongoException = (MongoCommandException) ex.getCause();
            }
            if (mongoException != null && mongoException.hasErrorLabel(MongoException.TRANSIENT_TRANSACTION_ERROR_LABEL)) {
                log.error("TransientTransactionError aborting transaction and retrying ...");
                throw mongoException;
            } else if (mongoException != null && mongoException.hasErrorLabel(MongoException.UNKNOWN_TRANSACTION_COMMIT_RESULT_LABEL)) {
                log.debug("UnknownTransactionCommitResult, retrying commit operation ...");
                throw mongoException;
            }
            throw ex;
        }
    }
    
}
