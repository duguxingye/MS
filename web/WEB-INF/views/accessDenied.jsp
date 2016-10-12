<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html  lang="zh">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
	<title>权限问题</title>
	<link rel="stylesheet" href="<c:url value='/static/css/material-icons.css' />" media="screen,projection">
	<link rel="stylesheet" href="<c:url value='/static/css/ms-materialize.css' />" media="screen,projection" />
	<link rel="stylesheet"  href="<c:url value='/static/css/style.css' />" media="screen,projection" />
</head>
</head>
<body>
    <%@include file="header.jsp"%>

    <main>
        <div class="container">
            <div class="section">

                <div class="row" style="padding-top: 15%;">
                    <div class="card" style="margin: 0 auto;max-width: 500px;">
                        <div class="card-image">
                            <img src="static/image/index/done.png">
                            <span class="card-title">权限不够</span>
                        </div>
                        <div class="card-content">
                            <span>你好 <strong>${loggedinuser}</strong>，你没有权限浏览本页面</span>
                        </div>
                        <div class="card-action">
                            <a href="javascript:history.go(-1)">返回</a>
                            <a href="<c:url value="/logout" />">退出</a>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </main>

    <footer>
        <div class="footer-copyright">
            <div class="container center">
                版权所有 © 泰允升网络科技有限公司
            </div>
        </div>
    </footer>

    <script src="static/js/jquery-2.1.1.min.js"></script>
    <script src="static/js/materialize.js"></script>
    <script src="static/js/init.js"></script>

</body>
</html>