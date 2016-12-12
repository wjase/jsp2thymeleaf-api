/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.expressions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.el.Expression;

/**
 *
 * @author jason
 */
@FunctionalInterface
public interface ParameterListConverter
{

    List<Expression> convertParams(List<Expression> expressions);

    public static ParameterListConverter noParamChange()
    {
        return (expressions) ->
        {
            return expressions;
        };
    }

    public static ParameterListConverter reorderParams(final int... indices)
    {
        return new ParameterListConverter()
        {
            @Override
            public List<Expression> convertParams(List<Expression> expressions)
            {
                return Arrays
                        .stream(indices)
                        .mapToObj((intO) -> expressions.get(intO))
                        .collect(Collectors.toList());
            }
        };
    }
}
