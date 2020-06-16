package com.horizon.Storm;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/*
存储HBase:将正常数据和不合理数据分别存入HBase中
发送合理数据到MySQLBolt
 */
public class HBaseBolt implements IRichBolt {

    private OutputCollector collector;
    private Connection connection;
    private Table table_Normal;
    private Table table_Abnormal;
    private String object;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
        Configuration config=HBaseConfiguration.create();
        try {
            config.set("hbase.rootdir","hdfs://172.16.29.88:9000/apps/hbase/data");
            config.set("hbase.zookeeper.quorum","172.16.29.88,172.16.29.89,172.16.29.90");
            connection= ConnectionFactory.createConnection(config);
            table_Normal=connection.getTable(TableName.valueOf("Gen_Normal"));
            table_Abnormal=connection.getTable(TableName.valueOf("Gen_Abnormal"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(Tuple tuple) {
        boolean flag=tuple.getBooleanByField("flag");
        String data=tuple.getStringByField("data");
        object=tuple.getStringByField("object");
        //数据处理
        String[] split=data.split(",");
        SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=split[2];
        String fan_no=split[1];
        if(date!=null && !date.equals("") && fan_no!=null && !fan_no.equals("")){
            //对非时间戳格式的数据进行转换
            if(!date.split("")[4].equals("-")){
                date=fmt.format(new Date(split[2]));
            }
            String rowKey=date+"_"+fan_no;
            try {
                //RowKey：年月日时分秒_机组编号
                Put put=new Put(Bytes.toBytes(rowKey));
                //将数据写入Value列
                put.addColumn(Bytes.toBytes("Value"), Bytes.toBytes(""), Bytes.toBytes(data));
                if(flag){
                    table_Normal.put(put);
                    System.out.println("正常数据："+rowKey+"     "+data);
                    //通过监控对象名称，获取相应的对象数据
                    Double temp=0.00;
                    if(object.equals("ROTOR_GROUP_T")){//发电机温度
                        temp=Double.valueOf(split[13]);
                    }else if(object.equals("ENVIRON_T")){//环境温度
                        temp=Double.valueOf(split[11]);
                    }else if(object.equals("WT_T")){//机舱温度
                        temp=Double.valueOf(split[12]);
                    }
                    //将合理的数据发送到处理MySQL的Bolt(编号、监控对象数据)
                    collector.emit(new Values(fan_no,temp));
                }else{
                    table_Abnormal.put(put);
                    System.out.println("异常数据："+rowKey+"     "+data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanup() {
        try {
            if(table_Normal!=null && table_Abnormal!=null){
                table_Normal.close();
                table_Abnormal.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("FAN_NO","temp"));
    }
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
