/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.util;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 * Sometimes you want to generate one out of a list of possible formats,
 * depending on the context variables available. ie if you have firstName and
 * lastName, then this. If you only have firstName then do that.
 *
 * @author jason
 */
public class AlternateFormatStrings
{

    public static Function<Map<String, Object>, String> fromFormats(String... formats)
    {
        final AlternateFormatStrings afs = new AlternateFormatStrings(formats);
        return (values) ->
        {
            return afs.format(values);
        };
    }

    public static Function<Map<String, Object>, String> constant(String value)
    {
        return (values) ->
        {
            return value;
        };
    }
    private List<CandidateFormat> candidateFormats;

    public AlternateFormatStrings(String... formatStrings)
    {
        candidateFormats = Arrays.stream(formatStrings)
                .map(item -> parse(item))
                .collect(Collectors.toList());

    }

    public Optional<String> formatWhichUsesValues(Map<String, Object> attributeMap)
    {
        Set<String> availableValues = attributeMap.keySet();
        return candidateFormats.stream()
                .filter((item) -> availableValues.containsAll(item.requiredAttributes))
                .findFirst()
                .map(it -> it.format);
    }

    private static CandidateFormat parse(String format)
    {
        CandidateFormat candidateFormat = new CandidateFormat();
        candidateFormat.format = format;
        candidateFormat.requiredAttributes = getRequiredValuesForFormat(format);
        return candidateFormat;
    }

    private static Set<String> getRequiredValuesForFormat(String candidateFormat)
    {
        Set<String> requiredVariables = new HashSet<>();
        StrSubstitutor ss = SimpleStringTemplateProcessor.getSubstitutor(new StrLookup<String>()
        {
            @Override
            public String lookup(String key)
            {
                final ValueDefaultAndFilter keyDefaultFilter = ValueDefaultAndFilter.parse(key);
                if (keyDefaultFilter.isRequired())
                {
                    requiredVariables.add(keyDefaultFilter.getName());
                }
                return "";
            }
        });
        ss.replace(candidateFormat); // do a dummy replace to find out what's needed
        return requiredVariables;
    }

    private String format(Map<String, Object> values)
    {
        return SimpleStringTemplateProcessor.generate(formatWhichUsesValues(values).orElseThrow(err(values)), values);
    }

    Supplier<RuntimeException> err(Map<String, Object> values)
    {
        return () ->
        {
            StringWriter message = new StringWriter();
            message.write("Could not select format from candidates:");
            message.write(candidateFormats.stream().map(it -> it.toString()).collect(Collectors.joining(",")));
            message.write('\n');
            message.write("Given Attributes:");
            message.write(values.keySet().stream().collect(Collectors.joining(",")));
            return new RuntimeException(message.toString());
        };
    }

    private static class CandidateFormat
    {

        private String format;
        private Set<String> requiredAttributes;

        @Override
        public String toString()
        {
            StringWriter stringWriter = new StringWriter();
            stringWriter.write("Format:");
            stringWriter.write(format);
            stringWriter.write('\n');
            stringWriter.write("Uses:");
            stringWriter.write(requiredAttributes.stream().collect(Collectors.joining(",")));
            stringWriter.write('\n');
            return stringWriter.toString();
        }

    }

}
