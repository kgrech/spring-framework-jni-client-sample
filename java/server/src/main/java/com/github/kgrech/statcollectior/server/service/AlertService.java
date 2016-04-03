package com.github.kgrech.statcollectior.server.service;

/**
 * Interface of the  service to monitor events and send clients updates
 * @author Konstantin G. (kgrech@mail.ru)
 */
public interface AlertService {

    /**
     * Handles update event. Iterates over client alert list and sends message if required by schedule
     * @param event event about record update
     */
    void handleRecordUpdateEvent(RecordUpdateEvent event);
}
