package com.gen.Util;

import com.gen.VO.AlertMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
从数据库中读取数据
 */
public class DBUtil {

    public List<AlertMessage> getFromDB() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<AlertMessage> list=new ArrayList<AlertMessage>();
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn=DriverManager.getConnection("jdbc:mysql://172.16.29.90:3306/Gen?useUnicode=true&characterEncoding=UTF-8&useSSL=false","hive","995524@Fjq");
        Statement stat=conn.createStatement();
        ResultSet rs=stat.executeQuery("select * from alert");
        while(rs.next()){
            AlertMessage alertMessage=new AlertMessage(rs.getString(2),rs.getString(3),rs.getString(4));
            list.add(alertMessage);
        }
        rs.close();
        stat.close();
        conn.close();
        return list;
    }
}