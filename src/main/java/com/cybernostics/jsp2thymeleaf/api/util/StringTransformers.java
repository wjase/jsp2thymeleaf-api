/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.util;

import java.lang.reflect.Method;
import java.util.Arrays;
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
        add(StringFunctions.class);
    }

    private static String[] args(String... args)
    {
        return args;
    }

    private static Function<String, String> supplierFor(Method method)
    {
        return (item) ->
        {
            try
            {
                return (String) method.invoke(null, args(item));
            } catch (Exception ex)
            {
                throw new RuntimeException(ex);
            }

        };
    }

    private StringTransformers()
    {
    }

    public static void add(Class<?> utilityClass)
    {
        Arrays.stream(utilityClass
                .getMethods())
                .filter(method -> method.getReturnType().equals(String.class)
                && method.getParameterCount() == 1
                && method.getParameterTypes()[0].equals(String.class)
                )
                .forEach(method -> add(method.getName(), supplierFor(method)));
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
