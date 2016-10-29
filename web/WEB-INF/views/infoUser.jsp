<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html  lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
    <title>会员信息</title>
    <link rel="stylesheet" href="<c:url value='/static/css/material-icons.css' />" media="screen,projection" />
    <link rel="stylesheet" href="<c:url value='/static/css/ms-materialize.css' />" media="screen,projection" />
    <link rel="stylesheet"  href="<c:url value='/static/css/style.css' />" media="screen,projection" />
</head>
<body>
    <%@include file="header.jsp"%>

    <main>
        <div class="container">
            <div class="section">
                <h5>会员信息</h5>
                <c:if test="${not empty users}">
                    <p>name : ${users.name}</p>
                    <p>phone : ${users.phone}</p>
                    <p>jobId : ${users.jobId}</p>
                </c:if>
                <a  href="<c:url value='/reset-pwd-${users.jobId}' />" class="waves-effect waves-light btn"><i class="material-icons left">edit</i>修改密码</a>
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
