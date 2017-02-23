/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.expressions.function;

import com.cybernostics.jsp2thymeleaf.api.expressions.ExpressionVisitor;
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

    private Map<String, ExpressionVisitor> converterMap = new HashMap<>();
    private String uri;

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public FunctionConverterSource()
    {

    }

    public FunctionConverterSource(String forUri, ExpressionVisitor... converters)
    {
        this.uri = forUri;
        for (ExpressionVisitor converter : converters)
        {
            add(converter);
        }
    }

    public FunctionConverterSource(String forUri, List<ExpressionVisitor> converters)
    {
        this.uri = forUri;
        for (ExpressionVisitor converter : converters)
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

    public Optional<ExpressionVisitor> converterFor(PrefixedName domTag)
    {
        return Optional.ofNullable(converterMap.get(domTag.getName()));
    }

    public Optional<ExpressionVisitor> converterFor(Expression expression)
    {
        PrefixedName domTag = prefixedNameFor(expression.getExpressionString());
        return converterFor(domTag);
    }

    public void add(ExpressionVisitor converter)
    {
        converterMap.put(converter.getConvertsMethodName(), converter);
    }
}
