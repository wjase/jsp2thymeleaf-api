/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import com.cybernostics.jsp.parser.JSPParser;
import java.util.List;
import org.jdom2.Content;

/**
 *
 * @author jason
 */
public interface JSPElementNodeConverter
{

    /**
     * Transform node into a list of output Content to be inserted in the target
     * document. It may not necessarily be one output element for each input
     * element. (The input may just get ignored in some circumstances, although
     * it might be preferrable to replace it with an html comment initially so
     * nothing gets lost)
     *
     * @param node - input JSP element node to convert
     * @param context - supplier of current taglib and function converters
     * @return
     */
    List<Content> process(JSPParser.JspElementContext node, JSPElementNodeConverter context);

    /**
     * Convert this element into a new attribute value.
     *
     * An example is the <c:url> element which gets transformed as follows:
     *
     * <a href="<c:url value="some/path"/>">A Link</a>
     *
     * <a href="@{some/path}">A Link</a>
     *
     * @param node
     * @return a String value to insert as an attribute
     */
    String processAsAttributeValue(JSPParser.HtmlQuotedElementContext node, JSPElementNodeConverter context);

    boolean canHandle(JSPParser.JspElementContext node);

    ScopedJSPConverters getScopedConverters();

    void setScopedConverters(ScopedJSPConverters scopedConverters);

}
