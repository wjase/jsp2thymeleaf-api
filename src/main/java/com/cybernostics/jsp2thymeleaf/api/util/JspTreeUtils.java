/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.util;

import com.cybernostics.forks.jsp2x.JspTree;
import static com.cybernostics.jsp2thymeleaf.api.util.PrefixedName.prefixedNameFor;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 *
 * @author jason
 */
public class JspTreeUtils
{

    public static void doWithChildren(JspTree parent, BiConsumer<Integer, JspTree> func)
    {
        for (int i = 0; i < parent.getChildCount(); i++)
        {
            JspTree child = parent.getChild(i);
            func.accept(i, child);
        }
    }

    public static void doWithAttributes(JspTree parent, BiConsumer<Integer, JspTree> func)
    {
        final JspTree attributesNode = parent.attributes();
        for (int i = 0; i < attributesNode.getChildCount(); i++)
        {
            JspTree child = attributesNode.getChild(i);
            func.accept(i, child);
        }
    }

    public static String elEscape(String input)
    {
        return String.format("${%s}", input);
    }

    public static Optional<JspTree> getAttribute(JspTree jtElement, String attributeName)
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

    public static PrefixedName tagFor(JspTree jspTree)
    {
        return prefixedNameFor(nameOrNone(jspTree));
    }

    public static String nameOrNone(JspTree jspTree)
    {
        return jspTree.getChildCount() > 0 ? jspTree.name() : "no name";
    }
}
