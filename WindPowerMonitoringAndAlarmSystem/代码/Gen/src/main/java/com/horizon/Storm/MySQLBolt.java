package com.horizon.Storm;

import com.google.common.collect.Maps;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.common.JdbcClient;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.windowing.TupleWindow;

import java.text.SimpleDateFormat;
import java.util.*;

/*
输出MySQL：对于正常数据监控异常指标，输出到MySQL中记录
规则：从MySQL数据库中读取（前端自定义写入到MySQL数据库）
报警信息包括（机组编号、报警时间、报警描述：过去X秒内发电机温度高于X度以上出现：N次）
 */
public class MySQLBolt extends BaseWindowedBolt {

    private ConnectionProvider connectionProvider;
    private JdbcClient jdbcClient;
    private Double temp;
    private int alert_duration;
    private int alert_temp;
    private int alert_count;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
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
        //获取数据库中的监控报警条件
        List<List<Column>> rules=jdbcClient.select("select * from rule",new ArrayList<Column>());
        List<Column> col=rules.get(rules.size()-1);//取最新一条规则
        alert_duration=Integer.parseInt(col.get(5).getVal().toString());//监控周期时长
        alert_temp=Integer.parseInt(col.get(6).getVal().toString());//报警温度
        alert_count=Integer.parseInt(col.get(7).getVal().toString());//高温累计次数
    }

    public void execute(TupleWindow tupleWindow) {
        Map<String,Integer> map=new HashMap<String,Integer>();
        //计算报警次数
        for(Tuple tuple:tupleWindow.get()) {
            String fan_no=tuple.getStringByField("FAN_NO");
            temp=tuple.getDoubleByField("temp");
            if(temp>alert_temp){
                Integer value=map.get(fan_no);
                if(value==null){
                    map.put(fan_no,1);
                }else{
                    map.put(fan_no,value+1);
                }
            }
        }
        //发送报警数据到MySQL
        for(Map.Entry<String,Integer> e:map.entrySet()){
            Integer count=e.getValue();
            if(count>=alert_count){
                String fan_no=e.getKey();
                //获取实时系统时间
                SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time=fmt.format(new Date(System.currentTimeMillis()));
                //形成描述
                String describe="过去"+alert_duration+"秒发电机温度高于"+alert_temp+"度以上："+count+"次";
                //写入数据库
                jdbcClient.executeSql("INSERT INTO alert(FAN_NO,DATA_DATE,DATA_DESCRIBE) VALUES('"+fan_no+"','"+time+"','"+describe+"')");
                System.out.println(fan_no+","+time+","+describe);
            }
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }
}
