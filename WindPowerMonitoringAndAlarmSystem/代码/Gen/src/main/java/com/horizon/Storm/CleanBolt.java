package com.horizon.Storm;

import com.google.common.collect.Maps;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.common.JdbcClient;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
清洗数据：按照数据清洗规则，分别为合理数据和不合理数据添加标注
 */
public class CleanBolt extends BaseRichBolt {

    private OutputCollector collector;
    private ConnectionProvider connectionProvider;
    private JdbcClient jdbcClient;
    private String DATA_DATE;
    private double WIND_SPEED1;
    private double WIND_SPEED2;
    private int POWER1;
    private int POWER2;
    private String object;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
        //设置数据库连接参数
        Map configMap=Maps.newHashMap();
        configMap.put("dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        configMap.put("dataSource.url", "jdbc:mysql://172.16.29.90:3306/Gen?useUnicode=true&characterEncoding=UTF-8");
        configMap.put("dataSource.user","hive");
        configMap.put("dataSource.password","995524@Fjq");
        connectionProvider=new HikariCPConnectionProvider(configMap);
        //对数据库连接进行初始化
        connectionProvider.prepare();
        jdbcClient=new JdbcClient(connectionProvider,30);
        //获取数据库中的数据清洗规则
        List<List<Column>> rules=jdbcClient.select("select * from rule",new ArrayList<Column>());
        List<Column> col=rules.get(rules.size()-1);//取最新一条规则
        DATA_DATE=col.get(1).getVal().toString();
        WIND_SPEED1=Double.parseDouble(col.get(2).getVal().toString().split("~")[0]);
        WIND_SPEED2=Double.parseDouble(col.get(2).getVal().toString().split("~")[1]);
        POWER1=Integer.parseInt(col.get(3).getVal().toString().split("~")[0]);
        POWER2=Integer.parseInt(col.get(3).getVal().toString().split("~")[1]);
        object=col.get(4).getVal().toString();
    }

    public void execute(Tuple tuple) {
        String line=tuple.getString(0);
        String[] split=line.split(",");
        String date_get=split[2];//新获取到的数据的日期
        String speed_get=split[4];//新获取到的数据的风速
        String power_get=split[22];//新获取到的数据的功率
        boolean flag=true;//数据合理性的标记
        //对数据进行标记
        //日期
        if(DATA_DATE.equals("Today")){
            //获取当天的日期
            SimpleDateFormat fmt1=new SimpleDateFormat("MM-dd");
            String date1="2016-"+fmt1.format(new Date(System.currentTimeMillis()));
            //获取接收到的数据的日期
            SimpleDateFormat fmt_get=new SimpleDateFormat("yyyy-MM-dd");
            if(date_get!=null && !date_get.equals("")){
                String date=date_get;
                //对非时间戳格式的数据进行转换
                if(!date_get.split("")[4].equals("-")){
                    date=fmt_get.format(new Date(split[2]));
                }
                if(!date.equals(date)){
                    flag=false;
                }
            }else{
                flag=false;
            }
        }else if(DATA_DATE.equals("EveryDay")){
            if(date_get==null || date_get.equals("")){
                flag=false;
            }
        }
        //风速
        if(speed_get!=null && !speed_get.equals("")){
            double speed=Double.valueOf(speed_get);
            if(speed<WIND_SPEED1 || speed>WIND_SPEED2){
                flag=false;
            }
        }else{
            flag=false;
        }
        //功率
        if(power_get!=null && !power_get.equals("")){
            double power=Double.valueOf(power_get);
            if(power<POWER1 || power>POWER2){
                flag=false;
            }
        }else{
            flag=false;
        }
        collector.emit(new Values(flag,line,object));
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("flag","data","object"));
    }
}
