package com.github.kgrech.statcollectior.server.config.xsd;

import com.github.kgrech.statcollectior.server.model.Client;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Client xml parser. Corresponding schema:
 * <xsd:element name="client">
 *    <xsd:complexType>
 *        <xsd:sequence>
 *            <xsd:element name="alert" maxOccurs="unbounded" minOccurs="1">
 *                <xsd:complexType>
 *                    <xsd:complexContent>
 *                        <xsd:extension base="beans:identifiedType">
 *                            <xsd:attribute type="xsd:string" name="type" use="required"/>
 *                            <xsd:attribute type="xsd:float" name="limit" use="required"/>
 *                            <xsd:attribute type="xsd:boolean" name="enabled" use="required"/>
 *                            <xsd:attribute type="xsd:boolean" name="repeat" use="required"/>
 *                            </xsd:extension>
 *                    </xsd:complexContent>
 *                </xsd:complexType>
 *            </xsd:element>
 *        </xsd:sequence>
 *        <xsd:attribute type="xsd:string" name="key"/>
 *        <xsd:attribute type="xsd:string" name="mail"/>
 *        <xsd:attribute type="xsd:string" name="duration"/>
 *    </xsd:complexType>
 * </xsd:element>
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class ClientBeanDefinitionParser extends AbstractBeanDefinitionParser {

    /**
     * Should an ID be generated instead if the passed in {@link Element} does not
     * specify an "id" attribute explicitly?
     * The parser will first check for an "id" attribute in this case,
     * only falling back to a generated ID if no value was specified.
     * @return whether the parser should generate an id if no id was specified
     */
    @Override
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }

    /**
     * Method to parse the supplied {@link Element} into {@link BeanDefinition BeanDefinitions}.
     *
     * @param element       the element that is to be parsed into one or more {@link BeanDefinition BeanDefinitions}
     * @param ctx the object encapsulating the current state of the parsing process;
     *                      provides access to a {@link BeanDefinitionRegistry}
     * @return the primary {@link BeanDefinition} resulting from the parsing of the supplied {@link Element}
     * @see #parse(Element, ParserContext)
     * @see #postProcessComponentDefinition(BeanComponentDefinition)
     */
    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext ctx) {
        BeanDefinitionBuilder clientFactory = BeanDefinitionBuilder.rootBeanDefinition(Client.class);
        clientFactory.addConstructorArgValue(element.getAttribute("key"));
        clientFactory.addConstructorArgValue(element.getAttribute("mail"));
        clientFactory.addConstructorArgValue(element.getAttribute("duration"));

        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(ClientFactoryBean.class);
        factory.addPropertyValue("client", clientFactory.getBeanDefinition());

        ManagedList<BeanDefinition> alertList = new ManagedList<>();
        AlertBeanDefinitionParser alertParser = new AlertBeanDefinitionParser();
        ParserContext nestedCtx = new ParserContext(ctx.getReaderContext(), ctx.getDelegate(),
                factory.getBeanDefinition());

        DomUtils.getChildElementsByTagName(element, "alert").forEach( el->
            alertList.add(alertParser.parse(el, nestedCtx)))
        ;
        factory.addPropertyValue("alerts", alertList);
        return factory.getBeanDefinition();
    }



}
