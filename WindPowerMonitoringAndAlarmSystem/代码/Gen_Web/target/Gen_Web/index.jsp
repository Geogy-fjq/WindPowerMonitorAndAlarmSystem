<%@ page language="java" import="java.sql.*" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>首页</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        * {
            box-sizing: border-box;
        }
        /* body 样式 */
        body {
            font-family: Arial;
            margin: 0;
        }
        /* 标题 */
        .header {
            padding: 80px;
            text-align: center;
            background: #59bcb7;
            color: white;
        }
        /* 标题字体加大 */
        .header h1 {
            font-size: 40px;
        }
        /* 导航 */
        .navbar {
            overflow: hidden;
            background-color: #333;
        }
        /* 导航栏样式 */
        .navbar a {
            float: left;
            display: block;
            color: white;
            text-align: center;
            padding: 14px 20px;
            text-decoration: none;
        }
        /* 右侧链接*/
        .navbar a.right {
            float: right;
        }
        /* 鼠标移动到链接的颜色 */
        .navbar a:hover {
            background-color: #ddd;
            color: black;
        }
        /* 列容器 */
        .row {
            display: -ms-flexbox; /* IE10 */
            display: flex;
            -ms-flex-wrap: wrap; /* IE10 */
            flex-wrap: wrap;
        }
        /* 创建两个列 */
        /* 边栏 */
        .side {
            -ms-flex: 40%; /* IE10 */
            flex: 40%;
            background-color: #f1f1f1;
            padding: 20px;
        }
        /* 主要的内容区域 */
        .main {
            -ms-flex: 60%; /* IE10 */
            flex: 60%;
            background-color: white;
            padding: 20px;
        }
        /* 表格 */
        table.gridtable {
            font-family: verdana,arial,sans-serif;
            font-size:14px;
            color: black;
            border-width: 1px;
            border-color: #919191;
            border-collapse: collapse;
        }
        table.gridtable th {
            border-width: 1px;
            padding: 4px;
            text-align:center;
            border-style: groove;
            border-color: #919191;
            background-color: #dedede;
        }
        table.gridtable td {
            border-width: 1px;
            padding: 2px;
            text-align:center;
            border-style: groove;
            border-color: #919191;
            background-color: #fefefe;
        }
        /* 响应式布局 - 在屏幕设备宽度尺寸小于 700px 时, 让两栏上下堆叠显示 */
        @media screen and (max-width: 700px) {
            .row {
                flex-direction: column;
            }
        }
        /* 响应式布局 - 在屏幕设备宽度尺寸小于 400px 时, 让导航栏目上下堆叠显示 */
        @media screen and (max-width: 400px) {
            .navbar a {
                float: none;
                width: 100%;
            }
        }
    </style>
</head>
<body>
<div class="header">
    <h1>风电机组监控报警系统</h1>
</div>
<div class="navbar">
    <a href="index.jsp">首页</a>
    <a href="history.jsp">历史报警</a>
    <a href="realTime.jsp">实时监控</a>
    <a href="setUp.jsp" class="right">设置</a>
    <!-- <noframes></noframes>-->
</div>
<div class="row">
    <div class="side">
        <h2>报警列表</h2>
        <div style="height:500px; overflow-y:scroll">
            <table class="gridtable" id="view">
                <tr>
                    <th>风机编号</th>
                    <th>报警时间</th>
                    <th>报警描述</th>
                </tr>
            </table>
        </div>
    </div>
    <div class="main">
        <h2>近1小时每10分钟报警数量曲线图</h2>
        <div id="draw" style="height:500px;"></div>
    </div>
