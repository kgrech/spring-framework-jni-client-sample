package com.github.kgrech.statcollectior.server.config.xsd;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Defines mapping between xml elements and parsers
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class ClientNamespaceHandler extends NamespaceHandlerSupport {

    /**
     * Registers required parsers
     */
    @Override
    public void init() {
        registerBeanDefinitionParser("client", new ClientBeanDefinitionParser());
    }

}

