/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api;

import com.cybernostics.forks.jsp2x.JspTree;
import com.cybernostics.jsp2thymeleaf.api.JspConverterContext;
import java.util.List;
import java.util.Optional;
import org.jdom2.Content;

/**
 *
 * @author jason
 */
public interface JspTreeConverter
{
    List<Content> elementContentFor(JspTree jspTree, JspConverterContext context);
    boolean canHandle(JspTree jspTree);

    Optional<JspTree> getAttribute(JspTree jtElement, String attributeName);
    
}
