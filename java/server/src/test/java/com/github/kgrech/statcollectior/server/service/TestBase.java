package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.repo.ClientStatisticsRecordRepository;
import com.github.kgrech.statcollectior.server.repo.EmailLogEntryRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for all unit tests: clears the database
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class TestBase {

    @Autowired
    protected ClientStatisticsRecordRepository recordRepository;
    @Autowired
    protected EmailLogEntryRepository logEntryRepository;

    @Before
    @After
    public void cleanData() {
        recordRepository.deleteAll();
        logEntryRepository.deleteAll();
    }

}
