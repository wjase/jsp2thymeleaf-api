/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jason
 */
public class OptionalListComma
{

    public static <T> void join(Collection<T> items, Writer writer, String withString, BiConsumer<Writer, T> itemWrite)
    {
        int index = 0;
        int limit = items.size();
        for (T item : items)
        {
            itemWrite.accept(writer, item);
            if (index < limit - 1)
            {
                try
                {
                    writer.append(withString);
                } catch (IOException ex)
                {
                    Logger.getLogger(OptionalListComma.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            index++;
        }
    }
}
