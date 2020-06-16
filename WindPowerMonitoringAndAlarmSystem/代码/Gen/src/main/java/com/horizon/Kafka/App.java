package com.horizon.Kafka;

public class App {
    public static void main( String[] args ) {
    	GenerateData gd1=new GenerateData(IConstant.TOPIC, IConstant.FJ_DATA_WT02287);
    	GenerateData gd2=new GenerateData(IConstant.TOPIC, IConstant.FJ_DATA_WT02288);
    	GenerateData gd3=new GenerateData(IConstant.TOPIC, IConstant.FJ_DATA_WT02289);
    	new Thread(gd1).start();
    	new Thread(gd2).start();
    	new Thread(gd3).start();
		System.out.println("Over!");
    }
}
