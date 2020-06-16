package com.gen.VO;

public class Rule {

    public String DATA_DATE;
    public String WIND_SPEED;
    public String POWER;
    public String object;
    public String cycle;
    public String temperature;
    public String count;

    public Rule(String DATA_DATE, String WIND_SPEED,String POWER,String object,String cycle,String temperature,String count){
        this.DATA_DATE=DATA_DATE;
        this.WIND_SPEED=WIND_SPEED;
        this.POWER=POWER;
        this.object=object;
        this.cycle=cycle;
        this.temperature=temperature;
        this.count=count;
    }
    public String getDATA_DATE(){ return DATA_DATE; }
    public String getWIND_SPEED(){
        return WIND_SPEED;
    }
    public String getPOWER(){
        return POWER;
    }
    public String getObject(){
        return object;
    }
    public String getCycle(){
        return cycle;
    }
    public String getTemperature(){
        return temperature;
    }
    public String getCount(){
        return count;
    }
}
