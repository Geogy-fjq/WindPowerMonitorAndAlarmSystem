package com.gen.Dao;

import com.gen.VO.Rule;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DetermineDao1 extends available implements DetermineDao {

    //连接数据库
    public void connection() throws ClassNotFoundException, SQLException {
        Class.forName(jdbcName);
        this.con = DriverManager.getConnection(url,user,password);
    }

    //关闭连接
    public void refuse() throws SQLException {
        if (this.rs != null) {
            rs.close();
        }
        if (this.ps != null) {
            ps.close();
        }
        if (this.st != null) {
            st.close();
        }
        if (this.con != null) {
            con.close();
        }
    }

    @Override
    public String determine(Rule rule) {
        try {
            connection();
            this.sql="INSERT INTO rule(DATA_DATE,WIND_SPEED,POWER,object,cycle,temperature,counts) VALUES(?,?,?,?,?,?,?)";
            this.ps=con.prepareStatement(sql);
            ps.setString(1, rule.getDATA_DATE());
            ps.setString(2, rule.getWIND_SPEED());
            ps.setString(3, rule.getPOWER());
            ps.setString(4, rule.getObject());
            ps.setInt(5, Integer.parseInt(rule.getCycle()));
            ps.setInt(6, Integer.parseInt(rule.getTemperature()));
            ps.setInt(7, Integer.parseInt(rule.getCount()));
            ps.executeUpdate();
            refuse();
            return "success";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
