package com.lemon.util;

import com.lemon.apidefinition.ApiCall;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JDBCUtils {
    public static Connection getConnection(){
        String url = "jdbc:mysql://mall.lemonban.com/yami_shops?useUnicode=true&characterEncoding=utf-8&useSSL=true";
        String user = "lemon";
        String password = "lemon123";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 查询单个数据   new ScalarHandler<>()
     * @param sql  查询语句
     * @return   查询结果
     */
    public static Object querySingleData(String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        sql = Environment.replaceParams(sql);
        Object data = null;
        try {
            data = queryRunner.query(connection,sql,new ScalarHandler<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 查询一条数据   new MapHandler()
     * @param sql   查询语句
     * @return   返回结果（Map）
     */
    public static Map<String,Object> queryOneData(String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        sql = Environment.replaceParams(sql);
        Map map = null;
        try {
            map = queryRunner.query(connection,sql,new MapHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 查询多条数据   new MapListHandler()
     * @param sql  查询语句
     * @return  返回结果（List<Map>）
     */
    public static List<Map<String,Object>> queryMoreData(String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        sql = Environment.replaceParams(sql);
        List<Map<String,Object>> list = null;
        try {
            list = queryRunner.query(connection,sql,new MapListHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
