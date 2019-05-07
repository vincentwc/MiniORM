package com.orm.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类用来封装和存储映射信息
 */
public class Mapper {

    //类名
    private String className;

    //表名
    private String tableName;

    //存储主键信息
    private Map<String, String> idMapper = new HashMap<>();

    //存储普通的属性和字段信息
    private Map<String, String> propMapper = new HashMap<>();


    public Mapper() {
    }

    public Mapper(String className, String tableName, Map<String, String> idMapper, Map<String, String> propMapper) {
        this.className = className;
        this.tableName = tableName;
        this.idMapper = idMapper;
        this.propMapper = propMapper;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getIdMapper() {
        return idMapper;
    }

    public void setIdMapper(Map<String, String> idMapper) {
        this.idMapper = idMapper;
    }

    public Map<String, String> getPropMapper() {
        return propMapper;
    }

    public void setPropMapper(Map<String, String> propMapper) {
        this.propMapper = propMapper;
    }

    @Override
    public String toString() {
        return "Mapper{" +
                "className='" + className + '\'' +
                ", tableName='" + tableName + '\'' +
                ", idMapper=" + idMapper +
                ", propMapper=" + propMapper +
                '}';
    }
}
