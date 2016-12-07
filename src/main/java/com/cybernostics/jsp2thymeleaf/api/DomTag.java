/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api;

/**
 *
 * @author jason
 */
public class DomTag
{

    private String prefix;

    private String tagname;
    
    public DomTag(String wholeTag){
        final String[] parts = wholeTag.split(":");
        if(parts.length>1){
            this.prefix=parts[0];
            this.tagname = parts[1];
        }else{
            this.prefix="";
            this.tagname=parts[0];
        }
    }
    
    public DomTag(String prefix,String tagname){
        this.prefix = prefix;
        this.tagname = tagname;
        
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getTagname()
    {
        return tagname;
    }

    @Override
    public String toString()
    {
        return prefix+":"+tagname;
    }
    
    
    
}
