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
import java.util.*;

/**
 *
 * @author jbanes
 */
public class JSONObject implements Map<String, Object>
{
    private final HashMap<String, Object> map;
    
    private boolean ordered = false;
    private OrderedKeys<String> orderedKeys;

    public JSONObject()
    {
        this(false);
    }

    public JSONObject(boolean ordered)
    {
        map = new HashMap<>();
        
        if(ordered) setOrdered(ordered);
    }

    public JSONObject(String json)
    {
        JSONObject object;
        
        try
        {
            object = new JSONParser(json).parseObject();
            
            this.map = object.map;
            this.ordered = object.ordered;
            this.orderedKeys = object.orderedKeys;
        }
        catch(IOException e) { throw new ConvirganceException(e); }
    }
    
    public JSONObject(Map<String, Object> map)
    {
        this();
        
        this.map.putAll(map);
        
        if(map instanceof JSONObject)
        {
            if(((JSONObject)map).isOrdered())
            {
                this.ordered = true;
                this.orderedKeys = new OrderedKeys<>(((JSONObject)map).orderedKeys);
            }
        }
    }

    public boolean isOrdered()
    {
        return ordered;
    }

    public void setOrdered(boolean ordered)
    {
        if(ordered && !this.ordered) 
        {
            orderedKeys = new OrderedKeys<>();
            
            for(String key : this.map.keySet()) orderedKeys.add(key);
        }
        else if(!ordered)
        {
            orderedKeys = null;
        }
        
        this.ordered = ordered;
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
    
    public double getDouble(String key) throws ConvirganceException
    {
        Object value = this.map.get(key);
        
        if(value == null) throw new ConvirganceException(key + " is null and therefore can't be converted to a double");
        if(value instanceof Double) return ((Double)value);
        if(value instanceof String) return Double.parseDouble(value.toString());
        
        throw new ConvirganceException("Class type of " + value.getClass().getName() + " for " + key + " cannot be converted to a double");
    }
    
    public double getDouble(String key, double defaultValue) throws ConvirganceException
    {
        Object value = this.map.get(key);
        
        if(value == null) return defaultValue;
        if(value instanceof Double) return ((Double)value);
        if(value instanceof String) return Double.parseDouble(value.toString());
        
        throw new ConvirganceException("Class type of " + value.getClass().getName() + " for " + key + " cannot be converted to a double");
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
    
    public JSONArray getJSONArray(String key) throws ConvirganceException
    {
        Object value = this.map.get(key);
        
        if(value == null) return null;
        if(value instanceof JSONArray) return ((JSONArray)value);
        
        throw new ConvirganceException("Class type of " + value.getClass().getName() + " for " + key + " cannot be converted to a JSONArray");
    }
    
    public JSONArray getJSONArray(String key, JSONArray defaultValue) throws ConvirganceException
    {
        Object value = this.map.get(key);
        
        if(value == null) return defaultValue;
        if(value instanceof JSONArray) return ((JSONArray)value);
        
        throw new ConvirganceException("Class type of " + value.getClass().getName() + " for " + key + " cannot be converted to a JSONArray");
    }
    
    public JSONObject getJSONObject(String key) throws ConvirganceException
    {
        Object value = this.map.get(key);
        
        if(value == null) return null;
        if(value instanceof JSONObject) return ((JSONObject)value);
        
        throw new ConvirganceException("Class type of " + value.getClass().getName() + " for " + key + " cannot be converted to a JSONObject");
    }
    
    public JSONObject getJSONObject(String key, JSONObject defaultValue) throws ConvirganceException
    {
        Object value = this.map.get(key);
        
        if(value == null) return defaultValue;
        if(value instanceof JSONObject) return ((JSONObject)value);
        
        throw new ConvirganceException("Class type of " + value.getClass().getName() + " for " + key + " cannot be converted to a JSONObject");
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
        if(ordered && !orderedKeys.contains(key)) orderedKeys.add(key);
        
        return this.map.put(key, value);
    }

    @Override
    public Object remove(Object key)
    {
        if(ordered) orderedKeys.remove((String)key);
        
        return this.map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> map)
    {
        if(ordered)
        {
            for(String key : map.keySet())
            {
                if(!orderedKeys.contains(key)) orderedKeys.add(key);
            }
        }
        
        this.map.putAll(map);
    }

    @Override
    public void clear()
    {
        if(ordered) orderedKeys.clear();
        
        this.map.clear();
    }

    @Override
    public Set<String> keySet()
    {
        if(ordered) return orderedKeys;
        
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

        for(String key : keySet())
        {
            value = get(key);
            
            if(value == null) continue;

            hash += value.hashCode();
        }

        return hash + size();
    }
    
    private class OrderedKeys<T> extends ArrayList<T> implements Set<T>
    {
        public OrderedKeys()
        {
            super();
        }
        
        public OrderedKeys(Collection collection)
        {
            super(collection);
        }
    }
}
