<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
    <title>会员管理</title>
    <link rel="stylesheet" href="<c:url value='/static/css/material-icons.css' />" media="screen,projection" />
    <link rel="stylesheet" href="<c:url value='/static/css/ms-materialize.css' />" media="screen,projection" />
    <link rel="stylesheet"  href="<c:url value='/static/css/style.css' />" media="screen,projection" />
</head>
<body>
    <%@include file="header.jsp"%>

    <main>
        <div class="container">
            <div class="section">

                    <h2 class="header">新增会员</h2>
                    <mvc:form method="POST" modelAttribute="user">

                        <mvc:input type="hidden" path="id" id="id"/>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请输入工号" type="text" path="jobId" id="jobId" class="validate" />
                                <label for="jobId">工号</label>
                            </div>
                            <div class="col s12 m6">
                                <mvc:errors path="jobId" class="red white-text"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请输入密码" type="password" path="password" id="password" class="validate" />
                                <label for="password">密码</label>
                            </div>
                            <div class="col s12 m6">
                                <mvc:errors path="password" class="input-field red"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请再次输入密码" type="password" path="retypePassword" id="retypePassword" class="validate" />
                                <label for="retypePassword">确认密码</label>
                            </div>
                            <div class="col s12 m6">
                                <mvc:errors path="retypePassword" class="input-field red"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请输入姓名" type="text" path="name" id="name" class="validate"/>
                                <label for="name">姓名</label>
                            </div>
                            <div class="col s12 m6">
                                <mvc:errors path="name" class="input-field red"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请输入手机号码" type="text" path="phone" id="phone" class="validate" />
                                <label for="phone">手机号码</label>
                            </div>
                            <div class="col s12 m6">
                                <mvc:errors path="phone" class="input-field red"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请输入上级领导工号" type="text"  path="leaderId" id="leaderId" class="validate" />
                                <label for="leaderId">上级领导工号</label>
                            </div>
                            <div class="col s12 m6">
                                <mvc:errors path="leaderId" class="input-field red"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col s12 m6">
                                <label>类型</label>
                                <mvc:select path="userProfile" items="${profile}" multiple="false" itemValue="id" itemLabel="type" class="browser-default" />
                            </div>

                            <div class="col s12 m6">
                                <mvc:errors path="userProfile" class="input-field red"/>
                            </div>
                        </div>

                        <mvc:input type="hidden" path="hasLocked" id="hasLocked"/>

                        <br>
                        <button class="btn waves-effect waves-light" type="submit" name="action">确定新增会员</button>
                        <a style="padding-left: 50px;" href="<c:url value='/list-user' />">取消</a>
                    </mvc:form>

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