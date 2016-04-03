package com.github.kgrech.statcollectior.client.api;

import com.github.kgrech.statcollectior.client.monitor.ClientMonitor;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Test client monitor
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class ClientMonitorTest {

    private class StatisticsUpdaterMock implements StatisticsUpdater {
        public int count = 0;
        /**
         * Just counts number of updates and to nothing
         */
        @Override
        public int sendStatistics(String type, String value) {
            count++;
            return 200;
        }
    }

    @Test
    public void testMonitorSchedule() throws InterruptedException {
        StatisticsUpdaterMock updater = new StatisticsUpdaterMock();
        ClientMonitor monitor = new ClientMonitor(updater, 1); //Update every sedcond
        Thread.sleep(5000);
        monitor.cancel();
        assertTrue("Update should be called few times", updater.count > 3);
    }

    @Test
    public void testMonitorOnce() throws InterruptedException {
        StatisticsUpdaterMock updater = new StatisticsUpdaterMock();
        new ClientMonitor(updater, 0); //Update  once
        Thread.sleep(3000);
        assertEquals("Update should be called one time only", 3, updater.count);
    }

}
