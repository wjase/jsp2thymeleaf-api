/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jdom2.Attribute;

/**
 *
 * @author jason
 */
public interface NewAttributeBuilder
{

    public static String stripELQuotes(String value)
    {
        if (!value.startsWith("${"))
        {
            return value;
        }
        if (value.equals("${}"))
        {
            return "";
        }
        return value.substring(2, value.length() - 1);
    }

    List<Attribute> buildNewAttributes(Map<String, String> currentValues);

    public static class NOPNewAttributeBuilder implements NewAttributeBuilder
    {

        @Override
        public List<Attribute> buildNewAttributes(Map<String, String> currentValues)
        {
            return Arrays.asList();
        }
    }

    static String ucfirst(String in)
    {
        return Character.toUpperCase(in.charAt(0)) + in.substring(1);
    }

    static String humanReadable(String input)
    {
        return Arrays.asList(input.replaceAll("(^\\$\\{)|(\\}$)", "").split("\\."))
                .stream()
                .map(NewAttributeBuilder::ucfirst)
                .collect(Collectors.joining());
    }

}
