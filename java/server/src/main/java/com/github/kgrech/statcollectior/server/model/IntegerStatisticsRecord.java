package com.github.kgrech.statcollectior.server.model;

import com.github.kgrech.statcollectior.server.exception.WrongFormatException;

/**
 * Statistics information with integer measured value
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class IntegerStatisticsRecord extends ClientStatisticsRecord<Integer> {

    public IntegerStatisticsRecord() {
    }

    /**
     * Initializes object. Generates new id for the record.
     * @param type record type
     */
    public IntegerStatisticsRecord(String type) {
        setType(type);
    }

    /**
     * Validates and converts the value to the required type
     *
     * @param value measured value
     * @return converted value
     * @throws WrongFormatException in case of conversion failed
     */
    @Override
    protected Integer convert(Object value) throws WrongFormatException {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (NumberFormatException e)  {
            throw new WrongFormatException("Value " + value.toString() +
                    "could not be converted to Float", e);
        }
    }

    /**
     * Checks whether measured value is above given the limit
     *
     * @param limit value limit
     * @return true if limit is exceeded
     */
    @Override
    public boolean aboveTheLimit(Float limit) {
        return getValue() > limit;
    }
}
