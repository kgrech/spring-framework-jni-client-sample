package com.github.kgrech.statcollectior.server.model;

/**
 * Alert definition
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class Alert {

    private final String type;
    private final Float limit;
    private final Boolean enabled;
    private final Boolean repeat;

    /**
     * Creates new alert
     * @param type alert type
     * @param limit measured value limit
     * @param enabled enabled flag
     * @param repeat repeat flag
     */
    public Alert(String type, Float limit, Boolean enabled, Boolean repeat) {
        this.type = type;
        this.limit = limit;
        this.enabled = enabled;
        this.repeat = repeat;
    }

    public String getType() {
        return type;
    }

    public Float getLimit() {
        return limit;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Boolean getRepeat() {
        return repeat;
    }
}


