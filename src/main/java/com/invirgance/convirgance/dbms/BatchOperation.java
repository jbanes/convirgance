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
package com.invirgance.convirgance.dbms;

import com.invirgance.convirgance.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author jbanes
 */
public class BatchOperation implements AtomicOperation
{
    private Query query;
    private Iterable<JSONObject> records;
    private int commit = 10000;

    public BatchOperation()
    {
    }
    
    public BatchOperation(Query query)
    {
        this.query = query;
    }

    public BatchOperation(Query query, Iterable<JSONObject> records)
    {
        this.query = query;
        this.records = records;
    }

    public Query getQuery()
    {
        return query;
    }

    public void setQuery(Query query)
    {
        this.query = query;
    }

    public Iterable<JSONObject> getRecords()
    {
        return records;
    }

    public void setRecords(Iterable<JSONObject> records)
    {
        this.records = records;
    }
    
    public int getAutoCommit()
    {
        return commit;
    }

    public void setAutoCommit(int commit)
    {
        this.commit = commit;
    }
    
    private String getSQL()
    {
        StringBuilder builder = new StringBuilder();
        String sql = query.getSQL();
        int start = 0;
        
        for(Query.Parameter parameter : query.getParameters())
        {
            builder.append(sql.substring(start, parameter.getStart()));
            builder.append("?");
            
            start = parameter.getStart() + parameter.getLength();
        }
        
        builder.append(sql.substring(start, sql.length()));
        
        return builder.toString();
    }
    
    private void populate(PreparedStatement statement, JSONObject record) throws SQLException
    {
        int index = 1;
        
        for(Query.Parameter parameter : query.getParameters())
        {
            statement.setObject(index++, record.get(parameter.getName()));
        }
        
        statement.addBatch();
    }
    
    @Override
    public void execute(Connection connection) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement(getSQL());
        int index = 0;
        
        for(JSONObject record : records)
        {
            populate(statement, record);
            
            index++;
            
            // Perform a commit every 10000 records to prevent overflow of
            // transaction buffer
            if(index%commit == 0) statement.executeBatch();
        }
        
        statement.executeBatch();
        statement.close();
    }
    
}
