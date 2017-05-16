/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import com.cybernostics.jsp.parser.JSPParser;
import com.cybernostics.jsp2thymeleaf.api.util.PrefixedName;
import java.util.Optional;

/**
 *
 * @author jason
 */
class QuotedElementConverter
{

    String convert(JSPParser.HtmlQuotedElementContext quotedElementContext, JSPElementNodeConverter converterContext)
    {
        final ScopedJSPConverters scopedConverters = converterContext.getScopedConverters();
        final PrefixedName prefixedName = PrefixedName.prefixedNameFor(quotedElementContext.name.getText());
        final Optional<JSPNodeConverterSource> converter = scopedConverters.forPrefix(prefixedName.getPrefix());
        return converter.get().converterFor(quotedElementContext).get().processAsAttributeValue(quotedElementContext, converterContext);
    }

}
