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
public class DefaultFileErrorLocation extends DefaultStreamErrorLocation implements FileErrorLocation
{

    private String filename;

    public DefaultFileErrorLocation(String filename, int line, int column)
    {
        super(line, column);
        this.filename = filename;
    }

    @Override
    public String getFilename()
    {
        return this.filename;
    }

}
