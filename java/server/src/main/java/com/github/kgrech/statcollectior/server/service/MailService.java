package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.model.Alert;
import com.github.kgrech.statcollectior.server.model.Client;
import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;

/**
 * Interface for sending e-mail notification to clients
 * @author Konstantin G. (kgrech@mail.ru)
 */
public interface MailService {

    /**
     * Sends e-mail to the client about alert
     * @param client client information
     * @param alert info about triggered alert
     * @param record statistics record with value over the limit
     */
    void sentMail(Client client, Alert alert, ClientStatisticsRecord record);
}
