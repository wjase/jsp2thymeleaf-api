/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api;

import com.cybernostics.forks.jsp2x.JspTree;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author jason
 */
public abstract class TagConverterSource implements ConverterSource
{

    private Map<String, JspTreeConverter> converterMap = new HashMap<>();
    private String uri;

    public TagConverterSource(String forUri, TagConverter... converters)
    {
        this.uri = forUri;
        for (TagConverter converter : converters)
        {
            add(converter);
        }
    }

    /**
     * Returns the conventional URI used to match this taglib.
     *
     * @return
     */
    public String getTaglibURI()
    {
        return uri;
    }

    public Optional<JspTreeConverter> converterFor(DomTag domTag)
    {
        return Optional.ofNullable(converterMap.get(domTag.getTagname()));
    }

    @Override
    public Optional<JspTreeConverter> converterFor(JspTree jspTree)
    {
        DomTag domTag = new DomTag(jspTree.name());
        return converterFor(domTag);
    }

    public void add(TagConverter converter)
    {
        converterMap.put(converter.getApplicableTag(), converter);
    }
}
