package com.github.kgrech.statcollectior.client.monitor;

import com.github.kgrech.statcollectior.client.api.StatisticsUpdater;

import java.io.File;
import java.util.*;

/**
 * System monitor. Ask resisted checkers with a given rate and updates the server
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class ClientMonitor {

    private static final String LIB_PATH = "./libstat_collector_native.so";
    private static final String TIMER_NAME = "statUpdateTimer";

    private final List<StatisticChecker> checkerList = new ArrayList<>();
    private final StatisticsUpdater updater;
    private final Timer timer = new Timer(TIMER_NAME);

    /**
     * Constructs new instance.
     * @param updater instance to update API
     * @param updateRate update rate in seconds. If rate equals to 0, update will be performed only once
     */
    public ClientMonitor(StatisticsUpdater updater, int updateRate) {
        this.updater = updater;
        System.load(new File(LIB_PATH).getAbsolutePath());
        registerCheckers();
        if (updateRate > 0) {
            scheduleUpdate(updateRate);
        } else {
            performUpdate();
        }
    }

    /**
     * Defines list of checkers to get machine parameters
     */
    private void registerCheckers() {
        checkerList.add(new CpuChecker());
        checkerList.add(new MemoryChecker());
        checkerList.add(new ProcessesChecker());
    }

    /**
     * Schedules an update with the given rate
     * @param updateRate update rate in seconds
     */
    private void scheduleUpdate(int updateRate) {
        TimerTask timerTask = new TimerTask() { //Can't use labda as TimerTask is not an interface :(
            @Override
            public void run() {
                performUpdate();
            }
        };
        timer.scheduleAtFixedRate(timerTask, new Date(), updateRate * 1000);
    }

    /**
     * Stops timer
     */
    public void cancel() {
        timer.cancel();
    }

    /**
     * Performs API updated asking all registered checkers for the information
     */
    public void performUpdate() {
        for (StatisticChecker checker: checkerList) {
            updater.sendStatistics(checker.getType(), checker.getValue());
        }
    }
}
