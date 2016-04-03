package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.StatCollectorServerApplication;
import com.github.kgrech.statcollectior.server.model.Alert;
import com.github.kgrech.statcollectior.server.model.Client;
import com.github.kgrech.statcollectior.server.model.FloatStatisticsRecord;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Class to test mail sender features
 * @author Konstantin G. (kgrech@mail.ru)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StatCollectorServerApplication.class)
@WebAppConfiguration
public class MailSenderTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private List<Client> clients;

    @Test
    public void sendTestMail() {
        //TODO: This is wrong example, replace with some mock for mail sending
        Client client = clients.get(0);
        String testAlertType = new ObjectId().toString();
        Alert alert = new Alert(testAlertType, 0.1f, true, true);
        FloatStatisticsRecord record = new FloatStatisticsRecord();
        record.setClientKey(client.getKey());
        record.setValue(1.0f);
        //mailService.sentMail(client, alert, record);
        //Wait for some time, go to test mailbox and check
        //the presence of letter with testAlertType in the subject in sent folder
    }
}
