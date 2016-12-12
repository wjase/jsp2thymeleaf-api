/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.expressions;

import static com.cybernostics.jsp2thymeleaf.api.expressions.ParameterListConverter.noParamChange;
import com.cybernostics.jsp2thymeleaf.api.util.OptionalListComma;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.el.Expression;
import org.apache.commons.el.FunctionInvocation;

/**
 *
 * @author jason
 */
public class DefaultFunctionExpressionConverter implements ExpressionFunctionConverter
{

    private ParameterListConverter paramConverter;

    public static DefaultFunctionExpressionConverter convertsMethodCall(String name)
    {
        return new DefaultFunctionExpressionConverter(name);
    }

    public DefaultFunctionExpressionConverter toMethodCall(String newName)
    {
        this.newFunctionName = newName;
        return this;
    }

    public DefaultFunctionExpressionConverter withNewParams(ParameterListConverter listconverter)
    {
        this.paramConverter = listconverter;
        return this;
    }

    private String oldFunctionName;
    private String newFunctionName;

    protected DefaultFunctionExpressionConverter(String name)
    {
        oldFunctionName = name;
        newFunctionName = name;
        paramConverter = noParamChange();
    }

    @Override
    public String applicableFor()
    {
        return oldFunctionName;
    }

    @Override
    public void writeExpression(Writer writer, Expression expr, ExpressionConverterContext converter)
    {
        try
        {
            FunctionInvocation functionInvocation = (FunctionInvocation) expr;
            final List<Expression> paramsFor = paramConverter.convertParams(functionInvocation.getArgumentList());
            writer.write(newFunctionName);
            writer.write("(");
            OptionalListComma.join(paramsFor, writer, ",", (w, ex) ->
            {
                converter.writeExpression(w, ex, converter);
            });
            writer.write(")");
        } catch (IOException ex)
        {
            Logger.getLogger(DefaultFunctionExpressionConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
