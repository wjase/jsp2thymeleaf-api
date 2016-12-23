/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.util;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jason
 */
class ValueDefaultAndFilter
{

    private String name;
    private Optional<String> defaultValue;
    private Optional<String> filterName;
    private Function<String, String> filter;

    public String getName()
    {
        return name;
    }

    public Optional<String> getDefaultValue()
    {
        return defaultValue;
    }

    public Optional<String> getFilterName()
    {
        return filterName;
    }

    public String getFromMap(Map<String, String> source)
    {
        return filter.apply(source.getOrDefault(name, defaultValue.orElseGet(() -> "")));
    }

    public static ValueDefaultAndFilter parse(String valueName)
    {
        ValueDefaultAndFilter value = new ValueDefaultAndFilter();
        value.name = valueName;
        value.defaultValue = Optional.empty();
        value.filterName = Optional.empty();
        if (hasFilter(valueName))
        {
            String[] bits = valueName.split("!");
            value.name = bits[0];
            value.filterName = Optional.of(bits.length > 1 ? bits[1] : "");
            if (StringUtils.isBlank(value.filterName.get()))
            {
                throw new IllegalArgumentException("missing filter name for variable " + valueName);
            }
        }
        if (hasDefault(value.name))
        {
            String[] bits = value.name.split("\\|");
            value.name = bits[0];
            value.defaultValue = Optional.of(bits.length > 1 ? bits[1] : "");
        }
        value.filter = StringTransformers.get(value.filterName.orElseGet(() -> ""));
        return value;
    }

    public static boolean hasDefault(String valueName)
    {
        return valueName.contains("|");
    }

    public static boolean hasFilter(String valueName)
    {
        return valueName.contains("!");
    }

    public boolean isRequired()
    {
        return !defaultValue.isPresent();
    }

}
