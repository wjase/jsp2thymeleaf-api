/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.jsp2thymeleaf.api.util;

import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author jason
 */
public class ExpressionStringTemplateTest
{

    public ExpressionStringTemplateTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of generate method, of class SimpleStringTemplateProcessor.
     */
    @org.junit.Test
    public void testGenerate_String_Map()
    {
        System.out.println("generate");
        String inputFormat = "%{key2!stripEL}%{key1}%{key1!ucFirst}";
        Map<String, String> values = new HashMap<>();
        values.put("key1", "value1");
        values.put("key2", "${value2}");

        String expResult = "value2value1Value1";
        String result = SimpleStringTemplateProcessor.generate(inputFormat, values);
        assertThat(result, is(expResult));
    }

}
