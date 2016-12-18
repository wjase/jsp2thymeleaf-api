/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.util;

/**
 *
 * @author jason
 */
public class StringFunctions
{

    public static String ucFirst(String s)
    {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    static String stripEL(String s)
    {
        if (!s.startsWith("${"))
        {
            return s;
        }
        if (s.equals("${}"))
        {
            return "";
        }
        return s.substring(2, s.length() - 1);
    }
}
