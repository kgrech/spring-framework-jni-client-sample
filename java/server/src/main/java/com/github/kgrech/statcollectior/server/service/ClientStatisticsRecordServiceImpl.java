package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.model.Alert;
import com.github.kgrech.statcollectior.server.model.Client;
import com.github.kgrech.statcollectior.server.exception.WrongFormatException;
import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;
import com.github.kgrech.statcollectior.server.repo.ClientStatisticsRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Service instance to implement main logic of statistics message processing
 * @author Konstantin G. (kgrech@mail.ru)
 */
@Service
public class ClientStatisticsRecordServiceImpl implements ClientStatisticsRecordService {

    @Autowired
    private ClientStatisticsRecordFactory factory;
    @Autowired
    private ClientStatisticsRecordRepository recordRepository;

    /**
     * Framework event publisher
     */
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    private List<Client> clients;

    /**
     * Method called by framework after bean construction.
     * Verifies the definition of all clients alerts
     */
    @Autowired
    public void setClients(List<Client> clients) throws WrongFormatException {
        for (Client client: clients) {
            for (Alert alert: client.getAlerts()) {
                if (!validTypes().contains(alert.getType())) {
                    throw new WrongFormatException("Invalid alert type: " +
                        alert.getType());
                }
            }
        }
        this.clients = clients;
    }

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
    @Override
    public ClientStatisticsRecord newRecord(
            String clientKey,
            String type,
            Object value) throws WrongFormatException {
        ClientStatisticsRecord record = factory.createRecord(type, value);
        record.setClientKey(clientKey);
        record = recordRepository.save(record);
        eventPublisher.publishEvent(new RecordUpdateEvent(record)); //Notify other components
        return record;
    }

    /**
     * Returns list of client records. Supports pagination
     * @param clientKey  client identification
     * @param page id of the records page
     * @param pageSize size of the records page
     * @return page content
     */
    @Override
    public List<ClientStatisticsRecord> clientRecords(
            String clientKey,
            int page,
            int pageSize) {
        Pageable pageRequest = new PageRequest(page, pageSize);
        return recordRepository.findByClientKey(clientKey, pageRequest).getContent();
    }

    /**
     * Returns list of client records of the given type. Supports pagination
     * @param clientKey  client identification
     * @param type  record type
     * @param page id of the records page
     * @param pageSize size of the records page
     * @return page content
     */
    @Override
    public List<ClientStatisticsRecord> clientRecordsByType(
            String clientKey,
            String type,
            int page,
            int pageSize) {
        Pageable pageRequest = new PageRequest(page, pageSize);
        return recordRepository.findByClientKeyAndType(clientKey, type, pageRequest).getContent();
    }

    /**
     * Returns all valid types of record
     * @return valid types of record
     */
    @Override
    public Set<String> validTypes() {
        return factory.validTypes();
    }
}
