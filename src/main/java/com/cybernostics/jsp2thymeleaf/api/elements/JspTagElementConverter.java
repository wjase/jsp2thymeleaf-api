/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import com.cybernostics.forks.jsp2x.JspTree;
import static com.cybernostics.jsp2thymeleaf.api.util.SetUtils.setOf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.ListUtils;
import org.jdom2.Attribute;
import org.jdom2.Namespace;

public abstract class JspTagElementConverter extends CopyElementConverter implements TagConverter
{

    protected final Namespace thymeleafNS = Namespace.getNamespace("th", "http://www.thymeleaf.org");
    protected final Namespace xmlNS = Namespace.getNamespace("http://www.w3.org/1999/xhtml");
    protected Set<String> attributesToRemove = setOf();
    protected List<NewAttributeBuilder> newAttributeBuilders = new ArrayList<>();
    protected String appliesTo;
    protected String convertedElementName;
    protected ELExpressionConverter exprConverter = new ELExpressionConverter();

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
        newAttributeBuilders.add(attributeBuilder);
        return this;
    }

    public JspTagElementConverter renamesAttribute(String oldName, String newName, Namespace namespace)
    {
        removesAtributes(oldName);
        addsAttributes((currentValues)
                -> Arrays.asList(new Attribute(newName,
                        currentValues.get(oldName), namespace)));
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

        final List<Attribute> createdAttributes = newAttributeBuilders.stream()
                .flatMap(eachBuilder -> eachBuilder.buildNewAttributes(attMap).stream())
                .collect(Collectors.toList());

        for (Attribute createdAttribute : createdAttributes)
        {
            ActiveNamespaces.add(createdAttribute.getNamespace());
        }

        return ListUtils.union(createdAttributes, attributes);
    }

    @Override
    protected Namespace newNamespaceForElement(JspTree jspTree)
    {
        return thymeleafNS;
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
