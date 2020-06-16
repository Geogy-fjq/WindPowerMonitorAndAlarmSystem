package com.horizon.VO;

public class Data {
    public String DATASOURCE;//0来源
    public String FAN_NO;//1编号
    public String DATA_DATE;//2日期
    public String FAN_STATUS;//3状态
    public double WIND_SPEED;//4风速
    public double ROTOR_RS;//5电机转速
    public double RS;//6桨叶转速
    public double WIND_DIRECTION;//7风向
    public double YAW_ANGLE;//8偏航角度
    public double BOX_T;//9齿轮箱油温
    public double BOX_BEARING_T;//10齿轮箱轴承油温
    public double ENVIRON_T;//11环境温度
    public double WT_T;//12机舱温度
    public double ROTOR_GROUP_T;//13发电机温度
    public double A_PHASE_C;//14A相电流
    public double B_PHASE_C;//15B相电流
    public double C_PHASE_C;//16C相电流
    public double A_PHASE_V;//17A相电压
    public double B_PHASE_V;//18B相电压
    public double C_PHASE_V;//19C相电压
    public double MACHINE_FREQUENCY;//20电机频率
    public double REACTIVE_POWER;//21无功功率
    public double POWER;//22有功功率
    public double POWER_FACTOR;//23功率因数
    public double TOTAL_POWER;//24总发电量
    public double TOTAL_GEN_TIME;//25总发电时间
    public double DOWN_TIME;//26故障时间
    public double STANDBY_TIME;//27备用时间
    public double REMARK;//28备注
    public String fc;//29
    public String fj;//30
    public String describe;//描述

    public Data(String DATA_DATE,String describe){
        this.DATA_DATE=DATA_DATE;
        this.describe=describe;
    }
    public String getDATA_DATE(){ return DATA_DATE; }
    public String getDescribe(){
        return describe;
    }
    public void setDATA_DATE(String DATA_DATE){
        this.DATA_DATE=DATA_DATE;
    }
    public void setDescribe(String describe){ this.describe=describe; }
}
