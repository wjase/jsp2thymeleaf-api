/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.expressions;

import java.io.Writer;
import org.apache.commons.el.Expression;

/**
 *
 * @author jason
 */
public interface ExpressionConverterContext
{

    public void writeExpression(Writer writer,
            Expression invocation,
            ExpressionConverterContext converter);
}
