/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.exception;

import com.cybernostics.jsp.parser.JSPParser;
import com.cybernostics.jsp2thymeleaf.api.common.TokenisedFile;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 *
 * @author jason
 */
public class JSPNodeException extends JSP2ThymeLeafException implements MutableFileLocation
{

    private ParserRuleContext jspNode;
    private TokenisedFile file;

    public TokenisedFile getFile()
    {
        return file;
    }

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
        super(message);
        this.jspNode = jspNode;
    }

    public String getMessage()
    {
        return super.getMessage() + " " + getLocation().toString();
    }

    @Override
    public FileErrorLocation getLocation()
    {
        return new DefaultFileErrorLocation(file != null ? file.getRelativePathString() : "unknown", jspNode.start.getLine(), jspNode.start.getCharPositionInLine());
    }

    @Override
    public void setFile(TokenisedFile file)
    {
        this.file = file;
    }

}
