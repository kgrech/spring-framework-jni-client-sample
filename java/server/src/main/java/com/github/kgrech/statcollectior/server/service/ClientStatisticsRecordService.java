package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.exception.WrongFormatException;
import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;

import java.util.List;
import java.util.Set;

/**
 * Service interface to implement main logic of statistics message processin
 * @author Konstantin G. (kgrech@mail.ru)
 */
public interface ClientStatisticsRecordService {

    /**
     * Creates new record and saves it to the database.
     * We do not validate client key here, because it has passed authentication.
     * type and value are validated inside factory and record classes
     * @param clientKey client identification
     * @param type record type
     * @param value measured value
     * @return created record
     * @throws WrongFormatException
     */
    ClientStatisticsRecord newRecord(
            String clientKey,
            String type,
            Object value) throws WrongFormatException;

    /**
     * Returns list of client records. Supports pagination
     * @param clientKey  client identification
     * @param page id of the records page
     * @param pageSize size of the records page
     * @return page content
     */
    List<ClientStatisticsRecord> clientRecords(
            String clientKey,
            int page,
            int pageSize);

    /**
     * Returns list of client records of the given type. Supports pagination
     * @param clientKey  client identification
     * @param type  record type
     * @param page id of the records page
     * @param pageSize size of the records page
     * @return page content
     */
    List<ClientStatisticsRecord> clientRecordsByType(
            String clientKey,
            String type,
            int page,
            int pageSize);

    /**
     * Returns all valid types of record
     * @return valid types of record
     */
    Set<String> validTypes();
}
