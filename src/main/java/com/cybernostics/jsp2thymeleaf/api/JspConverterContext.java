/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api;

import com.cybernostics.forks.jsp2x.JspTree;
import java.util.List;
import org.jdom2.Content;

/**
 *
 * @author jason
 */
public interface JspConverterContext
{
    List<Content> contentFor(JspTree eachChild, JspConverterContext context);
    
}
