package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;
import org.springframework.context.ApplicationEvent;

/**
 * System event to notify about new record insertion
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class RecordUpdateEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RecordUpdateEvent(ClientStatisticsRecord source) {
        super(source);
    }

    /**
     * The object on which the Event initially occurred
     * @return The object on which the Event initially occurred.
     */
    @Override
    public ClientStatisticsRecord getSource() {
        return (ClientStatisticsRecord) super.getSource();
    }
}
