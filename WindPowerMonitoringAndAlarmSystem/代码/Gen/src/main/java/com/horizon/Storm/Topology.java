package com.horizon.Storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.topology.base.BaseWindowedBolt;

import java.util.concurrent.TimeUnit;

public class Topology {

    public static void main(String[] args) throws InterruptedException, AlreadyAliveException, InvalidTopologyException, AuthorizationException {
        //1.配置spoutConf
        ZkHosts zkHosts=new ZkHosts("172.16.29.90:2181");
        SpoutConfig spoutConfig=new SpoutConfig(zkHosts,"Gen","","Gen-Group");
        spoutConfig.scheme=new SchemeAsMultiScheme(new StringScheme());
        spoutConfig.socketTimeoutMs=60*1000;
        //spoutConfig.forceFromStart=true;
        //2.配置conf
        Config conf=new Config();
        conf.setNumWorkers(2);
        conf.setNumAckers(0);
        conf.setDebug(false);
        Config.setMessageTimeoutSecs(conf,40000);//如果不设置，会报错（window持续35000）
        //3.构建拓扑
        TopologyBuilder builder=new TopologyBuilder();
        builder.setSpout("kafka-spout", new KafkaSpout(spoutConfig));
        builder.setBolt("clean-bolt", new CleanBolt()).shuffleGrouping("kafka-spout");
        builder.setBolt("hbase-bolt", new HBaseBolt()).shuffleGrouping("clean-bolt");
        builder.setBolt("mysql-bolt", new MySQLBolt().withWindow(new BaseWindowedBolt.Duration(30, TimeUnit.SECONDS),new BaseWindowedBolt.Duration(5, TimeUnit.SECONDS))).shuffleGrouping("hbase-bolt");
        //4.提交topology
        if(args!=null && args.length>0) {
            //提交topology到storm集群中运行
            StormSubmitter.submitTopology(args[0],conf,builder.createTopology());
        }else {
            //提交topology到本地运行
            LocalCluster cluster=new LocalCluster();
            cluster.submitTopology("Topology",conf,builder.createTopology());
            Thread.sleep(10000);
            cluster.killTopology("WordCountTopology");
            cluster.shutdown();
        }
    }
}
