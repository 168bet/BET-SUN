package com.wecode.game.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.Statement;

@Component
public class SpringJdbc
{
    static Logger        logger       = Logger.getLogger(SpringJdbc.class
                                              .getName());
    @Resource(name="readJdbcTemplate")
    private JdbcTemplate readJdbcTemplate;
    
    @Resource(name="writeJdbcTemplate")
    private JdbcTemplate writeJdbcTemplate;
    
    synchronized public int update(String sql, Object[] params)
    {
        int iRowsAffected = 0;
        try
        {
            iRowsAffected = writeJdbcTemplate.update(sql, params);
        }
        catch ( DataAccessException ex )
        {
            logger.error(ex.getMessage());
            return -1;
        }
        return iRowsAffected;
    }

    public List<Map<String, Object>> query(String sql)
    {
        try
        {
            return readJdbcTemplate.queryForList(sql);
        }
        catch ( DataAccessException ex )
        {
            logger.error("SpringJdbcImpl - query :error " + ex);
        }
        return null;
    }

    public List<Map<String, Object>> query(String sql, Integer page, Integer num)
    {
        if ( page <= 0 || num <= 0 )
        {
            return query(sql);
        }
        sql = sql + " limit ?, ?";
        Object[] params = new Object[] { ( page - 1 ) * num, num };
        try
        {
            return readJdbcTemplate.queryForList(sql, params);
        }
        catch ( DataAccessException ex )
        {
            logger.error("SpringJdbcImpl - query :error " + ex);
        }
        return null;
    }

    public List<Map<String, Object>> query(String sql, Object[] args,
            int[] argTypes)
    {
        try
        {
            return readJdbcTemplate.queryForList(sql, args, argTypes);
        }
        catch ( DataAccessException ex )
        {
            logger.error("SpringJdbcImpl - query :error " + ex);

            return null;
        }
    }

    public List<Map<String, Object>> query(String sql, Object[] args,
            int[] argTypes, Integer page, Integer num)
    {
        if ( page <= 0 || num <= 0 )
        {
            return query(sql, args, argTypes);
        }
        sql = sql + " limit ?, ?";
        int length = args.length;

        Object[] newArags = new Object[length + 2];
        int[] newArgTypes = new int[length + 2];

        System.arraycopy(args, 0, newArags, 0, length);
        System.arraycopy(argTypes, 0, newArgTypes, 0, length);

        length = length + 2;

        newArgTypes[length - 2] = Types.INTEGER;
        newArgTypes[length - 1] = Types.INTEGER;

        newArags[length - 2] = ( page - 1 ) * num;
        newArags[length - 1] = num;

        try
        {
            return readJdbcTemplate.queryForList(sql, newArags, newArgTypes);
        }
        catch ( DataAccessException ex )
        {
            logger.error("SpringJdbcImpl - query :error " + ex);

            return null;
        }

    }

    public int insert(final String sql, final Object[] params)
    {
        System.out.println("sql:" + sql);
        // System.out.println(params[0]);

        KeyHolder keyHolder = new GeneratedKeyHolder(); 
        writeJdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection arg0)
                    throws SQLException
            {
                PreparedStatement ps = arg0.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                for ( int i = 0; i < params.length; i++ )
                {
                	ps.setObject(i+1, params[i]);
                }
                return ps;
            }
        }, keyHolder);

        return 1;
    }

    public long insertWithType(final String sql, final Object[] params,
            final int[] argTypes)
    {
        System.out.println("sql:" + sql);
        // System.out.println(params[0]);

        KeyHolder keyHolder = new GeneratedKeyHolder(); 
        writeJdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection arg0)
                    throws SQLException
            {
                PreparedStatement ps = arg0.prepareStatement(sql);
                for ( int i = 0; i < params.length; i++ )
                {
                    ps.setObject(i + 1, params[i], argTypes[i]);
                }
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
