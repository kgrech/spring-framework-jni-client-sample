package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.exception.WrongFormatException;
import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;

import java.util.Set;

/**
 * Factory interface for producing records of the given type
 * @author Konstantin G. (kgrech@mail.ru)
 */
public interface ClientStatisticsRecordFactory {

    /**
     * Returns all valid types of record
     * @return valid types of record
     */
    Set<String> validTypes();

    /**
     * Creates new record
     * @param type record type
     * @param value measured value
     * @return new record
     * @throws WrongFormatException in case invalid record type of measured value
     * @throws RuntimeException in case of reflection api failure. Should never happen in case of case of
     * correct configuration. In case happen, client will recieve 500 error code
     */
    ClientStatisticsRecord createRecord(String type, Object value) throws WrongFormatException;
}
