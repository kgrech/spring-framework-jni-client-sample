package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.StatCollectorServerApplication;
import com.github.kgrech.statcollectior.server.exception.WrongFormatException;
import com.github.kgrech.statcollectior.server.model.Alert;
import com.github.kgrech.statcollectior.server.model.Client;
import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Test case for ClientStatisticsRecordService
 * @author Konstantin G. (kgrech@mail.ru)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StatCollectorServerApplication.class)
@WebAppConfiguration
public class ClientStatisticsRecordServiceTest extends MockTest {

    //This values are defined in test/resources/clients.xml
    private final String[] clients = new String[]{"test_client_1", "test_client_2", "test_client_3"};
    private final String[] floatTypes = new String[]{"memory", "cpu"};
    private final String[] intTypes = new String[]{"processes"};
    private final int alertsPerClient = 50;
    private final Random r = new Random();

    @Test
    public void testRecordsStoring() {
        for (int i = 0; i<alertsPerClient; i++) {
            for (String client : clients) {
                for (String alert : floatTypes) {
                    statisticsRecordService.newRecord(client, alert, r.nextFloat());
                }
                for (String alert : intTypes) {
                    statisticsRecordService.newRecord(client, alert, r.nextInt());
                }
            }
        }
        int count = alertsPerClient*(floatTypes.length + intTypes.length);
        for (String client : clients) {
            List<ClientStatisticsRecord> records = statisticsRecordService
                    .clientRecords(client, 0, count + 10);
            assertEquals("Correct number of records should be loaded", count, records.size());
            for (String alert : floatTypes) {
                records = statisticsRecordService
                        .clientRecordsByType(client, alert, 0, alertsPerClient + 10);
                assertEquals("Correct number of records should be loaded", alertsPerClient, records.size());
            }
            for (String alert : intTypes) {
                records = statisticsRecordService
                        .clientRecordsByType(client, alert, 0, alertsPerClient + 10);
                assertEquals("Correct number of records should be loaded", alertsPerClient,records.size());
            }
        }
    }

    @Test(expected = WrongFormatException.class)
    public void testWrongTypeHandled() {
        statisticsRecordService.newRecord(clients[0], "wrongType", r.nextFloat());
    }

    @Test(expected = WrongFormatException.class)
    public void testWrongFloatValueHandled() {
        statisticsRecordService.newRecord(clients[0], floatTypes[0], "not_number");
    }

    @Test(expected = WrongFormatException.class)
    public void testWrongIntValueHandled() {
        statisticsRecordService.newRecord(clients[0], intTypes[0], 2.5);
    }

    @Test(expected = WrongFormatException.class)
    public void testWrongTypeInConfigHandled() {
        List<Client> clientsList = new ArrayList<>();
        Client client = new Client(clients[0], "abc", "daily");
        client.addAlert(new Alert("wrongType", 0.5f, true, true));
        clientsList.add(client);
        statisticsRecordService.setClients(clientsList);
    }

}
