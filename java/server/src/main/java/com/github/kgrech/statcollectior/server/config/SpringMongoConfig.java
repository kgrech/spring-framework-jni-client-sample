package com.github.kgrech.statcollectior.server.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.Resource;

/**
 * A configuration class for mongodb
 * @author Konstatin Grechishchev
 */
@Configuration
@PropertySource(value = "classpath:mongo.properties")
@EnableMongoRepositories("com.github.kgrech.statcollectior.server.repo")
public class SpringMongoConfig extends AbstractMongoConfiguration {

    private static final Logger logger = Logger.getLogger(SpringMongoConfig.class);

    @Resource
    private Environment environment;

    @Override
    public Mongo mongo() throws Exception {
        Mongo m = new MongoClient(environment.getProperty("mongo.host") + ":" +
                environment.getProperty("mongo.port"));
        m.setWriteConcern(WriteConcern.SAFE); //See: https://docs.mongodb.org/manual/reference/write-concern/
        logger.info("Connecting to mongodb: "+ m.getAddress());
        return m;
    }

    @Override
    protected String getDatabaseName() {
        return environment.getProperty("mongo.base");
    }


}