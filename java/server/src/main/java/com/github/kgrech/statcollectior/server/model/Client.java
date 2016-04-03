package com.github.kgrech.statcollectior.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Client definition
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class Client {

    private final String key;
    private final String mail;
    private final String duration;
    private final List<Alert> alerts;

    /**
     * Creates client
     * @param key client key
     * @param mail client e-mail
     * @param duration duration definition
     */
    public Client(String key, String mail, String duration) {
        this.key = key;
        this.mail = mail;
        this.duration = duration;
        this.alerts = new ArrayList<>();
    }

    /**
     * Creates client by taking parent client information, but custom list
     * of alerts
     * @param client parent client
     * @param alerts alert list
     */
    public Client(Client client, List<Alert> alerts) {
        this.key = client.getKey();
        this.mail = client.getMail();
        this.duration = client.getDuration();
        this.alerts = alerts;
    }

    public String getKey() {
        return key;
    }

    public String getMail() {
        return mail;
    }

    public String getDuration() {
        return duration;
    }

    /**
     * Adds alert definition to the client
     * @param alert alert definition
     */
    public void addAlert(Alert alert) {
        this.alerts.add(alert);
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

}


