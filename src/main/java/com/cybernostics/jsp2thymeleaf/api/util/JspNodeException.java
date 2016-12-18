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
public class JspNodeException extends RuntimeException
{

    private JspTree jspTree;

    public JspNodeException()
    {
    }

    public JspNodeException(String message, JspTree jspTree)
    {
        super(message + String.format("( Line: %d, Column:%d )", jspTree.getLine(), jspTree.getCharPositionInLine()));
        this.jspTree = jspTree;
    }

    public JspTree getJspTree()
    {
        return jspTree;
    }

}
