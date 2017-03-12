/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import com.cybernostics.jsp.parser.JSPParser;
import com.cybernostics.jsp.parser.JSPParser.HtmlAttributeContext;
import com.cybernostics.jsp.parser.JSPParser.JspElementContext;
import static com.cybernostics.jsp2thymeleaf.api.common.Namespaces.XMLNS;
import com.cybernostics.jsp2thymeleaf.api.util.PrefixedName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import static org.apache.commons.collections.ListUtils.EMPTY_LIST;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 *
 * @author jason
 */
public class CopyElementConverter implements JSPElementNodeConverter
{

    protected ELExpressionConverter expressionConverter = new ELExpressionConverter();

    private ScopedJSPConverters scopedConverters;

    public void setScopedConverters(ScopedJSPConverters scopedConverters)
    {
        this.scopedConverters = scopedConverters;
    }

    @Override
    public List<Content> process(JSPParser.JspElementContext node, JSPElementNodeConverter context)
    {
        ActiveNamespaces.add(newNamespaceForElement(node));
        Optional<Element> maybeElement = createElement(node, context);
        if (maybeElement.isPresent())
        {
            Element element = maybeElement.get();
            element.removeNamespaceDeclaration(XMLNS);
            element.addContent(getNewChildContent(node, context));
            addAttributes(element, node, context);
            return Arrays.asList(element);
        }

        return EMPTY_LIST;
    }

    protected List<Content> getNewChildContent(JSPParser.JspElementContext node, JSPElementNodeConverter context)
    {
        return EMPTY_LIST;
    }

    protected List<Attribute> getAttributes(JspElementContext jspNode, JSPElementNodeConverter context)
    {
        List<Attribute> attributes = new ArrayList<>();

        doWithAttributes(jspNode, (i, eachAtt) -> attributes.add(createAttribute(eachAtt, context).get()));
        return attributes;
    }

    protected void doWithAttributes(JspElementContext jspNode, BiConsumer<Integer, HtmlAttributeContext> toDo)
    {
        final List<HtmlAttributeContext> atts = jspNode.atts;
        for (int i = 0; i < atts.size(); i++)
        {
            toDo.accept(i, atts.get(i));
        }
    }

    protected void addAttributes(Element parent, JspElementContext node, JSPElementNodeConverter context)
    {
        getAttributes(node, context).stream().forEach((entry) -> parent.setAttribute(entry));
    }

    protected String attributeNameFor(JSPParser.JspElementContext node)
    {
        return node.name.getText();
    }

    protected String valueFor(JSPParser.HtmlAttributeContext att)
    {
        return att.value.toStringTree();

    }

    protected Optional<Element> createElement(JspElementContext node, JSPElementNodeConverter context)
    {
        final Element element = new Element(newNameForElement(node));
        element.removeNamespaceDeclaration(element.getNamespace());
        element.setNamespace(newNamespaceForElement(node));
        return Optional.of(element);
    }

    protected String newNameForElement(JSPParser.JspElementContext node)
    {
        return nameOrNone(node);
    }

    protected Optional<Attribute> createAttribute(HtmlAttributeContext jspNodeAttribute, JSPElementNodeConverter context)
    {
        final JSPParser.JspElementContext jspElement = jspNodeAttribute.jspElement();
        if (jspElement != null)
        {
            // do something here
            throw new UnsupportedOperationException("element converter needed here.TODO");
        }

        final JSPParser.HtmlAttributeValueContext value = jspNodeAttribute.value;
        String attributeValueText = "";

        if (value != null)
        {
            final JSPParser.HtmlAttributeValueExprContext exprValue = jspNodeAttribute.value.htmlAttributeValueExpr();
            if (exprValue != null)
            {
                if (context == null)
                {
                    throw new RuntimeException("Cannot convert expression - null context:" + exprValue.getText());
                }
                attributeValueText = expressionConverter.convert(exprValue.getText(), context.getScopedConverters());
            } else
            {
                final JSPParser.HtmlAttributeValueConstantContext constant = jspNodeAttribute.value.htmlAttributeValueConstant();
                if (constant != null)
                {
                    attributeValueText = constant.getText();
                }

            }

        } else
        {
            attributeValueText = jspNodeAttribute.name.getText();
        }

        return Optional.of(new Attribute(jspNodeAttribute.name.getText(), attributeValueText));

    }

    @Override
    public boolean canHandle(JSPParser.JspElementContext jspNode
    )
    {
        return true;
    }

    protected Namespace newNamespaceForElement(JSPParser.JspElementContext jspNode)
    {
        final PrefixedName prefixedNameFor = PrefixedName.prefixedNameFor(jspNode.name.getText());
        if (!prefixedNameFor.getPrefix().isEmpty())
        {
            final Namespace namespace = Namespace.getNamespace(prefixedNameFor.getPrefix(), "UNKNOWN_PREFIX_" + prefixedNameFor.getPrefix());
            ActiveNamespaces.add(namespace);
            return namespace;
        }

        return XMLNS;
    }

    protected Namespace attributeNamespaceFor(JSPParser.JspElementContext eachAtt)
    {
        return XMLNS;
    }

    @Override
    public ScopedJSPConverters getScopedConverters()
    {
        return scopedConverters;
    }

    private String nameOrNone(JspElementContext node)
    {
        final PrefixedName prefixedNameFor = PrefixedName.prefixedNameFor(node.name.getText());

        return prefixedNameFor.getName();
    }

}
