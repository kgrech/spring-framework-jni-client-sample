package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.StatCollectorServerApplication;
import com.github.kgrech.statcollectior.server.exception.WrongFormatException;
import com.github.kgrech.statcollectior.server.model.Alert;
import com.github.kgrech.statcollectior.server.model.Client;
import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Test case for ClientStatisticsRecordService
 * @author Konstantin G. (kgrech@mail.ru)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StatCollectorServerApplication.class)
@WebAppConfiguration
public class AlertServiceTest extends MockTest {

    //This values are defined in test/resources/clients.xml
    private final String[] clients = new String[]{"test_client_1", "test_client_2", "test_client_3"};
    private final String[] floatTypes = new String[]{"memory", "cpu"};
    private final String[] intTypes = new String[]{"processes"};

    @Autowired
    private ClientStatisticsRecordFactory factory;

    @Test
    public void testClient1() {
        for (String type : floatTypes) {
            //Saving objects for first time. Notification should be sent
            ClientStatisticsRecord record = sendEvent(clients[0], type, 0.5f);
            verify(mailService, times(1)).sentMail(any(Client.class), any(Alert.class), eq(record));
            ;
        }
        for (String type : floatTypes) {
            //Notification should not be sent for daily updates
            ClientStatisticsRecord record = sendEvent(clients[0], type, 0.5f);
            verify(mailService, never()).sentMail(any(Client.class), any(Alert.class), eq(record));
        }
    }

    @Test
    public void testClient2() {
        for (String type: floatTypes) {
            //Notification should not sent for low values
            ClientStatisticsRecord record = sendEvent(clients[1], type, 0.2f);
            verify(mailService, never()).sentMail(any(Client.class), any(Alert.class), eq(record));
        }

        for (String type: floatTypes) {
            //Saving objects for first time. Notification should be sent
            ClientStatisticsRecord record = sendEvent(clients[1], type, 0.6f);
            verify(mailService, times(1)).sentMail(any(Client.class), any(Alert.class), eq(record));;
        }
        for (String type: floatTypes) {
            //Notification should not be sent for hourly updates
            ClientStatisticsRecord record = sendEvent(clients[1], type, 0.6f);
            verify(mailService, never()).sentMail(any(Client.class), any(Alert.class), eq(record));
        }
        for (int i = 0; i<5; i++) {
            //Repeated alerts are sent every time
            ClientStatisticsRecord record = sendEvent(clients[1], intTypes[0], 1000);
            verify(mailService, times(1)).sentMail(any(Client.class), any(Alert.class), eq(record));
        }
    }

    @Test
    public void testClient3() {
        //Notification should be always sent for immediate clients
        for (int i = 0; i<10; i++) {
            for (String type : floatTypes) {
                ClientStatisticsRecord record = sendEvent(clients[2], type, 0.7f);
                verify(mailService, times(1)).sentMail(any(Client.class), any(Alert.class), eq(record));
            }
        }
        //Notifications for disabled alerts are not sent
        ClientStatisticsRecord record = sendEvent(clients[2], intTypes[0], 1000);
        verify(mailService, never()).sentMail(any(Client.class), any(Alert.class), eq(record));
    }

    private ClientStatisticsRecord sendEvent(String client, String type, Object value) {
        ClientStatisticsRecord record = factory.createRecord(type, value);
        record.setClientKey(client);
        alertService.handleRecordUpdateEvent(new RecordUpdateEvent(record));
        return record;
    }

    @Test(expected = WrongFormatException.class)
    public void testWrongDurationInConfigHandled() {
        List<Client> clientsList = new ArrayList<>();
        Client client = new Client(clients[0], "abc", "wrongDuretion");
        clientsList.add(client);
        alertService.setClients(clientsList);
    }

    @Test(expected = WrongFormatException.class)
    public void testWrongAlertsInConfigHandled() {
        List<Client> clientsList = new ArrayList<>();
        Client client = new Client(clients[0], "abc", "daily");
        client.addAlert(new Alert(floatTypes[0], 0.5f, true, true));
        client.addAlert(new Alert(floatTypes[0], 0.4f, true, true));
        clientsList.add(client);
        alertService.setClients(clientsList);
    }


}
