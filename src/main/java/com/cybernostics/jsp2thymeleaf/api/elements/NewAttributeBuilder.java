/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.jdom2.Attribute;
import org.jdom2.Namespace;

/**
 *
 * @author jason
 */
public interface NewAttributeBuilder
{

    List<Attribute> buildNewAttributes(Map<String, String> currentValues);

    public static class NOPNewAttributeBuilder implements NewAttributeBuilder
    {

        @Override
        public List<Attribute> buildNewAttributes(Map<String, String> currentValues)
        {
            return Arrays.asList();
        }
    }

    public static DefaultAttributeBuilder attributeNamed(String name, Namespace namespace)
    {
        return new DefaultAttributeBuilder(name, namespace);
    }

    public static class DefaultAttributeBuilder implements NewAttributeBuilder
    {

        private String name;
        private Namespace namespace;
        private Function<Map<String, String>, String> valueMaker;

        public DefaultAttributeBuilder(String name, Namespace namespace)
        {
            this.name = name;
            this.namespace = namespace;
        }

        public DefaultAttributeBuilder withValue(String valueKey)
        {
            valueMaker = (values) -> values.getOrDefault(valueKey, "");
            return this;
        }

        public DefaultAttributeBuilder withValue(Function<Map<String, String>, String> valueMaker)
        {
            this.valueMaker = valueMaker;
            return this;
        }

        @Override
        public List<Attribute> buildNewAttributes(Map<String, String> currentValues)
        {
            return asList(new Attribute(name, valueMaker.apply(currentValues), namespace));
        }

    }

}
