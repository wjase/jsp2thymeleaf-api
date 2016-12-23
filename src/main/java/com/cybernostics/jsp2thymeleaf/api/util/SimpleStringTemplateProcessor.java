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
public class SimpleStringTemplateProcessor
{

    private final String format;

    public SimpleStringTemplateProcessor(String format)
    {
        this.format = format;
    }

    public String generate(Map<String, String> values)
    {
        return getSubstitutor(values).replace(format);
    }

    public static StrSubstitutor getSubstitutor(Map<String, String> values)
    {
        return getSubstitutor(new ValueResolverWithDefaultAndFilter(values));
    }

    public static StrSubstitutor getSubstitutor(StrLookup<String> variableResolver)
    {
        StrSubstitutor strSubstitutor = new StrSubstitutor(variableResolver);
        strSubstitutor.setEscapeChar('%');
        strSubstitutor.setVariablePrefix("%{");
        strSubstitutor.setEnableSubstitutionInVariables(false);
        return strSubstitutor;
    }

    public static String generate(String inputFormat, Map<String, String> values)
    {
        return getSubstitutor(values).replace(inputFormat);
    }

}
