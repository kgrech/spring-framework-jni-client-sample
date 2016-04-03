package com.github.kgrech.statcollectior.server.model;

import com.github.kgrech.statcollectior.server.exception.WrongFormatException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Representation of all database records with client statistics information.
 * Uses swagger annotations to define online documentation
 * @author Konstantin G. (kgrech@mail.ru)
 */
@ApiModel(description = "Statistics object")
@Document
public abstract class ClientStatisticsRecord<T> {

    @Id
    @ApiModelProperty(value = "Id of the record", required = true, example = "559cce0c70726f3ee7000005")
    private String id;

    @Indexed
    @ApiModelProperty(value = "Client key", required = true, example = "test_client_1")
    private String clientKey;

    @ApiModelProperty(value = "Measured value", required = true)
    private T value;

    @Indexed
    @ApiModelProperty(value = "Type of alert", required = true, example = "memory")
    private String type;

    @Indexed(direction = IndexDirection.DESCENDING)
    @ApiModelProperty(value = "Record timestamp", required = true)
    private long timestamp;

    /**
     * Initializes object. Generates new id and timestamp for the record.
     */
    public ClientStatisticsRecord() {
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
     * @return client identification information
     */
    public String getClientKey() {
        return clientKey;
    }

    /**
     * @return value of measured parameter
     */
    public T getValue() {
        return value;
    }

    /**
     * @return type of record: e.g. memory, cpu and so on
     */
    public String getType() {
        return type;
    }

    /**
     * @return timestamp of the record creation
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

    /**
     * Sets measured value. Performs model validation of the type of data and try to convert it if possible
     * @param value measured value
     */
    public void setValue(Object value) {
        this.value = convert(value);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Validates and converts the value to the required type
     * @param value measured value
     * @return converted value
     * @throws WrongFormatException in case of conversion failed
     */
    protected abstract T convert(Object value) throws WrongFormatException;

    /**
     * Checks whether measured value is above given the limit
     * @param limit value limit
     * @return true if limit is exceeded
     */
    public abstract boolean aboveTheLimit(Float limit);


}

