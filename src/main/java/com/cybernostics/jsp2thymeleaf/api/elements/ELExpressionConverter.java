///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package com.cybernostics.jsp2thymeleaf.api.elements;
///**
// *

import com.cybernostics.jsp2thymeleaf.api.expressions.ExpressionWalker;
import com.cybernostics.jsp2thymeleaf.api.expressions.ExpressionWritingVisitor;
import java.io.StringWriter;

// * @author jason
// *
public class ELExpressionConverter
{
//

    public String convert(String toConvert, ScopedJSPConverters converters)
    {
        ExpressionWalker ew = new ExpressionWalker(); //Logger.getLogger(ELExpressionConverter.class.getName()).log(Level.SEVERE, null, ex);
        final StringWriter stringWriter = new StringWriter();
        boolean isExpression = ew.walkExpressionString(toConvert, new ExpressionWritingVisitor(stringWriter, converters));
        return isExpression ? "${" + stringWriter.toString() + "}" : stringWriter.toString();

    }
//
}
