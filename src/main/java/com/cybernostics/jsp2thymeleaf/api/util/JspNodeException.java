/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.util;

import com.cybernostics.forks.jsp2x.JspTree;

/**
 *
 * @author jason
 */
public class JspNodeException extends RuntimeException implements HasLocationInStream
{

    private JspTree jspTree;

    public JspNodeException()
    {
    }

    public JspNodeException(String message, JspTree jspTree)
    {
        super(message);
        this.jspTree = jspTree;
    }

    public JspNodeException(Throwable t, JspTree jspTree)
    {
        super(t);
        this.jspTree = jspTree;
    }

    public JspTree getJspTree()
    {
        return jspTree;
    }

    @Override
    public StreamErrorLocation getLocation()
    {
        return new DefaultStreamErrorLocation(jspTree.getLine(), jspTree.getCharPositionInLine());
    }

}
