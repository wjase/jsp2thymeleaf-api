/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.util;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 *
 * @author jason
 */
public class StringTransformers
{

    private static Map<String, Function<String, String>> functionMap = new TreeMap<>();

    private static Function<String, String> identity = item -> item;

    static
    {
        add("ucfirst", StringFunctions::ucFirst);
        add("stripEL", StringFunctions::stripEL);
    }

    private StringTransformers()
    {
    }

    public static void add(String name, Function<String, String> fn)
    {
        functionMap.put(name, fn);
    }

    public static Function<String, String> get(String name)
    {
        return functionMap.getOrDefault(name, identity);
    }
}
