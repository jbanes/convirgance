/*
 * Copyright 2024 INVIRGANCE LLC

Permission is hereby granted, free of charge, to any person obtaining a copy 
of this software and associated documentation files (the “Software”), to deal 
in the Software without restriction, including without limitation the rights to 
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
of the Software, and to permit persons to whom the Software is furnished to do 
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all 
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
SOFTWARE.
 */
package com.invirgance.convirgance.json;

import com.invirgance.convirgance.ConvirganceException;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jbanes
 */
public class JSONObjectTest
{
    @Test
    public void testContains()
    {
        JSONObject record = new JSONObject();
        
        assertTrue(record.isEmpty());
        assertTrue(record.isNull("string"));
        assertTrue(record.isNull("null"));
        assertFalse(record.containsKey("string"));
        assertFalse(record.containsKey("null"));
        assertFalse(record.containsValue("Hello world!"));
        
        record.put("string", "Hello world!");
        record.put("null", null);
        
        assertFalse(record.isEmpty());
        assertFalse(record.isNull("string"));
        assertTrue(record.isNull("null"));
        assertTrue(record.containsKey("string"));
        assertTrue(record.containsKey("null"));
        assertTrue(record.containsValue("Hello world!"));
        
        record.clear();
        
        assertTrue(record.isEmpty());
        assertTrue(record.isNull("string"));
        assertTrue(record.isNull("null"));
        assertFalse(record.containsKey("string"));
        assertFalse(record.containsKey("null"));
        assertFalse(record.containsValue("Hello world!"));
    }
    
    @Test
    public void testBooleans()
    {
        JSONObject record = new JSONObject();
        
        record.put("boolean", true);
        record.put("booleanString", "true");
        record.put("booleanNull", null);
        record.put("notBoolean", 1337);
        
        assertTrue((Boolean)record.get("boolean"));
        assertTrue(record.getBoolean("boolean"));
        assertTrue(record.getBoolean("booleanString"));
        assertTrue(record.getBoolean("boolean", false));
        assertTrue(record.getBoolean("booleanString", false));
        assertTrue(record.getBoolean("booleanNull", true));
        assertNull(record.get("booleanNull"));
        
        try 
        {
            record.getBoolean("booleanNull");
            fail("Expected failure on null lookup");
        } 
        catch(ConvirganceException e) { assertEquals("booleanNull is null and therefore can't be converted to a boolean", e.getMessage()); }
        
        try 
        {
            record.getBoolean("notBoolean");
            fail("Expected failure on lookup of non-boolean value");
        } 
        catch(ConvirganceException e) { assertEquals("Class type of java.lang.Integer for notBoolean cannot be converted to a boolean", e.getMessage()); }
        
        try 
        {
            record.getBoolean("notBoolean", false);
            fail("Expected failure on lookup of non-boolean value");
        } 
        catch(ConvirganceException e) { assertEquals("Class type of java.lang.Integer for notBoolean cannot be converted to a boolean", e.getMessage()); }
    }
    
    @Test
    public void testInts()
    {
        JSONObject record = new JSONObject();
        
        record.put("integer", 1337);
        record.put("integerString", "1337");
        record.put("integerNull", null);
        record.put("notInteger", false);
        
        assertEquals(1337, (Integer)record.get("integer"));
        assertEquals(1337, record.getInt("integer"));
        assertEquals(1337, record.getInt("integerString"));
        assertEquals(1337, record.getInt("integer", 7331));
        assertEquals(1337, record.getInt("integerString", 7331));
        assertEquals(1337, record.getInt("integerNull", 1337));
        assertNull(record.get("booleanNull"));
        
        try 
        {
            record.getInt("integerNull");
            fail("Expected failure on null lookup");
        } 
        catch(ConvirganceException e) { assertEquals("integerNull is null and therefore can't be converted to an int", e.getMessage()); }
        
        try 
        {
            record.getInt("notInteger");
            fail("Expected failure on lookup of non-boolean value");
        } 
        catch(ConvirganceException e) { assertEquals("Class type of java.lang.Boolean for notInteger cannot be converted to an int", e.getMessage()); }
        
        try 
        {
            record.getInt("notInteger", 1337);
            fail("Expected failure on lookup of non-boolean value");
        } 
        catch(ConvirganceException e) { assertEquals("Class type of java.lang.Boolean for notInteger cannot be converted to an int", e.getMessage()); }
    }
    
