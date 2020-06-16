# WindPowerMonitoringAndAlarmSystem
## 基于Kafka+Storm+HBase——风电行业实时流计算系统  
### 项目描述  
    实现风电机组数据的实时数据清洗和监控指标报警。  
    利用风场实时采集的风电机组运行数据，对数据进行实时清洗，保证数据的质量。同时处理后的数据划分为正常数据和不合理数据，分别存储到HBase中，作为历史数据日后分析。通过时间窗口对正常数据进行指标监控，指标数值超出特定界限时给出报警信息，并存储到MySQL，最后推送到Web端实时展示报警信息，展示内容包括报警信息列表（机组编号、报警时间、报警描述），以及每台风机近1小时每10分钟报警数量的曲线图。  
    数据清洗规则开放前端页面来配置写入，保存到MySQL数据库中，每次启动时加载规则。报警信息采用WebSocket主动推送的方式，一旦产生报警就推送到Web端展示。报警数量的曲线图通过ECharts图表进行绘制。  
<div align=center><img src="https://github.com/Geogy-fjq/WindPowerMonitorAndAlarmSystem/blob/master/READMEPhoto/p1.png" width="800"></div>  
   
### 项目环境  
    操作系统：Microsoft Windows 10  
    开发环境：IntelliJ IDEA 2019.3.4、TOMCAT 9、JDK 1.8  
    数据库：MYSQL 5.1.31、Hbase 2.2.2  
    大数据：Zokkeper 3.5.5、Kafka 2.11.0、Storm1.1.1  
    
### 系统设计  
1.	模拟实时数据生产  
    读取CSV文件（WT02287.csv、WT02288.csv、WT02289.csv）中的数据，保存到Kafka的Topic中，再由Storm程序实时读取。  
2.	实时数据清洗（CleanBolt）  
    数据清洗规则由Web界面进行配置，并存储数据清洗规则到MySQL数据库中，清洗规则包括数据日期、机组风速、机组功率等。在Storm程序启动时自动从数据库中加载数据清洗规则，并对数据进行实时清洗，将数据标记为正常数据和不合理数据。  
3.	数据分类存储到Hbase中（HbaseBolt）  
    清洗好的数据按照正常和不合理标记，分别存储到Hbase数据库中。  
4.	实时监控报警（MySQLBolt）  
    数据清洗后，对于正常数据监控异常指标（监控报警条件由Web界面进行配置，MySQL数据库进行存储，每次启动时加载），并保存到MySQL中记录。规则：每X秒监控X秒内发电机温度高于X度以上X次，报警信息包括（机组编号、报警时间、报警描述：过去X秒内发电机温度高于X度以上出现：N次）。  
5.	Web实时展示  
    Web页面实时展示报警信息列表（机组编号、报警时间、报警描述），采用WebSocket技术，实现报警信息的主动推送。  
    使用ECharts图表绘制近1小时每10分钟报警数量的曲线图，曲线图的绘制采用定时任务的方式，最新时间对应的曲线点实时变化，每隔十分钟刷新一次整体的曲线图。  
    在Web端提供数据清洗规则和监控报警条件的设置功能，通过导航栏右侧的“设置”按钮进入该界面，填写想要设置的数据清洗规则和监控报警条件，点击确定按钮，将规则保存到数据库中，供Storm程序启动时加载。  
### 实现过程  
    主要分为两部分工程：数据获取+数据清洗+数据存储（Gen）和Web端监控报警(Gen_Web)。  
    具体实现步骤和运行步骤见《步骤指导书》。  
### 项目成果  
1. 监控报警页面  
<div align=center><img src="https://github.com/Geogy-fjq/WindPowerMonitorAndAlarmSystem/blob/master/READMEPhoto/p14.png" width="800"></div> 
2. 设置数据清洗规则和监控报警条件  
设置页面：  
<div align=center><img src="https://github.com/Geogy-fjq/WindPowerMonitorAndAlarmSystem/blob/master/READMEPhoto/p15.png" width="800"></div> 

提交设置：  
<div align=center><img src="https://github.com/Geogy-fjq/WindPowerMonitorAndAlarmSystem/blob/master/READMEPhoto/p16.png" width="800"></div> 



