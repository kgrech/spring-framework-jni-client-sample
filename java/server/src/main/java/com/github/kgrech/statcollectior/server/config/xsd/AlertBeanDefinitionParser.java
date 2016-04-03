package com.github.kgrech.statcollectior.server.config.xsd;

import com.github.kgrech.statcollectior.server.model.Alert;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Alert xml parser. Corresponding schema:
 * <xsd:element name="alert" maxOccurs="unbounded" minOccurs="1">
 *    <xsd:complexType>
 *    <xsd:complexContent>
 *    <xsd:extension base="beans:identifiedType">
 *    <xsd:attribute type="xsd:string" name="type" use="required"/>
 *    <xsd:attribute type="xsd:float" name="limit" use="required"/>
 *    <xsd:attribute type="xsd:boolean" name="enabled" use="required"/>
 *    <xsd:attribute type="xsd:boolean" name="repeat" use="required"/>
 *    </xsd:extension>
 *    </xsd:complexContent>
 *    </xsd:complexType>
 * </xsd:element>
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class AlertBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    /**
     * Determine the bean class corresponding to the supplied {@link Element}.
     * @param element the {@code Element} that is being parsed
     * @return the {@link Class} of the bean that is being defined via parsing
     * the supplied {@code Element}, or {@code null} if none
     * @see #getBeanClassName
     */
    @Override
    protected Class getBeanClass(Element element) {
        return Alert.class;
    }

    /**
     * Parse the supplied {@link Element} and populate the supplied
     * {@link BeanDefinitionBuilder} as required.
     * @param element the XML element being parsed
     * @param builder used to define the {@code BeanDefinition}
     */
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        builder.addConstructorArgValue(element.getAttribute("type"));
        builder.addConstructorArgValue(element.getAttribute("limit"));
        builder.addConstructorArgValue(element.getAttribute("enabled"));
        builder.addConstructorArgValue(element.getAttribute("repeat"));

    }

}
