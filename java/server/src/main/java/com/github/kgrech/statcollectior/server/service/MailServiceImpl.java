package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.model.Alert;
import com.github.kgrech.statcollectior.server.model.Client;
import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Implementation of MailService
 * @author Konstantin G. (kgrech@mail.ru)
 */
@Service
@PropertySource(value = "classpath:mail.properties")
public class MailServiceImpl implements MailService {

    private static final Logger logger = Logger.getLogger(MailService.class);

    @Resource
    private Environment environment;

    private final JavaMailSenderImpl sender = new JavaMailSenderImpl();
    private final String[] javaMailProps = new String[] {
            "mail.smtp.auth", "mail.smtp.starttls.enable", "mail.smtp.quitwait"
    };

    /**
     * Creates mail sender instance
     */
    @PostConstruct
    public void init() {
        sender.setHost(environment.getProperty("mail.host"));
        sender.setPort(Integer.valueOf(environment.getProperty("mail.port")));
        sender.setProtocol(environment.getProperty("mail.protocol"));
        sender.setUsername(environment.getProperty("mail.userName"));
        sender.setPassword(environment.getProperty("mail.password"));
        for (String key: javaMailProps) {
            sender.getJavaMailProperties().setProperty(key, environment.getProperty(key));
        }
    }

    /**
     * Sends e-mail to the client about alert
     * @param client client information
     * @param alert info about triggered alert
     * @param record statistics record with value over the limit
     */
    @Override
    public void sentMail(Client client, Alert alert, ClientStatisticsRecord record) {
        String recipientAddress = client.getMail();
        String subject = "Alert notification: " + alert.getType();
        String message = "This email sent to notify you that value of parameter " +
                record.getType() + " is equals to " + record.getValue() +
                " which is higher then current limit (" +
                + alert.getLimit() + ")!";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        sender.send(email);
        logger.info("Sent mail: " + client.getMail() + ". " +
            alert.getType() + ": " + record.getValue() + ">" + alert.getLimit());
    }

}
