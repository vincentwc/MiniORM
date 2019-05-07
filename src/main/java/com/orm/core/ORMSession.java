package com.orm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ORMSession {

    private Connection connection;

    public ORMSession(Connection connection) {
        this.connection = connection;
    }

    //保存数据
    public void save(Object entity) throws Exception {
        String insertSQL = "";
        //1.从ORMConfig中获得保存有映射信息的集合
        List<Mapper> mapperList = ORMConfig.mapperList;

        //2.遍历集合，找到想要的那个对象【entity】
        for (Mapper mapper : mapperList) {
            if (mapper.getClassName().equals(entity.getClass().getName())) {

                String insertSQL1 = "insert into " + mapper.getTableName() + "( ";
                String insertSQL2 = ") values ( ";

                //3.拿到当前对象entity所属类中所有的属性
                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true); //拿到私有属性
                    //4.遍历过程中，根据属性得到字段名，遍历过程中，根据属性，得到值
                    String cloumnName = mapper.getPropMapper().get(field.getName());
                    //5.遍历得到值
                    String columnValue = field.get(entity).toString();
                    //6.拼接sql
                    insertSQL1 += cloumnName + ",";
                    insertSQL2 += "'" + columnValue + "',";
                }
                insertSQL = insertSQL1.substring(0, insertSQL1.length() - 1) + insertSQL2.substring(0, insertSQL2.length() - 1) + ")";
                break;
            }
        }

        //把sql语句打印到控制台
        System.out.println("MiniORM-save" + insertSQL);

        //7.通过JDBC发送并执行SQL
        PreparedStatement statement = connection.prepareStatement(insertSQL);
        statement.executeUpdate();
        statement.close();
    }
}
