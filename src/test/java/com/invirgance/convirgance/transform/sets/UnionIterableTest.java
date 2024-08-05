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
package com.invirgance.convirgance.transform.sets;

import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jbanes
 */
public class UnionIterableTest
{
    @Test
    public void testUnionNone()
    {
        UnionIterable union = new UnionIterable();
        
        assertFalse(union.iterator().hasNext());
    }
    
    @Test
    public void testUnionSingle()
    {
        JSONArray<JSONObject> array = new JSONArray<>("[{\"num\":0},{\"num\":1}]");
        UnionIterable union = new UnionIterable(array);
        int index = 0;
        
        assertTrue(union.iterator().hasNext());
        
        for(JSONObject record : union)
        {
            assertEquals(1, record.size());
            assertEquals(index++, record.get("num"));
        }
        
        assertEquals(2, index);
    }
    
    @Test
    public void testUnionTwo()
    {
        JSONArray<JSONObject> array1 = new JSONArray<>("[{\"num\":0},{\"num\":1}]");
        JSONArray<JSONObject> array2 = new JSONArray<>("[{\"num\":2},{\"num\":3}]");
        UnionIterable union = new UnionIterable(array1, array2);
        int index = 0;
        
        assertTrue(union.iterator().hasNext());
        
        for(JSONObject record : union)
        {
            assertEquals(1, record.size());
            assertEquals(index++, record.get("num"));
        }
        
        assertEquals(4, index);
    }
    
    @Test
    public void testUnionThree()
    {
        JSONArray<JSONObject> array1 = new JSONArray<>("[{\"num\":0},{\"num\":1}]");
        JSONArray<JSONObject> array2 = new JSONArray<>("[]");
        JSONArray<JSONObject> array3 = new JSONArray<>("[{\"num\":2},{\"num\":3}]");
        UnionIterable union = new UnionIterable(array1, array2, array3);
        int index = 0;
        
        assertTrue(union.iterator().hasNext());
        
        for(JSONObject record : union)
        {
            assertEquals(1, record.size());
            assertEquals(index++, record.get("num"));
        }
        
        assertEquals(4, index);
    }
    
}
