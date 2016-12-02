/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api;

import com.cybernostics.forks.jsp2x.JspTree;
import com.cybernostics.jsp2thymeleaf.api.NewAttributeBuilder;
import com.cybernostics.jsp2thymeleaf.api.ElementConverter;
import static com.cybernostics.jsp2thymeleaf.api.SetUtils.setOf;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.ListUtils;
import org.jdom2.Attribute;
import org.jdom2.Namespace;

public abstract class JspTagElementConverter extends ElementConverter implements TagConverter
{

    protected final Namespace thymeleafNS = Namespace.getNamespace("th", "http://www.thymeleaf.org");
    protected final Namespace xmlNS = Namespace.getNamespace("http://www.w3.org/1999/xhtml");
    protected Set<String> attributesToRemove = setOf();
    protected NewAttributeBuilder newAttributeBuilder = new NewAttributeBuilder.NOPNewAttributeBuilder();
    protected final Namespace thNamespace = Namespace.getNamespace("th", "http://www.thymeleaf.org");
    protected String appliesTo;
    protected String convertedElementName;

    public JspTagElementConverter()
    {
    }

    public JspTagElementConverter(String appliesTo, String convertedElementName)
    {
        this.appliesTo = appliesTo;
        this.convertedElementName = convertedElementName;
    }

    @Override
    public boolean canHandle(JspTree jspTree)
    {
        return jspTree.name().equals(getApplicableTag());
    }

    public JspTagElementConverter removesAtributes(String... names)
    {
        attributesToRemove = setOf(names);
        return this;
    }

    public JspTagElementConverter addsAttributes(NewAttributeBuilder attributeBuilder)
    {
        newAttributeBuilder = attributeBuilder;
        return this;
    }

    @Override
    protected List<Attribute> getAttributes(JspTree jspTree)
    {
        Map<String, String> attMap = new HashMap<>();
        final List<Attribute> attributes = super.getAttributes(jspTree)
                .stream()
                .filter((eachAttribute) ->
                {
                    attMap.put(eachAttribute.getName(), eachAttribute.getValue());
                    return !attributesToRemove.contains(eachAttribute.getName());
                }).collect(Collectors.toList());

        return ListUtils.union(newAttributeBuilder.buildNewAttributes(attMap), attributes);
    }

    @Override
    protected Namespace newNamespaceForElement(JspTree jspTree)
    {
        return thNamespace;
    }

    @Override
    protected String newNameForElement(JspTree jspTree)
    {
        return convertedElementName;
    }

    @Override
    public String getApplicableTag()
    {
        return appliesTo;
    }

}