    @Test
    public void testStrings()
    {
        JSONObject record = new JSONObject();
        
        record.put("string", "Hello world!");
        record.put("number", 1337);
        record.put("double", 3.14159);
        record.put("boolean", true);
        record.put("null", null);
        
        assertEquals("Hello world!", record.get("string"));
        assertEquals("Hello world!", record.getString("string"));
        assertEquals("1337", record.getString("number"));
        assertEquals("3.14159", record.getString("double"));
        assertEquals("true", record.getString("boolean"));
        assertNull(record.getString("null"));
        
        assertEquals("Hello world!", record.getString("string", "Hello world!"));
        assertEquals("1337", record.getString("number", "Hello world!"));
        assertEquals("3.14159", record.getString("double", "Hello world!"));
        assertEquals("true", record.getString("boolean", "Hello world!"));
        assertEquals("Hello world!", record.getString("null", "Hello world!"));
        assertNull(record.getString("null"));
    }
            
    @Test
    public void testToString() throws Exception
    {
        JSONObject record = new JSONObject();
        
        record.put("string", "Hello world!");
        record.put("number", 1337);
        record.put("double", 3.14159);
        record.put("boolean", true);
        record.put("null", null);

        assertEquals("{\"number\":1337,\"boolean\":true,\"string\":\"Hello world!\",\"null\":null,\"double\":3.14159}", record.toString());
        assertEquals("{\n    \"number\": 1337,\n    \"boolean\": true,\n    \"string\": \"Hello world!\",\n    \"null\": null,\n    \"double\": 3.14159\n}", record.toString(4));
    }
    
    @Test
    public void testFromString() throws Exception
    {
        JSONObject record = new JSONObject("{\"number\":1337,\"boolean\":true,\"string\":\"Hello world!\",\"null\":null,\"double\":3.14159}");
        
        assertEquals(5, record.size());
        assertEquals("Hello world!", record.get("string"));
        assertEquals(1337, record.get("number"));
        assertEquals(3.14159, record.get("double"));
        assertTrue(record.getBoolean("boolean"));
        assertNull(record.get("null"));
    }
    
    @Test
    public void testCopy() throws Exception
    {
        JSONObject record = new JSONObject("{\"number\":1337,\"boolean\":true,\"string\":\"Hello world!\",\"null\":null,\"double\":3.14159}");
        
        assertEquals(5, record.size());
        assertEquals("Hello world!", record.get("string"));
        assertEquals(1337, record.get("number"));
        assertEquals(3.14159, record.get("double"));
        assertTrue(record.getBoolean("boolean"));
        assertNull(record.get("null"));
        
        record = new JSONObject(record);
                
        assertEquals(5, record.size());
        assertEquals("Hello world!", record.get("string"));
        assertEquals(1337, record.get("number"));
        assertEquals(3.14159, record.get("double"));
        assertTrue(record.getBoolean("boolean"));
        assertNull(record.get("null"));
    }
    
    @Test
    public void testOrdered()
    {
        JSONObject record = new JSONObject(true);
        JSONObject record2;
        int index = 1;
        
        record.setOrdered(true);
        
        record.put("Item 1", 1);
        record.put("Item 2", 2);
        record.put("Item 3", 3);
        record.put("Item 4", 4);
        record.put("Item 5", 5);
        
        for(String key : record.keySet())
        {
            assertEquals("Item " + index, key);
            assertEquals(index, record.get(key));
            
            index++;
        }
        
        record.remove("Item 2");
        record.remove("Item 1");
        
        index = 3;
        
        for(String key : record.keySet())
        {
            assertEquals("Item " + index, key);
            assertEquals(index, record.get(key));
            
            index++;
        }
        
        // Validate that ordering carries from object to object
        record2 = new JSONObject(record);
        
        record.setOrdered(false);
        
        assertFalse(record.isOrdered());
        
        index = 3;
        
        for(String key : record2.keySet())
        {
            assertEquals("Item " + index, key);
            assertEquals(index, record2.get(key));
            
            index++;
        }
    }
}
