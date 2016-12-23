/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import com.cybernostics.forks.jsp2x.JspTree;
import static com.cybernostics.jsp2thymeleaf.api.util.SetUtils.setOf;
import com.cybernostics.jsp2thymeleaf.api.util.SimpleStringTemplateProcessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.ListUtils;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Namespace;
import org.jdom2.Text;

public class JspTagElementConverter extends CopyElementConverter implements TagConverter
{

    public static final Namespace TH = Namespace.getNamespace("th", "http://www.thymeleaf.org");
    public static final Namespace CN = Namespace.getNamespace("cn", "http://www.cybernostics.com");
    public static final Namespace XMLNS = Namespace.getNamespace("http://www.w3.org/1999/xhtml");

    protected final ELExpressionConverter exprConverter = new ELExpressionConverter();

    protected String appliesTo;
    protected String convertedElementName;
    private Namespace newNamespace = TH;
    protected Set<String> attributesToRemove = setOf();
    protected List<NewAttributeBuilder> newAttributeBuilders = new ArrayList<>();
    protected String newTextContent = "";

    public JspTagElementConverter()
    {
    }

    public JspTagElementConverter(String appliesTo)
    {
        this(appliesTo, "");
    }

    public JspTagElementConverter(String appliesTo, String convertedElementName)
    {
        this.appliesTo = appliesTo;
        this.convertedElementName = convertedElementName;
    }

    public static JspTagElementConverter converterFor(String tagName)
    {

        return new JspTagElementConverter(tagName);
    }

    public JspTagElementConverter withNewName(String name, Namespace namespace)
    {
        convertedElementName = name;
        newNamespace = namespace;
        return this;
    }

    public JspTagElementConverter withNewName(String name)
    {
        convertedElementName = name;
        return this;
    }

    public JspTagElementConverter withNewTextContent(String contentFormat)
    {
        newTextContent = contentFormat;
        return this;
    }

    @Override
    protected List<Content> getChildContent(JspTree jspTree, JspTreeConverterContext context)
    {
        if (newTextContent.length() == 0)
        {
            return super.getChildContent(jspTree, context);
        }
        return Arrays.asList(new Text(SimpleStringTemplateProcessor.generate(newTextContent, getAttributeMap(jspTree))));
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

    public JspTagElementConverter addsAttributes(NewAttributeBuilder... attributeBuilder)
    {
        newAttributeBuilders.addAll(Arrays.asList(attributeBuilder));
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
        final List<Attribute> sourceAtributes = super.getAttributes(jspTree);
        final List<Attribute> attributes = sourceAtributes
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
        return newNamespace;
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

    protected Map<String, String> getAttributeMap(JspTree jspTree)
    {
        return super.getAttributes(jspTree)
                .stream()
                .collect(Collectors.toMap((item) -> item.getName(), (item) -> item.getValue()));

    }

}
