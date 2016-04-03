package com.github.kgrech.statcollectior.client.monitor;

/**
 * Class to check memory status
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class ProcessesChecker implements StatisticChecker {

    /**
     * @return type of checked value: memory, cpu or processes
     */
    @Override
    public String getType() {
        return "processes";
    }

    /**
     * @return number of active processes
     */
    @Override
    public String getValue() {
        return String.valueOf(doGetValue());
    }

    private native int doGetValue();

}
