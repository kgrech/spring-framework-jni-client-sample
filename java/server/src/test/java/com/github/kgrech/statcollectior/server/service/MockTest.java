package com.github.kgrech.statcollectior.server.service;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base test class with mocking of e-mail sender
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class MockTest extends TestBase {

    @Autowired
    protected ClientStatisticsRecordServiceImpl statisticsRecordService;
    @Mock
    protected MailService mailService;
    @InjectMocks
    @Autowired
    protected AlertServiceImpl alertService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

}
