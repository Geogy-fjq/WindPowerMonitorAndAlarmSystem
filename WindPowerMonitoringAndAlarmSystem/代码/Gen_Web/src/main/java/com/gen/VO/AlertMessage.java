package com.gen.VO;

public class AlertMessage {

    public String FAN_NO;//编号
    public String DATA_DATE;//日期
    public String describe;//报警描述

    public AlertMessage(String FAN_NO,String DATA_DATE, String describe){
        this.FAN_NO=FAN_NO;
        this.DATA_DATE=DATA_DATE;
        this.describe=describe;
    }
    public String getFAN_NO(){ return FAN_NO; }
    public String getDATA_DATE(){
        return DATA_DATE;
    }
    public String getDescribe(){
        return describe;
    }
}
