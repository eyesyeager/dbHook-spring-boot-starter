package io.github.eyesyeager.dbHookStarter.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author artonyu
 */

public class DbUtils {

    public static Object resultSetToField(ResultSet resultSet, Class<?> clazz) throws SQLException {
        Object obj = resultSet.getObject(1);
        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        }
        throw new RuntimeException("type mismatch, result is not of that type: " + clazz.getName());
    }

    public static <T> T resultSetToEntity(ResultSet resultSet, Class<T> clazz)
            throws SQLException, ReflectiveOperationException {
        T entity = clazz.getDeclaredConstructor().newInstance();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = toCamelCase(metaData.getColumnName(i));
            Object columnValue = resultSet.getObject(i);
            // 将列名转换为字段名
            Field field = clazz.getDeclaredField(columnName);
            field.setAccessible(true);
            field.set(entity, columnValue);
        }
        return entity;
    }

    public static String toCamelCase(String underscore) {
        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;
        for (char c : underscore.toCharArray()) {
            if (c == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }
}
