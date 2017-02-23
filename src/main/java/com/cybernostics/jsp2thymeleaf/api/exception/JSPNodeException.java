/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.exception;

import com.cybernostics.jsp.parser.JSPParser;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 *
 * @author jason
 */
public class JSPNodeException extends RuntimeException implements HasLocationInStream
{

    private ParserRuleContext jspNode;

    public ParserRuleContext getJspNode()
    {
        return jspNode;
    }

    public JSPNodeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public JSPNodeException(String message, JSPParser.JspDirectiveContext jspNode)
    {
        super(message);
        this.jspNode = jspNode;
    }

    public JSPNodeException(String message, JSPParser.JspElementContext jspNode)
    {
        this.jspNode = jspNode;
    }

    @Override
    public StreamErrorLocation getLocation()
    {
        return new DefaultStreamErrorLocation(jspNode.start.getLine(), jspNode.start.getCharPositionInLine());
    }

}
