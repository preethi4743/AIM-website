package com.aimTek.aimTek.Repository;

import com.aimTek.aimTek.model.EmailSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmailRepository extends MongoRepository<EmailSubscription, String> {
    boolean existsByEmail(String email);  // MongoDB query method to check if email exists

    Optional<EmailSubscription> findByEmail(String email);
 
}
