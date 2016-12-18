/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * ActiveTaglibConverters keeps track of taglib converters which have been
 * declared with jsp taglib directives, and the prefixes with which they are
 * associated.
 *
 * As the taglib is encountered, this is used to register the service.
 *
 * The forUri method is then used when tags with a given (or no) prefix are
 * encountered.
 *
 * @author wjase
 */
public class ActiveTaglibConverters
{

    private static Map<String, JspTreeConverterSource> activeTagConverters = new HashMap<>();

    /**
     * Registers a given converter for handling tags with the prefix specified
     *
     * @param prefix
     * @param converterSource
     */
    public static void addTaglibConverter(String prefix, JspTreeConverterSource converterSource)
    {
        activeTagConverters.putIfAbsent(prefix, converterSource);
    }

    /**
     * Return the converter for a given prefix.
     *
     * @param prefix
     * @return
     */
    public static Optional<JspTreeConverterSource> forPrefix(String prefix)
    {
        return Optional.ofNullable(activeTagConverters.getOrDefault(prefix, null));
    }
}
