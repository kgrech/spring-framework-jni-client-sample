package com.github.kgrech.statcollectior.server.repo;

import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring data repository for querying database table of statistics records.
 * Methods implementation are done automatically by spring data framework
 * @author Konstantin G. (kgrech@mail.ru)
 */
public interface ClientStatisticsRecordRepository extends MongoRepository<ClientStatisticsRecord, String> {

    /**
     * Finds all clients records. Supports sorting and pagination
     * @param clientKey client identification information
     * @param pageable page and sort definition
     * @return results page
     */
    Page<ClientStatisticsRecord> findByClientKey(String clientKey, Pageable pageable);


    /**
     * Finds all clients records. Supports sorting and pagination
     * @param clientKey client identification information
     * @param pageable page and sort definition
     * @return results page
     */
    Page<ClientStatisticsRecord> findByClientKeyAndType(String clientKey, String type, Pageable pageable);
}
