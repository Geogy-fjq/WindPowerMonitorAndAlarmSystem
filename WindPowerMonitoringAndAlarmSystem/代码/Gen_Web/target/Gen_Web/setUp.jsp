<%@ page language="java" import="java.sql.*" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>设置</title>
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
            -ms-flex: 30%; /* IE10 */
            flex: 30%;
            background-color: #f1f1f1;
            padding: 20px;
        }
        /* 主要的内容区域 */
        .main {
            -ms-flex: 70%; /* IE10 */
            flex: 70%;
            background-color: white;
            padding: 20px;
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
        <h2>设置需知</h2>
        <div style="height:500px;overflow-y:scroll">
            <label style="font-size:25px">数据清洗规则</label>
            <div style="height:200px">
                <div style="height:25px"></div>
                <div style="height:50px">
                    <label>数据日期：</label>
                    <label>设置是否只监控当日数据</label>
                </div>
                <div style="height:50px">
                    <label>风速范围：</label>
                    <label>设置合理风速的范围</label>
                </div>
                <div style="height:50px">
                    <label>功率范围：</label>
                    <label>设置合理功率的范围</label>
                </div>
            </div>
            <label style="font-size:25px">监控报警条件</label>
            <div style="height:200px">
                <div style="height:25px"></div>
                <div style="height:50px">
                    <label>监控对象：</label>
                    <label>监控的风机数据对象</label>
                </div>
                <div style="height:50px">
                    <label>监控周期：</label>
                    <label>监控过去X秒内的数据</label>
                </div>
                <div style="height:50px">
                    <label>限制温度：</label>
                    <label>当监控对象的温度超过X度时报警</label>
                </div>
                <div style="height:50px">
                    <label>限制次数：</label>
                    <label>当报警次数超过X次时进行记录</label>
                </div>
            </div>
        </div>
    </div>
    <div class="main">
        <h2>数据清洗规则</h2>
        <div style="height:175px">
            <div style="height:25px"></div>
            <div style="height:50px">
                <label>数据日期：</label>
                <span><select class="select" name="DATA_DATE" id="DATA_DATE">
                    <option value="0">--请选择--</option>
                    <option value="Today">当天数据</option>
                    <option value="EveryDay">全部数据</option>
                </select></span>
            </div>
            <div style="height:50px">
                <label>风速范围：</label>
                <input type="text" name="WIND_SPEED1" id="WIND_SPEED1">
                <label> ~ </label>
                <input type="text" name="WIND_SPEED1" id="WIND_SPEED2">
            </div>
            <div style="height:50px">
                <label>功率范围：</label>
                <input type="text" name="POWER1" id="POWER1">
                <label> ~ </label>
                <input type="text" name="POWER1" id="POWER2">
            </div>
        </div>
        <h2>监控报警条件</h2>
        <div style="height:225px">
            <div style="height:25px"></div>
            <div style="height:50px">
                <label>监控对象：</label>
                <span><select class="select" name="object" id="object">
                    <option value="0">--请选择--</option>
                    <option value="ROTOR_GROUP_T">发电机温度</option>
                    <option value="ENVIRON_T">环境温度</option>
                    <option value="WT_T">机舱温度</option>
                </select></span>
            </div>
            <div style="height:50px">
                <label>监控周期：</label>
                <input type="text" name="cycle" id="cycle">
            </div>
            <div style="height:50px">
                <label>限制温度：</label>
                <input type="text" name="temperature" id="temperature">
            </div>
            <div style="height:50px">
                <label>限制次数：</label>
                <input type="text" name="count" id="count">
            </div>
        </div>
        <div style="height:35px">
            <div style="height:10px"></div>
            <label style="display:inline-block;width:375px;"> </label>
            <input type="button" value="确定" onclick="determine()" style="height:25px;width:60px">
        </div>
    </div>
</div>
<script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript">
function determine() {
    const DATA_DATE=document.getElementById("DATA_DATE").options[document.getElementById("DATA_DATE").selectedIndex].value;
    const WIND_SPEED1=document.getElementById("WIND_SPEED1").value;
    const WIND_SPEED2=document.getElementById("WIND_SPEED2").value;
    const POWER1=document.getElementById("POWER1").value;
    const POWER2=document.getElementById("POWER2").value;
    const object=document.getElementById("object").options[document.getElementById("object").selectedIndex].value;
    const cycle=document.getElementById("cycle").value;
    const temperature=document.getElementById("temperature").value;
    const count=document.getElementById("count").value;
    if(DATA_DATE!=0 && object!=0 && WIND_SPEED1!="" && WIND_SPEED2!="" && POWER1!="" && POWER2!="" && cycle!="" && temperature!="" && count!="") {
        $.ajax({
            url:"http://localhost:8080/Gen_Web/determineServlet?number="+Math.random(),//要提交的路径
            type:"post",//请求方法
            data:{//要发送的数据
                "DATA_DATE":DATA_DATE,
                "WIND_SPEED1":WIND_SPEED1,
                "WIND_SPEED2":WIND_SPEED2,
                "POWER1":POWER1,
                "POWER2":POWER2,
                "object":object,
                "cycle":cycle,
                "temperature":temperature,
                "count":count
            },
            success:function(result){
                alert("设置成功！");
            },
            error:function(result){
                alert("设置失败!");
            }
        });
    }
    else{
        alert("输入错误!");
    }
};

</script>
</body>
</html>