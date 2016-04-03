package com.github.kgrech.statcollectior.client.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Test case for StatisticsUpdater class
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class StatisticsUpdaterTest {

    private StatisticsUpdater updater;


    @Test
    public void correctData() {
        updater = new StatisticsUpdaterIml("localhost", 8080, "client_1");
        assertEquals("Status code should be correct", 200, updater.sendStatistics("memory", "0.75"));
        assertEquals("Status code should be correct", 200, updater.sendStatistics("cpu", "0.75"));
        assertEquals("Status code should be correct", 200, updater.sendStatistics("processes", "75"));
    }

    @Test
    public void wrongData() {
        updater = new StatisticsUpdaterIml("localhost", 8080, "client_1");
        assertEquals("Status code should be correct", 400, updater.sendStatistics("unknownParameter", "0.75"));
        //processes are int
        assertEquals("Status code should be correct", 400, updater.sendStatistics("processes", "0.75"));
    }

    @Test
    public void correctWrongAuth() {
        updater = new StatisticsUpdaterIml("localhost", 8080, "client_worng");
        assertEquals("Status code should be correct", 401, updater.sendStatistics("memory", "0.75"));
    }

    @Test
    public void correctWServer() {
        updater = new StatisticsUpdaterIml("wrongIp", 8080, "client_worng");
        assertEquals("Status code should be correct", -1, updater.sendStatistics("memory", "0.75"));
    }


}
