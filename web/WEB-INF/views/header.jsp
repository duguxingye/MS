<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header>

    <nav class="top-nav blue lighten-1" style="background-image: url('/static/image/index/main-header.png'); background-size:cover;">
        <div class="container">
            <div class="nav-wrapper"><a class="page-title">泰允升</a></div>
        </div>
    </nav>

    <div class="container">
        <a href="#" data-activates="nav-mobile" class="button-collapse top-nav full hide-on-large-only"><i class="material-icons">menu</i></a>
    </div>

    <ul id="nav-mobile" class="side-nav fixed" style="overflow-x: hidden;overflow-y: hidden;">
        <li>
            <div class="userView">
                <img class="background" src="static/image/index/nav-bg.png">
                <a href="#"><img class="circle" src="static/image/index/user.png"></a>
                <a href="#"><span class="white-text name-info">${loginUser}</span></a>
                <sec:authorize access="hasRole('ADMIN')">
                    <a href="#"><span class="white-text type-info">欢迎，您的身份是管理员</span></a>
                </sec:authorize>
                <sec:authorize access="hasRole('AREA')">
                    <a href="#"><span class="white-text type-info">欢迎，您的身份是行销总监</span></a>
                </sec:authorize>
                <sec:authorize access="hasRole('GROUP')">
                    <a href="#"><span class="white-text type-info">欢迎，您的身份是行销部经理</span></a>
                </sec:authorize>
                <sec:authorize access="hasRole('REGULAR')">
                    <a href="#"><span class="white-text type-info">欢迎，您的身份是行销经理</span></a>
                </sec:authorize>
            </div>
        </li>
        <li>
            <div class="card red center" style="margin:5px;">
                <a class="white-text bold" href="<c:url value="/logout" />">退出登录</a>
            </div>
        </li>
        <li class="bold"><a href="<c:url value='/info-user' />" class="waves-effect waves-teal">会员信息</a></li>
        <sec:authorize access="hasRole('GROUP') or hasRole('AREA') or hasRole('ADMIN')">
            <li class="bold"><a href="<c:url value='/list-user' />" class="waves-effect waves-teal">会员管理</a></li>
        </sec:authorize>
        <li class="no-padding">
            <ul class="collapsible collapsible-accordion">
                <li class="bold"><a class="collapsible-header  waves-effect waves-teal">产品管理</a>
                    <div class="collapsible-body">
                        <ul>
                            <li><a href="<c:url value='/list-product-car' />">车险</a></li>
                            <li><a href="<c:url value='/list-product-person' />">寿险</a></li>
                            <li><a href="<c:url value='/list-product-team' />">团险</a></li>
                            <li><a href="<c:url value='/list-product-card' />">卡保险</a></li>
                            <li><a href="#">更多产品</a></li>
                        </ul>
                    </div>
                </li>
            </ul>
        </li>
    </ul>

</header>
