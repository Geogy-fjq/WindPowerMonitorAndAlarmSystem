package com.gen.Socket;

import com.gen.Util.DBUtil;
import com.gen.VO.AlertMessage;

import javax.websocket.Session;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
用于发送新数据到页面的线程
run中开启无限循环，用一个变量currentIndex记录当前数据量，当有新数据时，发送新数据
 */
public class OneThread extends Thread {

    private Session session;
    private List<AlertMessage> currentMessage;
    private DBUtil dbUtil;
    private int currentIndex;

    public OneThread(Session session) {
        this.session=session;
        currentMessage=new ArrayList<AlertMessage>();
        dbUtil=new DBUtil();
        currentIndex=0;//此时是0条消息
    }

    @Override
    public void run() {
        while(true) {
            List<AlertMessage> list=null;
            try {
                list=dbUtil.getFromDB();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(list!=null && currentIndex<list.size()) {
                for(int i=currentIndex;i<list.size();i++) {
                    try {
                        session.getBasicRemote().sendText(list.get(i).getFAN_NO() + "," + list.get(i).getDATA_DATE() + "," + list.get(i).getDescribe());
                        //session.getBasicRemote().sendObject(list.get(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    currentIndex=list.size();
                }
//            try {
//                Thread.sleep(1000);//一秒刷新一次
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            }
        }
    }
}
