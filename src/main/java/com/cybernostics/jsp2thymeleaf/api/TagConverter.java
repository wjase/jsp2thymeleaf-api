package com.cybernostics.jsp2thymeleaf.api;

import com.cybernostics.jsp2thymeleaf.api.JspTreeConverter;

/**
 *
 * @author jason
 */
public interface TagConverter extends JspTreeConverter
{
    /**
     * Returns the name of the tag this converter converts eg c:out
     * @return 
     */
    public String getApplicableTag();



}
