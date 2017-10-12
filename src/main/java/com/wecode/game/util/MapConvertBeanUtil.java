package com.wecode.game.util;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.wecode.game.exception.FieldNotExistException;
import com.wecode.game.exception.IntConvertBoolException;

public class MapConvertBeanUtil
{

    @SuppressWarnings("unchecked")
	public static <T> T resultToBean(Map<String, Object> result,
            Class<T> classType) throws IllegalArgumentException,
            IntConvertBoolException, InstantiationException,
            IllegalAccessException
    {
        Assert.notNull(classType);

        if ( result == null )
        {
            return null;
        }

        Object object = classType.newInstance();
        List<Field> fields = getAllDeclaredFields(classType);
        String databaseFieldName;

        for ( Field field : fields )
        {
            databaseFieldName = field.getName();

            if ( result.containsKey(databaseFieldName) )
            {
                setValue(field, databaseFieldName, result, object);
            }
        }

        return (T) object;
    }

   
    @SuppressWarnings("unchecked")
	public static <T> T resultToBean(Map<String, Object> result,
            Class<T> classType, Map<String, String> mapTable)
            throws FieldNotExistException, IllegalArgumentException,
            IntConvertBoolException, IllegalAccessException,
            InstantiationException
    {
        Assert.notNull(classType);
        Assert.notNull(mapTable);

        if ( result == null )
        {
            return null;
        }

        Object object = classType.newInstance();
        List<Field> fields = getAllDeclaredFields(classType);
        String databaseFieldName;

        for ( Field field : fields )
        {
            databaseFieldName = mapTable.get(field.getName());
            if ( databaseFieldName != null )
            {
                if ( result.containsKey(databaseFieldName) )
                {
                    setValue(field, databaseFieldName, result, object);
                }
                else
                {
                    throw new FieldNotExistException(databaseFieldName
                            + " is not exsit database.");
                }
            }
            else
            {
                databaseFieldName = field.getName();
                if ( result.containsKey(databaseFieldName) )
                {
                    setValue(field, databaseFieldName, result, object);
                }
            }
        }
        return (T) object;
    }

    private static void setValue(Field field, String databaseFieldName,
            Map<String, Object> result, Object object)
            throws IllegalArgumentException, IllegalAccessException,
            IntConvertBoolException
    {
        // TODO Auto-generated method stub
        Object value;
        if ( result.get(databaseFieldName) == null )
        {
            return;
        }

        if ( field.getType() == Boolean.class )
        {
            Integer bool = (Integer) result.get(databaseFieldName);
            if ( bool == 0 )
            {
                value = false;
            }
            else if ( bool == 1 )
            {
                value = true;
            }
            else
            {
                throw new IntConvertBoolException("��ݿ��е������޷�ת��Ϊboolean");
            }
        }
        else
        {
            value = result.get(databaseFieldName);
        }
        field.setAccessible(true);
        field.set(object, value);
    }

    @SuppressWarnings("rawtypes")
	private static List<Field> getAllDeclaredFields(Class classType)
    {
        List<Field> fieldList = new ArrayList<Field>();

        Field[] fields = classType.getDeclaredFields();

        fieldList.addAll(Arrays.asList(fields));
        Class type = classType.getSuperclass();
        while ( type != null
                && !type.getCanonicalName().equalsIgnoreCase(
                        Object.class.getCanonicalName()) )
        {
            Field[] tmpFields = type.getDeclaredFields();
            if ( null != tmpFields && tmpFields.length != 0 )
            {
                fieldList.addAll(Arrays.asList(tmpFields));
            }
            type = type.getSuperclass();
        }

        return fieldList;

    }
}
