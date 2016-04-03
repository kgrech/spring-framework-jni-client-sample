package com.github.kgrech.statcollectior.server.repo;

import com.github.kgrech.statcollectior.server.model.EmailLogEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring data repository for querying database table of e-mail journal records.
 * Methods implementation are done automatically by spring data framework
 * @author Konstantin G. (kgrech@mail.ru)
 */
public interface EmailLogEntryRepository extends MongoRepository<EmailLogEntry, String> {

    /**
     * Find all records for the given customer about the events of the given time,
     * sent after the given timestamp
     * @param clientKey client key
     * @param type type of the causing event
     * @param timestamp timestamp of the earliest event to return
     * @return list of actions notifications sent
     */
    List<EmailLogEntry> findByClientKeyAndTypeAndTimestampGreaterThan(
            String clientKey,
            String type,
            Long timestamp);


}
