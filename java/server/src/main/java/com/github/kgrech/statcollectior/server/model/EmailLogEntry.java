package com.github.kgrech.statcollectior.server.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Representation of the log of sent e-mail
 * @author Konstantin G. (kgrech@mail.ru)
 */
@Document
public class EmailLogEntry {

    @Id
    private String id;
    @Indexed
    private String clientKey;
    @Indexed
    private String type;
    @Indexed(direction = IndexDirection.DESCENDING)
    private long timestamp;

    /**
     * Initializes object. Generates new id and timestamp for the record.
     */
    public EmailLogEntry() {
        this.id = new ObjectId().toString();
        this.timestamp = new Date().getTime();
    }

    /**
     * @return record id
     */
    public String getId() {
        return id;
    }

    /**
     * @return key of the client notification related to
     */
    public String getClientKey() {
        return clientKey;
    }

    /**
     * @return type of causing event
     */
    public String getType() {
        return type;
    }

    /**
     * @return timestamp of e-mail sent
     */
    public long getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
