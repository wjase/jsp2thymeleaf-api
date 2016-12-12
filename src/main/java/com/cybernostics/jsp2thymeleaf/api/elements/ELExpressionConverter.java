/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import com.cybernostics.jsp2thymeleaf.api.expressions.ActiveExpressionConverters;
import com.cybernostics.jsp2thymeleaf.api.expressions.ExpressionConverterContext;
import com.cybernostics.jsp2thymeleaf.api.expressions.FunctionConverterSource;
import com.cybernostics.jsp2thymeleaf.api.expressions.SymbolWriter;
import com.cybernostics.jsp2thymeleaf.api.util.PrefixedName;
import static com.cybernostics.jsp2thymeleaf.api.util.PrefixedName.prefixedNameFor;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.el.*;
import org.apache.commons.el.parser.ELParser;
import org.apache.commons.el.parser.ParseException;

/**
 *
 * @author jason
 */
public class ELExpressionConverter implements ExpressionConverterContext
{

    public ELExpressionConverter()
    {
        buildConverters();
    }

    public String convert(String toConvert)
    {
        try
        {
            ELParser eLParser = new ELParser(new StringReader(toConvert));
            final Object expressionOrString = eLParser.ExpressionString();
            if (expressionOrString instanceof String)
            {
                return expressionOrString.toString();
            }
            StringWriter newExpression = new StringWriter();
            newExpression.append("${");
            visitAndConvert(newExpression, expressionOrString);
            newExpression.append("}");
            return newExpression.toString();
        } catch (ParseException ex)
        {
            Logger.getLogger(ELExpressionConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void visitAndConvert(Writer newExpression, Object expressionOrString)
    {

        if (expressionOrString instanceof String)
        {
            write(newExpression, expressionOrString.toString());
        } else if (expressionOrString instanceof Expression)
        {
            expressionHandler.get(expressionOrString.getClass())
                    .accept(newExpression, expressionOrString);
        }

    }

    private void buildConverters()
    {
        expressionHandler.put(
                BinaryOperatorExpression.class,
                (writer, expr) ->

        {
            try
            {
                final BinaryOperatorExpression conditonExpression = (BinaryOperatorExpression) expr;
                writer.append("(");
                visitAndConvert(writer, conditonExpression.getExpression());
                final List operators = conditonExpression.getOperators();
                final List expressions = conditonExpression.getExpressions();
                for (int i = 0; i < operators.size(); i++)
                {
                    BinaryOperator operator = (BinaryOperator) operators.get(i);
                    Expression expression = (Expression) expressions.get(i);
                    SymbolWriter.write(writer, operator);
                    writer.append(operator.getOperatorSymbol());
                    visitAndConvert(writer, expression);
                }
                writer.append(")");
            } catch (IOException ex)
            {
                Logger.getLogger(ELExpressionConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        expressionHandler.put(
                BooleanLiteral.class,
                ELExpressionConverter::writeAsExpressionObj);
        expressionHandler.put(
                ComplexValue.class,
                ELExpressionConverter::writeAsExpressionObj);
        expressionHandler.put(
                ConditionalExpression.class,
                ELExpressionConverter::writeAsExpressionObj);
        expressionHandler.put(
                FloatingPointLiteral.class,
                ELExpressionConverter::writeAsExpressionObj);
        expressionHandler.put(
                FunctionInvocation.class,
                (writer, expr) ->

        {
            FunctionInvocation func = (FunctionInvocation) expr;
            final PrefixedName prefixedName = prefixedNameFor(func.getFunctionName());
            final Optional<FunctionConverterSource> converter = ActiveExpressionConverters.forPrefix(prefixedName.getPrefix());
            Optional<ExpressionConverterContext> expressionConverterContext = converter
                    .get()
                    .converterFor(prefixedName);
            expressionConverterContext.get().writeExpression(writer, func, this);
        });
        expressionHandler.put(
                IntegerLiteral.class,
                ELExpressionConverter::writeAsExpressionObj);
        expressionHandler.put(
                Literal.class,
                ELExpressionConverter::writeAsExpressionObj);
        expressionHandler.put(
                NamedValue.class,
                ELExpressionConverter::writeAsExpressionObj);
        expressionHandler.put(
                StringLiteral.class,
                ELExpressionConverter::writeAsExpressionObj);
        expressionHandler.put(
                UnaryOperatorExpression.class,
                (writer, expr) ->
        {
            final UnaryOperatorExpression unaryOperator = (UnaryOperatorExpression) expr;
            SymbolWriter.write(writer, unaryOperator.getOperator());
            visitAndConvert(writer, unaryOperator.getExpression());
        });
    }

    private static void writeAsExpressionObj(Writer w, Object expr)
    {
        write(w, (Expression) expr);
    }

    private static void write(Writer w, Expression expr)
    {
        write(w, expr.getExpressionString());
    }

    private static void write(Writer w, String s)
    {
        try
        {
            w.write(s);
        } catch (IOException ex)
        {
            Logger.getLogger(ELExpressionConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    Map<Class<?>, BiConsumer<Writer, Object>> expressionHandler = new HashMap<>();

    @Override
    public void writeExpression(Writer writer, Expression invocation, ExpressionConverterContext context)
    {
        visitAndConvert(writer, invocation);
    }
}
