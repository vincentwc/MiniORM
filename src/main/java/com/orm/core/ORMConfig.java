package com.orm.core;

import com.orm.utils.AnnotationUtil;
import com.orm.utils.Dom4jUtil;

import org.dom4j.Document;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 该类用来解析并封装框架的核心配置文件的数据
 */
public class ORMConfig {

    //classpath路径
    private static String classpath;

    //核心配置文件
    private static File cfgFile;

    //<prperty>标签中的数据
    private static Map<String, String> propConfig;

    //映射配置文件路径
    private static Set<String> mappingSet;

    //实体类
    private static Set<String> entitySet;

    //映射信息
    public static List<Mapper> mapperList;

    //静态代码块，使用类【加载类】的时候执行
    static {
        //得到classpath路径
        classpath = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
        //针对中文路径进行转码
        try {
            classpath = URLDecoder.decode(classpath, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //得到核心配置文件
        cfgFile = new File(classpath + "miniORM.cfg.xml");
        if (cfgFile.exists()) {
            //解析数据
            Document document = Dom4jUtil.getXMLByFilePath(cfgFile.getPath());
            propConfig = Dom4jUtil.Elements2Map(document, "prperty", "name");
            mappingSet = Dom4jUtil.Elements2Set(document, "mapping", "resource");
            entitySet = Dom4jUtil.Elements2Set(document, "entity", "package");

        } else {
            cfgFile = null;
            System.out.println("未找到核心配置文件miniORM.cfg.xml");
        }
    }

    /**
     * 从propConfig集合中获取数据并连接数据库
     */
    private Connection getConnection() throws Exception {
        String url = propConfig.get("connection.url");
        String driverClass = propConfig.get("connection.driverClass");
        String username = propConfig.get("connection.username");
        String password = propConfig.get("connection.password");
        Class.forName(driverClass);
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    /**
     * 获取映射数据
     */
    private void getMapping() throws ClassNotFoundException {
        mapperList = new ArrayList<>();
        //1.解析xxxMapper.xml文件拿到映射数据
        for (String xmlPath : mappingSet) {
            Document document = Dom4jUtil.getXMLByFilePath(classpath + xmlPath);
            String className = Dom4jUtil.getPropValue(document, "class", "name");
            String tableName = Dom4jUtil.getPropValue(document, "class", "table");
            Map<String, String> id_id = Dom4jUtil.ElementsID2Map(document);
            Map<String, String> mapping = Dom4jUtil.Elements2Map(document);
            //mapper对象属性封装
            Mapper mapper = new Mapper(className, tableName, id_id, mapping);
            mapperList.add(mapper);
        }
        //2.解析实体类中注解拿到映射数据
        for (String packagePath : entitySet) {
            //实体类名字的集合
            Set<String> nameSet = AnnotationUtil.getClassNameByPackage(packagePath);
            for (String name : nameSet) {
                Class<?> clz = Class.forName(name);
                String className = AnnotationUtil.getClassName(clz);
                String tableName = AnnotationUtil.getTableName(clz);
                Map<String, String> id_id = AnnotationUtil.getIdMapper(clz);
                Map<String, String> propMapping = AnnotationUtil.getPropMapping(clz);
                //mapper对象属性封装
                Mapper mapper = new Mapper(className, tableName, id_id, propMapping);
                mapperList.add(mapper);
            }
        }
    }

    /**
     * 创建ORMSession对象
     */
    public ORMSession buildORMSession() throws Exception {
        //1.连接数据库
        Connection connection = getConnection();

        //2.得到映射数据
        getMapping();

        //3.创建ORMSession对象
        return new ORMSession(connection);
    }
}
