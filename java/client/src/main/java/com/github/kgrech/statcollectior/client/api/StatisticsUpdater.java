package com.github.kgrech.statcollectior.client.api;

/**
 * Interface for performing api updates.
 * @author Konstantin G. (kgrech@mail.ru)
 */
public interface StatisticsUpdater {

    /**
     * Sends statistics to the server
     * @param type measured parameter name
     * @param value measured parameter value
     * @return API responce code or -1 in case connection failure
     */
    int sendStatistics(String type, String value);
}
