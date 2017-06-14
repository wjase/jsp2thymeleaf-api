/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import com.cybernostics.jsp2thymeleaf.api.common.Namespaces;
import static com.cybernostics.jsp2thymeleaf.api.common.Namespaces.TH;
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

    List<Attribute> buildNewAttributes(Map<String, Object> currentValues);

    public static class NOPNewAttributeBuilder implements NewAttributeBuilder
    {

        @Override
        public List<Attribute> buildNewAttributes(Map<String, Object> currentValues)
        {
            return Arrays.asList();
        }
    }

    public static DefaultAttributeBuilder named(String name, Namespace namespace)
    {
        return new DefaultAttributeBuilder(name, namespace);
    }

    public static DefaultAttributeBuilder namedTH(String name)
    {
        return new DefaultAttributeBuilder(name, TH);
    }

    public static class DefaultAttributeBuilder implements NewAttributeBuilder
    {

        private String name;
        private Namespace namespace = Namespaces.NONS;
        private Function<Map<String, Object>, String> valueMaker;

        public DefaultAttributeBuilder(String name, Namespace namespace)
        {
            this.name = name;
            this.namespace = namespace;
        }

        public DefaultAttributeBuilder withNamespace(Namespace namespace)
        {
            this.namespace = namespace;
            return this;
        }

        public DefaultAttributeBuilder withValue(String valueKey)
        {
            valueMaker = (values) -> values.getOrDefault(valueKey, "").toString();
            return this;
        }

        public DefaultAttributeBuilder withValue(Function<Map<String, Object>, String> valueMaker)
        {
            this.valueMaker = valueMaker;
            return this;
        }

        @Override
        public List<Attribute> buildNewAttributes(Map<String, Object> currentValues)
        {
            return asList(new Attribute(name, valueMaker.apply(currentValues), namespace));
        }

    }

}
