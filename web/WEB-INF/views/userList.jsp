<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html  lang="zh">
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
                <div class="row" style="margin: auto">
                    <div class="col s6">
                        <h5>会员列表</h5>
                    </div>

                    <sec:authorize access="hasRole('ADMIN') or hasRole('AREA') or hasRole('GROUP')">
                        <div class="col s6">
                            <a class="btn-floating btn-large red right" href="<c:url value='/addUser' />">
                                <i class="large material-icons">add</i>
                            </a>
                        </div>
                    </sec:authorize>

                </div>


                <br>
                <div class="row">
                    <div class="col s12">
                        <c:if test="${not empty users}">
                        <table class="bordered">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Phone</th>
                                    <th>JOB ID</th>
                                    <sec:authorize access="hasRole('ADMIN')">
                                        <th></th>
                                        <th></th>
                                    </sec:authorize>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach items="${users}" var="user">
                                    <tr>
                                        <td>${user.name}</td>
                                        <td>${user.phone}</td>
                                        <td>${user.jobId}</td>
                                        <sec:authorize access="hasRole('ADMIN')">
                                            <td>
                                                <a href="<c:url value='/edit-user-${user.jobId}' />" class="waves-effect waves-light btn">修改</a>
                                            </td>
                                            <td>
                                                <a href="#${user.jobId}" class="waves-effect waves-light btn modal-trigger">删除</a>
                                                <div id="${user.jobId}" class="modal">
                                                    <div class="modal-content">
                                                        <h4>确认删除${user.jobId}？</h4>
                                                        <p>一旦删除，无法撤销！确定想要删除？</p>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <a href="#" class=" modal-action modal-close waves-effect waves-green btn-flat">取消</a>
                                                        <a href="<c:url value='/delete-user-${user.jobId}' />" class=" modal-action modal-close waves-effect waves-green btn">确认</a>
                                                    </div>
                                                </div>
                                            </td>
                                        </sec:authorize>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        </c:if>

                        <c:if test="${empty users}">
                            <p>请添加用户</p>
                        </c:if>
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
    <script>
        $(document).ready(function(){
            $('.modal-trigger').leanModal();
        });
    </script>

</body>
</html>