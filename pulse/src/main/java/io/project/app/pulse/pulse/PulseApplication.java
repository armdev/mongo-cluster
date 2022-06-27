package io.project.app.pulse.pulse;

import java.rmi.UnknownHostException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan
@EnableMongoRepositories
@Slf4j
@EnableRetry
@EnableAsync
public class PulseApplication {

    @Autowired
    private PulseRepository pulseRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PostService postService;

    public static void main(String[] args) {
        SpringApplication.run(PulseApplication.class, args);

    }

   @EventListener(ApplicationReadyEvent.class)
    public void init() {
        PulseModel pulseModel = new PulseModel(System.currentTimeMillis());

       PaymentModel paymentModel = new PaymentModel(System.currentTimeMillis());
       /// PaymentModel paymentModel = new PaymentModel(1656327722530L);
        postService.performingTransactionaOperations(pulseModel, paymentModel);

        List<PulseModel> findAll = pulseRepository.findAll();

        for (PulseModel pm : findAll) {
            log.info("pulse " + pm.getPulse());
        }

        List<PaymentModel> payments = paymentRepository.findAll();
        for (PaymentModel pmd : payments) {
            log.info("payment " + pmd.getPayment());
        }
    }

}
