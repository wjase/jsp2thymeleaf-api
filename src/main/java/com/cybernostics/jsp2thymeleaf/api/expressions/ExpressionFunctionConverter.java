/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.expressions;

/**
 *
 * @author jason
 */
public interface ExpressionFunctionConverter extends ExpressionConverterContext
{

    public String applicableFor();
}
