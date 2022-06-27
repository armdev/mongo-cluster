package io.project.app.pulse.pulse;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan
@EnableMongoRepositories
@Slf4j
public class PulseApplication {

    @Autowired
    private PulseRepository pulseRepository;

    public static void main(String[] args) {
        SpringApplication.run(PulseApplication.class, args);

    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        PulseModel pulseModel = new PulseModel(System.currentTimeMillis());
        pulseRepository.save(pulseModel);
        List<PulseModel> findAll = pulseRepository.findAll();
        for (PulseModel pm : findAll) {
            log.info("pulse " + pm.getPulse());
        }
    }

}
