package com.github.kgrech.statcollectior.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.builders.PathSelectors;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.*;

/**
 * Configuration for swagger
 * @author Konstantin G. (kgrech@mail.ru)
 */
@Configuration
@ComponentScan(basePackages = "com.github.kgrech.statcollectior.server.web")
@EnableWebMvc
@EnableSwagger2 //Loads the spring beans required by the framework
@PropertySource("classpath:swagger.properties")
@Import(SwaggerUiConfiguration.class)
public class SwaggerConfig {

    @Autowired
    private Defaults defaults;

    /**
     * Defines general information about api
     * @return api information object
     */
    @Bean
    ApiInfo apiInfo() {
        Contact contact = new Contact(
                "Konstantin G.",
                "https://github.com/kgrech",
                "kgrech@mail.ru"
        );
        ApiInfo apiInfo = new ApiInfo(
                "StatCollector Project",
                "This the server side part of crossover spring-framework-jni-client-sample",
                "1.0",
                "Terms of service are not defined :)",
                contact,
                "Apache licence 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0" );
        return apiInfo;
    }

    /**
     * Defines swagger docket container
     */
    @Bean
    public Docket customImplementation(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).
                select().paths(paths()).build();
        docket.ignoredParameterTypes(Principal.class, WebRequest.class, NativeWebRequest.class);
        return docket;
    }

    /**
     * @return path filtering predicates
     */
    private Predicate<String> paths() {
        return Predicates.not(PathSelectors.regex("/error"));
    }

    /**
     * Inits swagger, sets default response codes
     */
    @PostConstruct
    public void init() {
        List<ResponseMessage> defaultCodes = new ArrayList<>();
        defaultCodes.add(new ResponseMessage(200,
                "OK", null, new HashMap<>()));
        defaultCodes.add(new ResponseMessage(400,
                "Bad request. Missing required parameter or invalid json passed", null, new HashMap<>()));
        defaultCodes.add(new ResponseMessage(401,
                "Unauthorized or invalid credentials specified", null, new HashMap<>()));

        Map<RequestMethod, List<ResponseMessage>> responses = defaults.defaultResponseMessages();
        for (Map.Entry<RequestMethod, List<ResponseMessage>> en: responses.entrySet()) {
            en.setValue(defaultCodes);
        }
    }

}