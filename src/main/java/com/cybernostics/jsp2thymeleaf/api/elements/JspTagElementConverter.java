/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import com.cybernostics.jsp.parser.JSPLexer;
import com.cybernostics.jsp.parser.JSPParser;
import com.cybernostics.jsp.parser.JSPParser.JspElementContext;
import static com.cybernostics.jsp2thymeleaf.api.elements.ScopedHtmlQuotedElementNodeContext.forNode;
import static com.cybernostics.jsp2thymeleaf.api.elements.ScopedJspElementNodeContext.forJspNode;
import static com.cybernostics.jsp2thymeleaf.api.util.SetUtils.setOf;
import com.cybernostics.jsp2thymeleaf.api.util.SimpleStringTemplateProcessor;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.collections.ListUtils;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Text;

public class JspTagElementConverter extends CopyElementConverter implements TagConverter
{

    public static final Namespace TH = Namespace.getNamespace("th", "http://www.thymeleaf.org");
    public static final Namespace CN = Namespace.getNamespace("cn", "http://www.cybernostics.com");
    public static final Namespace XMLNS = Namespace.getNamespace("http://www.w3.org/1999/xhtml");

    protected String appliesTo;
    protected String convertedElementName;
    private Namespace newNamespace = TH;
    protected Set<String> attributesToRemove = setOf();
    protected List<NewAttributeBuilder> newAttributeBuilders = new ArrayList<>();
    protected String newTextContent = "";
    private Optional<Function<ScopedHtmlQuotedElementNodeContext, String>> asAttributeConverter = Optional.empty();
    private Optional<Function<ScopedJspElementNodeContext, Map<String, String>>> childAttributeSource = Optional.empty();

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

    public static JspTagElementConverter ignore(String tagName)
    {
        return new JspTagElementConverter(tagName)
        {
            @Override
            public List<Content> process(JspElementContext node, JSPElementNodeConverter context)
            {
                final Element element = new Element("deleteme");
                return Arrays.asList(element);
            }

        };
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

    public JspTagElementConverter withChildElementAtributes(Function<ScopedJspElementNodeContext, Map<String, String>> attSource)
    {
        childAttributeSource = Optional.of(attSource);
        return this;
    }

    public JspTagElementConverter whenQuoted(Function<ScopedHtmlQuotedElementNodeContext, String> asAttributeConverter)
    {
        this.asAttributeConverter = Optional.of(asAttributeConverter);
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
    protected List<Content> getNewChildContent(JSPParser.JspElementContext node, JSPElementNodeConverter context)
    {
        if (newTextContent.length() == 0)
        {
            return super.getNewChildContent(node, context);
        }
        Map<String, Object> attMap = getAttributeMap(node, context);
        return Arrays.asList(new Text(SimpleStringTemplateProcessor.generate(newTextContent, attMap)));
    }

    @Override
    public boolean canHandle(JSPParser.JspElementContext node)
    {
        return node.name.getText().equals(getApplicableTag());
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
                -> currentValues.containsKey(oldName)
                ? Arrays.asList(new Attribute(newName,
                        currentValues.get(oldName).toString(), namespace))
                : ListUtils.EMPTY_LIST);
        return this;
    }

    @Override
    protected List<Attribute> getAttributes(JspElementContext node, JSPElementNodeConverter context)
    {
        final Map<String, String> emptyMap = new HashMap<>();
        Map<String, Object> attMap = new HashMap<>();
        final List<Attribute> sourceAtributes = super.getAttributes(node, context);
        final List<Attribute> attributes = sourceAtributes
                .stream()
                .filter((eachAttribute) ->
                {
                    attMap.put(eachAttribute.getName(), eachAttribute.getValue());
                    return !attributesToRemove.contains(eachAttribute.getName());
                }).collect(Collectors.toList());

        Map<String, String> childMap = childAttributeSource
                .map(i -> i.apply(forJspNode(node, context)))
                .orElse(emptyMap);

        if (!childMap.isEmpty())
        {
            attMap.put("_childAtts", childMap);
        }

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
    protected Namespace newNamespaceForElement(JSPParser.JspElementContext node)
    {
        return newNamespace;
    }

    @Override
    protected String newNameForElement(JSPParser.JspElementContext node)
    {
        return convertedElementName;
    }

    @Override
    public String getApplicableTag()
    {
        return appliesTo;
    }

    protected Map<String, Object> getAttributeMap(JspElementContext node, JSPElementNodeConverter context)
    {
        return super.getAttributes(node, context)
                .stream()
                .collect(Collectors.toMap((item) -> item.getName(), (item) -> item.getValue()));

    }

    @Override
    public String processAsAttributeValue(JSPParser.JspQuotedElementContext node, JSPElementNodeConverter context)
    {
        try
        {
            String elementText = node.getText();
            JSPLexer jspLexer = new JSPLexer(new org.antlr.v4.runtime.ANTLRInputStream(new ByteArrayInputStream(elementText.getBytes())));
            CommonTokenStream tokens = new CommonTokenStream(jspLexer);
            // Pass the tokens to the parser
            JSPParser parser = new JSPParser(tokens);
            JSPParser.JspDocumentContext documentContext = parser.jspDocument();
            JspElementContext jspElement = (JspElementContext) documentContext.children.get(1).getChild(0);
            List<Content> content = process(jspElement, context);
            System.out.println("Quoted content converted to:");
            System.out.println(content.stream().map(o -> o.toString()).collect(Collectors.joining()));
        } catch (Throwable t)
        {
            System.out.println("badbadbad");
            t.printStackTrace();
        }

        return asAttributeConverter
                .orElseThrow(() -> new UnsupportedOperationException("Element conversion not supported in attribute value context:" + node.toString()))
                .apply(forNode(node, context));
    }

}
