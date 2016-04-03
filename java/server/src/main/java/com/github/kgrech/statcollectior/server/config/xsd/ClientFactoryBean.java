package com.github.kgrech.statcollectior.server.config.xsd;

import com.github.kgrech.statcollectior.server.model.Alert;
import com.github.kgrech.statcollectior.server.model.Client;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;

/**
 * Factory class to create client object
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class ClientFactoryBean implements FactoryBean<Client> {

    private Client client;
    private List<Alert> alerts;

    /**
     * Sets client object
     * @param client client object
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Sets alerts list
     * @param alerts alerts list
     */
    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    /**
     * Return an instance  of the client
     * managed by this factory.
     * @return an instance of the bean (can be {@code null})
     */
    @Override
    public Client getObject() throws Exception {
        if (this.alerts != null && this.alerts.size() > 0) {
            for (Alert child : alerts) {
                this.client.addAlert(child);
            }
        }
        return this.client;
    }

    /**
     * Return the type of object that this FactoryBean creates,
     * or {@code null} if not known in advance.
     * @return the type of object that this FactoryBean creates
     */
    @Override
    public Class<Client> getObjectType() {
        return Client.class;
    }

    /**
     * Is the object managed by this factory a singleton? That is,
     * will {@link #getObject()} always return the same object
     * (a reference that can be cached)?
     * @return whether the exposed object is a singleton
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
