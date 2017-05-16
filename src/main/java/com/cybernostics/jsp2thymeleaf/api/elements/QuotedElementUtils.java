/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import com.cybernostics.jsp.parser.JSPParser;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.apache.commons.el.parser.ParseException;

/**
 *
 * @author jason
 */
public class QuotedElementUtils
{

    private static Logger LOG = Logger.getLogger(QuotedElementUtils.class.getName());

    private QuotedElementUtils()
    {

    }

    public static Optional<String> attributeValue(JSPParser.HtmlQuotedElementContext context, String name, JSPElementNodeConverter elementNodeConverter)
    {
        return context.atts
                .stream()
                .filter(att -> name.equals(att.name.getText()))
                .map(i -> attributeValue(i.value, elementNodeConverter))
                .findFirst();
    }

    public static Optional<String> attributeIdentifier(JSPParser.HtmlQuotedElementContext context, String name, JSPElementNodeConverter elementNodeConverter)
    {
        return context.atts
                .stream()
                .filter(att -> name.equals(att.name.getText()))
                .map(i -> attributeIdentifier(i.value, elementNodeConverter))
                .findFirst();
    }

    public static String attributeValue(JSPParser.HtmlAttributeValueContext context, JSPElementNodeConverter elementNodeConverter)
    {
        final JSPParser.HtmlAttributeValueConstantContext asConstant = context.htmlAttributeValueConstant();
        if (asConstant != null)
        {
            return "'" + asConstant.getText() + "'";
        }
        final JSPParser.HtmlAttributeValueExprContext asExpression = context.htmlAttributeValueExpr();
        if (asExpression != null)
        {
            try
            {
                return ELExpressionConverter.convertEmbedded(asExpression.getText(), elementNodeConverter.getScopedConverters());
            } catch (ParseException ex)
            {
                throw new RuntimeException(ex);
            }
        }
        return elementNodeConverter.processAsAttributeValue(context.htmlQuotedElement(), elementNodeConverter);
    }

    public static String attributeIdentifier(JSPParser.HtmlAttributeValueContext context, JSPElementNodeConverter elementNodeConverter)
    {
        final JSPParser.HtmlAttributeValueConstantContext asConstant = context.htmlAttributeValueConstant();
        if (asConstant != null)
        {
            return asConstant.getText();
        }
        final JSPParser.HtmlAttributeValueExprContext asExpression = context.htmlAttributeValueExpr();
        if (asExpression != null)
        {
            try
            {
                return ELExpressionConverter.convertEmbedded(asExpression.getText(), elementNodeConverter.getScopedConverters());
            } catch (ParseException ex)
            {
                throw new RuntimeException(ex);
            }
        }
        return elementNodeConverter.processAsAttributeValue(context.htmlQuotedElement(), elementNodeConverter);
    }

    public static void warnNotFullyConvertedIf(JSPParser.HtmlQuotedElementContext node, String... attNames)
    {
        Arrays
                .stream(attNames)
                .filter(attName -> attributeValue(node, attName, null).isPresent())
                .forEach(attName -> LOG.warning("attribute " + attName + "not converted in quoted element:" + node.getText()));
    }

    public static Stream<JSPParser.HtmlQuotedElementContext> childElements(JSPParser.HtmlQuotedElementContext node)
    {
        return node.children
                .stream()
                .filter(c -> c instanceof JSPParser.QuotedHtmlContentContext)
                .flatMap(c -> ((JSPParser.QuotedHtmlContentContext) c).children.stream())
                .filter(c -> c instanceof JSPParser.HtmlQuotedElementContext)
                .map(c -> (JSPParser.HtmlQuotedElementContext) c);
    }

//    public static Map<String, String> paramsFor(JSPParser.HtmlQuotedElementContext node){
//        return node.children
//                .stream()
//                .map(i->i.getPayload())
//                .collect(toMap((nd)->attributeValue(nd.g,"name","noname"),(nd)->attributeValue(nd, "value", "novalue")));
//    }
}
