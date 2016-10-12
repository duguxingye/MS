<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
    <title>修改密码成功</title>
    <link rel="stylesheet" href="<c:url value='/static/css/materialize.css' />" media="screen,projection" />
    <link rel="stylesheet"  href="<c:url value='/static/css/style.css' />" media="screen,projection" />
</head>
<body>

    <main>
        <div class="container">

            <div class="row" style="padding-top: 15%;">
                <div class="card" style="margin: 0 auto;max-width: 500px;">
                    <div class="card-image">
                        <img src="static/image/index/done.png">
                        <span class="card-title">修改成功</span>
                    </div>
                    <div class="card-content">
                        <p><span id="showTime">5</span>秒后将自动退出登录</p>
                    </div>
                    <div class="card-action">
                        <a href="<c:url value="/logout" />">确定退出</a>
                    </div>
                </div>
            </div>

        </div>
    </main>

    <script src="static/js/jquery-2.1.1.min.js"></script>
    <script src="static/js/materialize.js"></script>
    <script src="static/js/init.js"></script>
    <script type="text/javascript">
        var t = 5;
        function showTime(){
            t = t - 1;
            document.getElementById('showTime').innerHTML = t;
            if(t == 0){
                location.href = '/logout';
            }
            setTimeout("showTime()", 1000);
        }
        showTime();
    </script>

</body>
</html>