</div>
<script type="text/javascript" src="https://cdn.staticfile.org/echarts/4.3.0/echarts.min.js"></script>
<script type="text/javascript">
    //设置全局变量
    const myDate=new Date(new Date().getTime());
    Date.prototype.datetime=function(){
        return myDate.getFullYear()+'-'+('0'+(myDate.getMonth()+1)).slice(-2)+'-'+ myDate.getDate();
    };
    date=(new Date()).datetime();//实时系统日期
    times=new Array(7);//实时系统时间(近1小时每10分钟)
    counts_2287=new Array(7);//WT02287报警次数(近1小时每10分钟)
    counts_2288=new Array(7);//WT02288报警次数(近1小时每10分钟)
    counts_2289=new Array(7);//WT02289报警次数(近1小时每10分钟)
    myChart=echarts.init(document.getElementById("draw"));//设置ECharts


    //设置客户端与服务器的连接
    const ws=new WebSocket("ws://localhost:8080/Gen_Web/websocket");
    ws.onerror=function () {
        console.log("Connection error.");
    };
    ws.onopen=function () {
        console.log("Connection open...");
    };
    ws.onmessage=function (event) {
        show(event.data);
    };
    ws.onclose=function () {
        console.log("Connection closed.");
    };


    //设置ECharts的option
    const option = {
        title: {//图表标题
            text: ' '
        },
        tooltip: {
            trigger: 'axis',
        },
        legend: {
            data: ['WT02287',
                'WT02288',
                'WT02289']
        },
        color: ['#f43e49',
            '#228b22',
            '#17a3f4'],
        xAxis: {
            type: 'category',
            name: '时间',
            nameTextStyle: {frontSize: 14},
            data: times,
            boundaryGap: false,
            axisLabel: {
                interval: 0,//显示所有x轴标签
                textStyle: {
                    frontSize: 12
                }
            },
            axisLine: {
                lineStyle: {
                    width: 1
                }
            }
        },
        yAxis: {
            type: 'value',
            name: '报警次数',
            nameTextStyle: {frontSize: 14},
            minInterval: 1,//设置成1保证坐标轴分割刻度显示成整数
            axisLabel: {
                textStyle: {
                    frontSize: 12
                }
            },
            axisLine: {
                lineStyle: {
                    width: 1
                }
            }
        },
        series: [{
            name: 'WT02287',
            type: 'line', //折线图表示（生成报警次数曲线）
            //smooth: true,//平滑
            symbol: 'circle', //设置坐标点为圆形
            symbolSize: 10, //标记大小
            data: counts_2287
        }, {
            name: 'WT02288',
            type: 'line', //折线图表示（生成报警次数曲线）
            symbol: 'triangle', //设置坐标点为三角形
            symbolSize: 10, //标记大小
            data: counts_2288
        }, {
            name: 'WT02289',
            type: 'line', //折线图表示（生成报警次数曲线）
            symbol: 'rect', //设置坐标点为矩形
            symbolSize: 10, //标记大小
            data: counts_2289
        }]
    };


    //初始化折线图中的坐标值
    for(let i=0; i<=6; i++){
        counts_2287[i]=0;
        counts_2288[i]=0;
        counts_2289[i]=0;
    }
    for(let i=0; i<=6; i++){
        const myTime=new Date(new Date().getTime()+600000-i*600000);
        Date.prototype.datetime=function(){
            return myTime.getHours()+':'+('0'+(myTime.getMinutes())).slice(-2)+':'+('0'+(myTime.getSeconds())).slice(-2);
        };
        const now_time=(new Date()).datetime();
        times[6-i]=now_time;
    }

    //每隔十分钟更新一次
    const int=self.setInterval("clock()",600000);
    function clock(){
        if(counts_2287[5]!=counts_2287[6] && counts_2288[5]!=counts_2288[6] && counts_2289[5]!=counts_2289[6]){
            for(let i=0; i<=5; i++){
                counts_2287[i]=counts_2287[i+1];
                counts_2288[i]=counts_2288[i+1];
                counts_2289[i]=counts_2289[i+1];
                times[i]=times[i+1];
            }
            counts_2287[6]=0;
            counts_2288[6]=0;
            counts_2289[6]=0;
            const myTime=new Date(new Date((date+' '+times[6]).substring(0,19).replace(/-/g,'/')).getTime()+600000);
            Date.prototype.datetime=function(){
                return myTime.getHours()+':'+('0'+(myTime.getMinutes())).slice(-2)+':'+('0'+(myTime.getSeconds())).slice(-2);
            };
            const now_time=(new Date()).datetime();
            times[6]=now_time;
            //为Echarts对象加载数据
            myChart.setOption(option);
        }
    }


    //显示将从MySQL接收到的数据
    function show(data) {
        //获取从MySQL接收到的数据
        const line=data.split(",");
        const FAN_NO=line[0];
        const DATA_DATE=line[1];
        const describe=line[2];
        //1.显示报警信息列表
        const tab=document.getElementById("view");
        const tr=document.createElement("tr");
        const td1=document.createElement("td");
        const td2=document.createElement("td");
        const td3=document.createElement("td");
        td1.style.width="75px";
        td2.style.width="105px";
        td3.style.width="285px";
        td1.innerHTML=FAN_NO;
        td2.innerHTML=DATA_DATE;
        td3.innerHTML=describe;
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tab.appendChild(tr);
        //2.使用ECharts绘制近1小时每10分钟报警数量的曲线图
        const timestamp_now=new Date((date+' '+times[5]).substring(0,19).replace(/-/g,'/')).getTime();//必须把日期'-'转为'/'
        const timestamp_get=new Date(DATA_DATE.substring(0,19).replace(/-/g,'/')).getTime();//必须把日期'-'转为'/'
        const sub=timestamp_now-timestamp_get;//报警时间与当前系统时间的时间差
        if(sub>0 && sub<3600000){//在记录的系统时间的过去一小时内
            const index=parseInt(sub/600000);
            const count_get=parseInt(describe.substring(18,describe.length-1));
            if(FAN_NO=="WT02287"){
                counts_2287[5-index]+=count_get;
            }else if(FAN_NO=="WT02288"){
                counts_2288[5-index]+=count_get;
            }else if(FAN_NO=="WT02289"){
                counts_2289[5-index]+=count_get;
            }
        }else if(sub<0 && sub>-600000){//在记录的系统时间之后十分钟内
            const count_get=parseInt(describe.substring(18,describe.length-1));
            if(FAN_NO=="WT02287"){
                counts_2287[6]+=count_get;
            }else if(FAN_NO=="WT02288"){
                counts_2288[6]+=count_get;
            }else if(FAN_NO=="WT02289"){
                counts_2289[6]+=count_get;
            }
        }
        //为Echarts对象加载数据
        myChart.setOption(option);
    };
</script>
</body>
</html>