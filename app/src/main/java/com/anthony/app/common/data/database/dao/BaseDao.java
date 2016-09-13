package com.anthony.app.common.data.database.dao;


import com.anthony.app.common.base.MyApplication;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;


public class BaseDao<T> {
    protected Class<T> clazz;
    protected Dao<T, Integer> daoOpe;


    public BaseDao( MyApplication mApplication) {
        Class clazz = getClass();

        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    this.clazz = (Class<T>) args[0];
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }

        try {
            if (mApplication.dbHelper == null) {
                throw new RuntimeException("No DbHelper Found!");
            }
            daoOpe = mApplication.dbHelper.getDao(this.clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(T t) {
        try {
            daoOpe.create(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(T t) {
        try {
            daoOpe.delete(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(T t) {
        try {
            daoOpe.update(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<T> all() {
        try {
            return daoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<T> queryByColumn(String columnName, Object columnValue) {
        try {
            QueryBuilder builder = daoOpe.queryBuilder();
            builder.where().eq(columnName, columnValue);
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<T> queryByColumn(String columnName1, Object columnValue1,
                                 String columnName2, Object columnValue2) {
        try {
            QueryBuilder builder = daoOpe.queryBuilder();
            builder.where().eq(columnName1, columnValue1).and().eq(columnName2, columnValue2);
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteByColumn(String columnName1, Object columnValue1,
                               String columnName2, Object columnValue2) {
        try {
            DeleteBuilder builder = daoOpe.deleteBuilder();
            builder.where().eq(columnName1, columnValue1).and().eq(columnName2, columnValue2);
            builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteByColumn(String columnName,Object columnValue){
        try {
            DeleteBuilder builder = daoOpe.deleteBuilder();
            builder.where().eq(columnName, columnValue);
            builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void clearAll() {
        try {
            daoOpe.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long count() {
        try {
            return daoOpe.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void createOrUpdate(T t) {
        try {
            daoOpe.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
