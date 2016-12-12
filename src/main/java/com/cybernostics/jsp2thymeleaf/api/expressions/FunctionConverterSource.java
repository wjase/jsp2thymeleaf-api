/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.expressions;

import com.cybernostics.jsp2thymeleaf.api.util.PrefixedName;
import static com.cybernostics.jsp2thymeleaf.api.util.PrefixedName.prefixedNameFor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.el.Expression;

/**
 *
 * @author jason
 */
public class FunctionConverterSource
{

    private Map<String, ExpressionConverterContext> converterMap = new HashMap<>();
    private String uri;

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public FunctionConverterSource()
    {

    }

    public FunctionConverterSource(String forUri, ExpressionFunctionConverter... converters)
    {
        this.uri = forUri;
        for (ExpressionFunctionConverter converter : converters)
        {
            add(converter);
        }
    }

    public FunctionConverterSource(String forUri, List<ExpressionFunctionConverter> converters)
    {
        this.uri = forUri;
        for (ExpressionFunctionConverter converter : converters)
        {
            add(converter);
        }
    }

    /**
     * Returns the conventional URI used to match this taglib.
     *
     * @return
     */
    public String getTaglibURI()
    {
        return uri;
    }

    public Optional<ExpressionConverterContext> converterFor(PrefixedName domTag)
    {
        return Optional.ofNullable(converterMap.get(domTag.getName()));
    }

    public Optional<ExpressionConverterContext> converterFor(Expression expression)
    {
        PrefixedName domTag = prefixedNameFor(expression.getExpressionString());
        return converterFor(domTag);
    }

    public void add(ExpressionFunctionConverter converter)
    {
        converterMap.put(converter.applicableFor(), converter);
    }
}
