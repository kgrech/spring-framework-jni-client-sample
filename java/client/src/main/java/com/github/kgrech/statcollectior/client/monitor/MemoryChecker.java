package com.github.kgrech.statcollectior.client.monitor;

/**
 * Class to check memory status
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class MemoryChecker implements StatisticChecker {

    /**
     * @return type of checked value: memory, cpu or processes
     */
    @Override
    public String getType() {
        return "memory";
    }

    /**
     * @return amount of used memory in percentage
     */
    @Override
    public String getValue() {
        return String.valueOf(doGetValue());
    }

    private native float doGetValue();


}
