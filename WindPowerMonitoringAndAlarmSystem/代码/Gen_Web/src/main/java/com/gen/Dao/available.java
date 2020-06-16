package com.gen.Dao;

import java.sql.*;

/*
配置数据库设置
 */
public class available {

    public String jdbcName="com.mysql.jdbc.Driver";
    public String url="jdbc:mysql://172.16.29.90:3306/Gen?useUnicode=true&characterEncoding=UTF-8";
    public String user="hive";
    public String password="995524@Fjq";
    public Connection con=null;
    public Statement st=null;
    public PreparedStatement ps=null;
    public ResultSet rs=null;
    public String sql=null;
    public CallableStatement proc=null;

}