package com.github.kgrech.statcollectior.client.api;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;


/**
 * Class for performing api updates.
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class StatisticsUpdaterIml implements StatisticsUpdater {

    private final static Logger logger = Logger.getLogger(StatisticsUpdaterIml.class);
    private final static String HTTP = "http";
    private final static String URL_PREFIX = "/v1.0/statistics/";
    private final static String VALUE = "value";

    private final String host;
    private final int port;
    private final String clientKey;

    /**
     * Constructs new instance
     * @param host host name or ip
     * @param port port number to use
     * @param clientKey client key to authorize the request
     */
    public StatisticsUpdaterIml(String host, int port, String clientKey) {
        this.host = host;
        this.port = port;
        this.clientKey = clientKey;
    }

    /**
     * Sends statistics to the server.
     * Calls /v1.0/statistics/{type}?value={value} api
     * @param type measured parameter name
     * @param value measured parameter value
     * @return API responce code or -1
     */
    @Override
    public int sendStatistics(String type, String value){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(clientKey, clientKey));

        BasicHttpContext localContext = new BasicHttpContext();
        HttpPut httpPut;
        try {
            URIBuilder builder = new URIBuilder()
                    .setScheme(HTTP).setHost(host).setPort(port)
                    .setPath(URL_PREFIX + type)
                    .setParameter(VALUE, value);
            httpPut = new HttpPut(builder.build()); //Prepare PUT request

        } catch (URISyntaxException e) {
            logger.error("URl building error", e);
            return -1;
        }
        HttpResponse response;
        try {
            response = httpclient.execute(httpPut, localContext);
        } catch (IOException e) {
            logger.error("Can't update value " + type, e);
            return -1;
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            logger.error("Can't update value " + type + ". Error: " + response.getStatusLine());
            try {
                logger.error(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
            } catch (IOException ignored) {}
        } else {
            logger.info("Updated " + type + ": " + value);
        }
        return response.getStatusLine().getStatusCode();
    }


}
