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
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author jbanes
 */
public class JSONObject implements Map<String, Object>
{
    private final HashMap<String, Object> map;

    public JSONObject()
    {
        map = new HashMap<>();
    }

    public JSONObject(String json)
    {
        try
        {
            this.map = new JSONParser(json).parseObject().map;
        }
        catch(IOException e) { throw new ConvirganceException(e); }
    }
    
    public JSONObject(Map<String, Object> map)
    {
        this();
        
        map.putAll(map);
    }
    
    @Override
    public int size()
    {
        return this.map.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.map.isEmpty();
    }
    
    public boolean isNull(String key)
    {
        return (this.map.get(key) == null);
    }

    @Override
    public boolean containsKey(Object key)
    {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return this.map.containsValue(value);
    }

    @Override
    public Object get(Object key)
    {
        return this.map.get(key);
    }
    
    public boolean getBoolean(String key) throws ConvirganceException
    {
        Object value = this.map.get(key);
        
        if(value == null) throw new ConvirganceException(key + " is null and therefore can't be converted to a boolean");
        if(value instanceof Boolean) return ((Boolean)value);
        if(value instanceof String) return Boolean.parseBoolean(value.toString());
        
        throw new ConvirganceException("Class type of " + value.getClass().getName() + " for " + key + " cannot be converted to a boolean");
    }
    
    public boolean getBoolean(String key, boolean defaultValue) throws ConvirganceException
    {
        Object value = this.map.get(key);
        
        if(value == null) return defaultValue;
        if(value instanceof Boolean) return ((Boolean)value);
        if(value instanceof String) return Boolean.parseBoolean(value.toString());
        
        throw new ConvirganceException("Class type of " + value.getClass().getName() + " for " + key + " cannot be converted to a boolean");
    }
    
    public int getInt(String key) throws ConvirganceException
    {
        Object value = this.map.get(key);
        
        if(value == null) throw new ConvirganceException(key + " is null and therefore can't be converted to an int");
        if(value instanceof Integer) return ((Integer)value);
        if(value instanceof String) return Integer.parseInt(value.toString());
        
        throw new ConvirganceException("Class type of " + value.getClass().getName() + " for " + key + " cannot be converted to an int");
    }
    
    public int getInt(String key, int defaultValue) throws ConvirganceException
    {
        Object value = this.map.get(key);
        
        if(value == null) return defaultValue;
        if(value instanceof Integer) return ((Integer)value);
        if(value instanceof String) return Integer.parseInt(value.toString());
        
        throw new ConvirganceException("Class type of " + value.getClass().getName() + " for " + key + " cannot be converted to an int");
    }
    
    public String getString(String key)
    {
        Object value = this.map.get(key);
        
        if(value == null) return null;
        
        return value.toString();
    }
    
    public String getString(String key, String defaultValue)
    {
        Object value = this.map.get(key);
        
        if(value == null) return defaultValue;
        
        return value.toString();
    }

    @Override
    public Object put(String key, Object value)
    {
        return this.map.put(key, value);
    }

    @Override
    public Object remove(Object key)
    {
        return this.map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> map)
    {
        this.map.putAll(map);
    }

    @Override
    public void clear()
    {
        this.map.clear();
    }

    @Override
    public Set<String> keySet()
    {
        return this.map.keySet();
    }

    @Override
    public Collection<Object> values()
    {
        return this.map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet()
    {
        return this.map.entrySet();
    }

    @Override
    public String toString()
    {
        try
        {
            return new JSONWriter().write(this).toString();
        }
        catch(IOException e) { throw new ConvirganceException(e); }
    }

    public String toString(int indent)
    {
        try
        {
            return new JSONWriter(indent).write(this).toString();
        }
        catch(IOException e) { throw new ConvirganceException(e); }
    }
    
    @Override
    public boolean equals(Object obj)
    {
        JSONObject other;
        Object left;
        Object right;
        
        if(obj == this) return true;
        if(!(obj instanceof JSONObject)) return false;
        
        other = (JSONObject)obj;
        
        if(other.size() != size()) return false;
        
        for(String key : keySet())
        {
            if(!other.containsKey(key)) return false;
            
            left = get(key);
            right = other.get(key);
            
            if(left == null && right == null) continue;
            if(left == null) return false;
            if(right == null) return false;

            if(!left.getClass().equals(right.getClass())) return false;
            if(!left.equals(right)) return false;
        }
        
        return true;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 0xC0FFEE;
        Object value;
//System.out.println("---------");
        for(String key : keySet())
        {
            value = get(key);
            
            if(value == null) continue;
//System.out.println(Integer.toHexString(hash)+":"+Integer.toHexString(value.hashCode())+":"+key+":["+value+"]"+":"+value.getClass());
            hash += value.hashCode();
//System.out.println(Integer.toHexString(hash));
        }
//System.out.println("=========");
        return hash + size();
    }
}
