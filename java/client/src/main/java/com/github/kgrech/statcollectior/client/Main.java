package com.github.kgrech.statcollectior.client;

import com.github.kgrech.statcollectior.client.api.StatisticsUpdater;
import com.github.kgrech.statcollectior.client.api.StatisticsUpdaterIml;
import com.github.kgrech.statcollectior.client.monitor.ClientMonitor;

/**
 * Application entry point class
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class Main {

    /**
     * Application entry point
     * @param args cli args: host port client_key [updateRate]
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Wrong number of arguments. Usage java -jar client.jar host port client_key [updateRate]");
            System.exit(1);
        }
        int updateRate = 5 * 60; //Once in five minutes by default
        if (args.length > 3) {
            updateRate = Integer.valueOf(args[3]);
        }
        StatisticsUpdater updater = new StatisticsUpdaterIml(args[0], Integer.valueOf(args[1]), args[2]);
        new ClientMonitor(updater, updateRate);
    }
}
