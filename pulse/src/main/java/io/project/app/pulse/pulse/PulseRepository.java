package io.project.app.pulse.pulse;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PulseRepository extends MongoRepository<PulseModel, String> {
    
   
}
