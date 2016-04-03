package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.exception.WrongFormatException;
import com.github.kgrech.statcollectior.server.model.Alert;
import com.github.kgrech.statcollectior.server.model.Client;
import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;
import com.github.kgrech.statcollectior.server.model.EmailLogEntry;
import com.github.kgrech.statcollectior.server.repo.EmailLogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the service to monitor events and send clients updates
 * @author Konstantin G. (kgrech@mail.ru)
 */
@Service
public class AlertServiceImpl implements AlertService {

    private final static String DURATION_IMMEDIATE = "immediate";

    private final Map<String, Client> clients = new HashMap<>();
    private final Map<String, Long> validDurations = new HashMap<>();

    @Autowired
    private EmailLogEntryRepository logEntryRepository;
    @Autowired
    private MailService mailService;

    /**
     * Creates instance. Defines possible message sending intervals
     */
    public AlertServiceImpl() {
        validDurations.put(DURATION_IMMEDIATE, 0L);
        validDurations.put("hourly", 60*60*1000L);
        validDurations.put("daily", 24*60*60*1000L);
    }

    /**
     * Inits the list of client, but removing disabled alerts and
     * clients without alerts enables
     * @param clients clients list
     */
    @Autowired
    public void setClients(List<Client> clients) {
        for (Client client: clients) {
            if (!validDurations.containsKey(client.getDuration())) {
                throw new WrongFormatException("Invalid duration: " + client.getDuration() +
                    " for client: " + client.getMail());
            }
            List<Alert> alerts = client.getAlerts().stream()
                    .filter(Alert::getEnabled)
                    .collect(Collectors.toList());
            Set<String> uniqueTypes = new HashSet<>();
            for (Alert alert: alerts) {
                if (uniqueTypes.contains(alert.getType()))
                    throw new WrongFormatException("Multiple alerts of the same type " +
                            alert.getType() + "are defined for client: " + client.getMail());
                uniqueTypes.add(alert.getType());
            }
            if (!alerts.isEmpty()) {
                this.clients.put(client.getKey(), new Client(client, alerts));
            }
        }
    }

    /**
     * Handles update event. Iterates over client alert list and sends message if required by schedule
     * @param event event about record update
     */
    @Override
    @Async //Separate thread pool will be assented for even handling and e-mail messaging
    @EventListener
    public void handleRecordUpdateEvent(RecordUpdateEvent event) {
        ClientStatisticsRecord record = event.getSource();
        String type = record.getType();
        Client client = clients.get(record.getClientKey());
        client.getAlerts().stream()
                .filter(alert -> type.equals(alert.getType()) && record.aboveTheLimit(alert.getLimit()))
                .forEach(alert -> notifyClientIfRequired(client, alert, record));
    }

    /**
     * Checks the journal of sent message and schedule.
     * Add new e-mail to journal and sends it if allowed by schedule
     * @param client client information
     * @param alert info about triggered alert
     * @param record statistics record with value over the limit
     */
    private void notifyClientIfRequired(Client client, Alert alert, ClientStatisticsRecord record) {
        if (DURATION_IMMEDIATE.equals(client.getDuration()) || alert.getRepeat()) {
            saveLogEntry(client.getKey(), alert.getType());
            mailService.sentMail(client, alert, record);
        } else {
            List<EmailLogEntry> entries;
            synchronized (this) { //because events are handled async
                entries = logEntryRepository.findByClientKeyAndTypeAndTimestampGreaterThan(client.getKey(),
                        alert.getType(), getScheduleTimestamp(client));
                saveLogEntry(client.getKey(), alert.getType());
            }
            if (entries.isEmpty()) { //If there were not mail send in given duration interval
                mailService.sentMail(client, alert, record);
            }
        }
    }

    /**
     * Creates and saves log entry
     * @param clientKey client code
     * @param type type information
     */
    private void saveLogEntry(String clientKey, String type) {
        EmailLogEntry logEntry = new EmailLogEntry();
        logEntry.setClientKey(clientKey);
        logEntry.setType(type);
        logEntryRepository.save(logEntry);
    }

    /**
     * Get the latest possible timestamp of the previous e-mail sent to client
     * @param client client information
     * @return latest possible e-mail timestamp
     */
    private Long getScheduleTimestamp(Client client) {
        return new Date().getTime() - validDurations.get(client.getDuration());
    }


}
