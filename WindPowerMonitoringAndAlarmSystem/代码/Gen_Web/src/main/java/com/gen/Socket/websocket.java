package com.gen.Socket;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/*
启动发送数据线程
 */
@ServerEndpoint("/websocket")
public class websocket {

    @OnOpen
    public void onOpen(Session session){
        OneThread thread=null;
        thread=new OneThread(session);
        thread.start();
    }
}
