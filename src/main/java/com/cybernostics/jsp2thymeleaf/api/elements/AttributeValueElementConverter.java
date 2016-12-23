/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import com.cybernostics.forks.jsp2x.JspTree;
import static com.cybernostics.jsp2thymeleaf.api.util.JspTreeUtils.getAttribute;
import com.cybernostics.jsp2thymeleaf.api.util.PrefixedName;
import static com.cybernostics.jsp2thymeleaf.api.util.PrefixedName.prefixedNameFor;
import java.util.Optional;
import org.jdom2.Attribute;

/**
 *
 * @author jason
 */
public class AttributeValueElementConverter
{

    Attribute transform(JspTree jspTreeAttribute)
    {
        String attName = jspTreeAttribute.name();
        final JspTree treeValue = jspTreeAttribute.treeValue();
        PrefixedName name = prefixedNameFor(treeValue.name());
        if (name.getPrefix().equals("c"))
        {
            if (name.getName().equals("url"))
            {
                Optional<JspTree> value = getAttribute(treeValue, "value");
                return new Attribute(attName, "@{" + value.get().treeValue() + "}");
            }
        }
        return new Attribute(attName, "Unknown tag:" + jspTreeAttribute.treeValue().toStringTree());

    }

}
