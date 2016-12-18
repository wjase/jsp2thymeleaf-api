/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.util;

import java.util.Map;
import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 *
 * @author jason
 */
public class ExpressionStringTemplate
{

    private final String format;

    public ExpressionStringTemplate(String format)
    {
        this.format = format;

    }

    public String generate(Map<String, String> values)
    {
        StrLookup<String> variableResolver = new ExpressionStringResolver(values);
        StrSubstitutor strSubstitutor = new StrSubstitutor(variableResolver);

        return strSubstitutor.replace(format);
    }

    public static String generate(String inputFormat, Map<String, String> values)
    {
        ExpressionStringTemplate expressionStringTemplate = new ExpressionStringTemplate(inputFormat);
        return expressionStringTemplate.generate(values);
    }

    private static class ExpressionStringResolver extends StrLookup<String>
    {

        private final Map<String, String> values;

        public ExpressionStringResolver(Map<String, String> values)
        {
            this.values = values;
        }

        @Override
        public String lookup(String key)
        {
            if (key.contains("!"))
            {
                String[] bits = key.split("!");
                String keyBit = bits[0];
                String filter = bits[1];
                return StringTransformers.get(filter).apply(values.getOrDefault(keyBit, ""));
            }
            return values.getOrDefault(key, "");
        }

    }
}
