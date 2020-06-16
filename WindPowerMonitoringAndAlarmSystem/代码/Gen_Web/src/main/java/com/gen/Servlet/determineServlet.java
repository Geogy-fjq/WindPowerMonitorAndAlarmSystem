package com.gen.Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gen.Dao.DetermineDao1;
import com.gen.VO.Rule;

@WebServlet("/determineServlet")
public class determineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    DetermineDao1 dao1=new DetermineDao1();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置编码格式
        request.setCharacterEncoding("utf-8");
        //获取输入数据
        String DATA_DATE=request.getParameter("DATA_DATE");
        String WIND_SPEED1=request.getParameter("WIND_SPEED1");
        String WIND_SPEED2=request.getParameter("WIND_SPEED2");
        String POWER1=request.getParameter("POWER1");
        String POWER2=request.getParameter("POWER2");
        String object=request.getParameter("object");
        String cycle=request.getParameter("cycle");
        String temperature=request.getParameter("temperature");
        String count=request.getParameter("count");
        Rule rule=new Rule(DATA_DATE,WIND_SPEED1+"~"+WIND_SPEED2,POWER1+"~"+POWER2,object,cycle,temperature,count);
        //将数据清洗规则和监控报警条件存入数据库
        dao1.determine(rule);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}