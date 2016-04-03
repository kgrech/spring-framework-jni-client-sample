package com.github.kgrech.statcollectior.server.service;

import com.github.kgrech.statcollectior.server.exception.WrongFormatException;
import com.github.kgrech.statcollectior.server.model.ClientStatisticsRecord;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Instance to produce records of the given type
 * @author Konstantin G. (kgrech@mail.ru)
 */
@Component
public class ClientStatisticsRecordFactoryImpl implements ClientStatisticsRecordFactory {

    private final static String PROPERTIES_FILE = "/recordFactory.properties";

    private final Map<String, Constructor> mapping = new HashMap<>();

    /**
     * Inits factory. Reads properties file and resolves class constructors to be used in real time
     * @throws WrongFormatException in case of initialisation failure due to wrong properties file
     * or classes issues
     */
    @SuppressWarnings("unchecked")
    public ClientStatisticsRecordFactoryImpl() throws WrongFormatException {
        Properties properties = new Properties();
        InputStream stream = this.getClass().getResourceAsStream(PROPERTIES_FILE);
        if (stream == null)
            throw new WrongFormatException("Properties file is missing in classpath: " + PROPERTIES_FILE);
        try {
            properties.load(stream);
        } catch (IOException e) {
            throw new WrongFormatException("Can't read classpath properties file: " + PROPERTIES_FILE, e);
        }
        for (Map.Entry<Object, Object> en: properties.entrySet()) {
            String className = en.getValue().toString();
            Class clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new WrongFormatException("Wrong class: " + className, e);
            }
            if (!ClientStatisticsRecord.class.isAssignableFrom(clazz)) {
                throw new WrongFormatException(className + " in not a subclass of " +
                        ClientStatisticsRecord.class.getName());
            }
            try {
                Constructor con = clazz.getConstructor(String.class);
                mapping.put(en.getKey().toString(), con);
            } catch (NoSuchMethodException e) {
                throw new WrongFormatException("Can't find constructor with String parameter " +
                        " inside class: " + className, e);
            }
        }
    }

    /**
     * Returns all valid types of record
     * @return valid types of record
     */
    @Override
    public Set<String> validTypes() {
        return mapping.keySet();
    }

    /**
     * Creates new record
     * @param type record type
     * @param value measured value
     * @return new record
     * @throws WrongFormatException in case invalid record type of measured value
     * @throws RuntimeException in case of reflection api failure. Should never happen in case of case of
     * correct configuration. In case happen, client will recieve 500 error code
     */
    @Override
    public ClientStatisticsRecord createRecord(String type, Object value) throws WrongFormatException {
        if (!mapping.containsKey(type)) {
            throw new WrongFormatException("Wrong record type: " + type);
        }
        Constructor con = mapping.get(type);
        ClientStatisticsRecord record;
        try {
            record = (ClientStatisticsRecord) con.newInstance(type);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Can't crate record of type: " + type, e);
        }
        record.setValue(value);
        return record;
    }


}
