package com.github.kgrech.statcollectior.server.web;

import com.github.kgrech.statcollectior.server.exception.WrongFormatException;
import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;
import com.github.kgrech.statcollectior.server.service.ClientStatisticsRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * Implementation of records rest controller. Uses swagger annotations to define online documentation
 * @author Konstantin G. (kgrech@mail.ru)
 */
@Api(tags = {"Statistics Rest API"}, description = "APIs to track clients statistics")
@RestController
public class ClientStatisticsRecordControllerImpl implements ClientStatisticsRecordController {

    @Autowired
    private ClientStatisticsRecordService recordService;

    /**
     * Creates new record and saves it to the database.
     * We do not validate client key here, because it has passed authentication.
     * type and value are validated inside factory and record classes
     * @param type record type
     * @param value measured value
     * @param principal user auth info
     * @return created record
     * @throws WrongFormatException in case of issues. Handled by exception hablder
     */

    @ApiOperation(value = "Put new record", notes = "Creates statistics record and saves it to the database.")
    @Override
    public ClientStatisticsRecord newRecord(
            @ApiParam(value = "Type of the information: memory, cpu or processes",
                    required = true, defaultValue = "memory")
            @PathVariable String type,
            @ApiParam(value = "Measured value",
                    required = true, defaultValue = "0.5")
            @RequestParam Object value,
            Principal principal) throws WrongFormatException {
        return recordService.newRecord(principal.getName(), type, value);
    }

    /**
     * Returns list of client records. Supports pagination
     * @param page id of the records page
     * @param pageSize size of the records page
     * @param principal user auth info
     * @return page content
     */
    @ApiOperation(value = "Get client record",
            notes = "Returns records for the current client. Support pagination")
    @Override
    public List<ClientStatisticsRecord> clientRecords(
            @ApiParam(value = "Page id", defaultValue = "0")
            @RequestParam(required = false, defaultValue = "0")  Integer page,
            @ApiParam(value = "Page size", defaultValue = "10")
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            Principal principal) {
        return recordService.clientRecords(principal.getName(), page, pageSize);
    }

    /**
     * Returns list of client records of the given type. Supports pagination
     * @param type  record type
     * @param page id of the records page
     * @param pageSize size of the records page
     * @param principal user auth info
     * @return page content
     */
    @ApiOperation(value = "Get client record", notes = "Returns records of the given type " +
                    "for the current client. Support pagination")
    @Override
    public List<ClientStatisticsRecord> clientRecordsByType(
            @ApiParam(value = "Type of the information: memory, cpu or processes",
                    required = true, defaultValue = "memory")
            @PathVariable String type,
            @ApiParam(value = "Page id", defaultValue = "0")
            @RequestParam(required = false, defaultValue = "0")  Integer page,
            @ApiParam(value = "Page size", defaultValue = "10")
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            Principal principal) {
        return recordService.clientRecordsByType(principal.getName(), type, page, pageSize);
    }
}
