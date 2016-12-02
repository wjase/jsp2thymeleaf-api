/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api;

import com.cybernostics.forks.jsp2x.JspTree;
import java.util.Arrays;
import java.util.List;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 *
 * @author jason
 */
public class ElementConverter implements JspTreeConverter
{
    private final Namespace xmlns = Namespace.getNamespace("http://www.w3.org/1999/xhtml");

    public List<Content> elementContentFor(JspTree jspTree, JspConverterContext context)
    {
        Optional<Element> maybeElement = createElement(jspTree);
        if(maybeElement.isPresent()){
            Element element = maybeElement.get();
            element.removeNamespaceDeclaration(xmlns);
            element.addContent(getChildContent(jspTree, context));
            addAttributes(element, jspTree);
            return Arrays.asList(element);
        }

        return getChildContent(jspTree, context);
    }
    
    public static String nameOrNone(JspTree jspTree)
    {
        return jspTree.getChildCount() > 0 ? jspTree.name() : "no name";
    }
    
    protected List<Content> getChildContent(JspTree jspTree, JspConverterContext context){
        List<Content> childContent = new ArrayList<>();
        JspTreeUtils.doWithChildren(jspTree, (i, eachChild) ->
        {
            // child 0 is the name jspTree
            // child 1 is the ATTRIBUTES jspTree
            if (i > 1)
            {
                childContent.addAll(context.contentFor(eachChild, context));
            }
        });
        return childContent;
    }
    
    protected List<Attribute> getAttributes(JspTree jspTree){
        List<Attribute> attributes = new ArrayList<>();
        JspTreeUtils.doWithAttributes(jspTree, (i, eachAtt) -> attributes.add(createAttribute(eachAtt)));
        return attributes;
    }
    
    public Optional<JspTree> getAttribute(JspTree jtElement, String attributeName)
    {
        final JspTree attributes = jtElement.attributes();
        for (int i = 0; i < attributes.getChildCount(); i++)
        {
            JspTree jspTree = attributes.getChild(i);
            if (jspTree.name().equals(attributeName))
            {
                return Optional.of(jspTree);
            }
        }
        return Optional.empty();
    }

    protected void addAttributes(Element parent, JspTree jspTree){
        getAttributes(jspTree).stream().forEach((entry)->parent.setAttribute(entry));
    }
    
    protected String attributeNameFor(JspTree jspTree){
        return jspTree.name();
    }
    
    protected String valueFor(JspTree jspTree){
        return jspTree.treeValue().toStringTree();
        
    }

    protected Optional<Element> createElement(JspTree jspTree)
    {
        final Element element = new Element(newNameForElement(jspTree));
        element.removeNamespaceDeclaration(element.getNamespace());
        element.setNamespace(newNamespaceForElement(jspTree));
        return Optional.of(element);
    }
    
    protected String newNameForElement(JspTree jspTree){
        return nameOrNone(jspTree);
    }
    
    protected Attribute createAttribute(JspTree jspTreeAttribute){
        return new Attribute(jspTreeAttribute.name(), jspTreeAttribute.treeValue().toStringTree());
    }

    @Override
    public boolean canHandle(JspTree jspTree)
    {
        return true;
    }

    protected Namespace newNamespaceForElement(JspTree jspTree)
    {
        return xmlns;
    }

    protected Namespace attributeNamespaceFor(JspTree eachAtt)
    {
        return xmlns;
    }
    

}
