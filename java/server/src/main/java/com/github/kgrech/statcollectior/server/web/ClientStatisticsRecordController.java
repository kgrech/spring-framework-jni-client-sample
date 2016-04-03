package com.github.kgrech.statcollectior.server.web;

import com.github.kgrech.statcollectior.server.exception.WrongFormatException;
import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

/**
 * Rest controller interface for records update
 * @author Konstantin G. (kgrech@mail.ru)
 */
@RequestMapping(ClientStatisticsRecordController.version + "/statistics")
public interface ClientStatisticsRecordController {

    String version = "/v1.0";

    /**
     * Creates new record and saves it to the database.
     * We do not validate client key here, because it has passed authentication.
     * type and value are validated inside factory and record classes
     * @param type record type
     * @param value measured value
     * @param principal user auth info
     * @return created record
     * @throws WrongFormatException in case of issues. Handled by exception handler
     */
    @RequestMapping(value = "/{type}", method = RequestMethod.PUT, produces = "application/json")
    ClientStatisticsRecord newRecord(
            @PathVariable String type,
            @RequestParam Object value,
            Principal principal) throws WrongFormatException;

    /**
     * Returns list of client records. Supports pagination
     * @param page id of the records page
     * @param pageSize size of the records page
     * @param principal user auth info
     * @return page content
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    List<ClientStatisticsRecord> clientRecords(
            @RequestParam(required = false, defaultValue = "0")  Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            Principal principal);

    /**
     * Returns list of client records of the given type. Supports pagination
     * @param type  record type
     * @param page id of the records page
     * @param pageSize size of the records page
     * @param principal user auth info
     * @return page content
     */
    @RequestMapping(value = "/{type}", method = RequestMethod.GET, produces = "application/json")
    List<ClientStatisticsRecord> clientRecordsByType(
            @PathVariable String type,
            @RequestParam(required = false, defaultValue = "0")  Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            Principal principal);
}
