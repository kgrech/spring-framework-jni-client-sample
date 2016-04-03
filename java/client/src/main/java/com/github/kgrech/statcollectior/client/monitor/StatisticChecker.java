package com.github.kgrech.statcollectior.client.monitor;

/**
 * Class to get system parameter
 * @author Konstantin G. (kgrech@mail.ru)
 */
public interface StatisticChecker {

    /**
     * @return type of checked value: memory, cpu or processes
     */
    String getType();

    /**
     * @return measured value as string
     */
    String getValue();
}
