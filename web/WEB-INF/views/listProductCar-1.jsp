<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html  lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
    <title>车险列表</title>
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
                        <h5>车险列表</h5>
                        <%--<h3><a href="<c:url value='/export-product-car' />">Export</a></h3>--%>
                    </div>

                    <sec:authorize access="hasRole('AREA') or hasRole('GROUP') or hasRole('REGULAR')">
                        <div style="position: relative; height: 70px;">

                            <div class="fixed-action-btn horizontal click-to-toggle">
                                <a class="btn-floating btn-large red">
                                    <i class="material-icons">edit</i>
                                </a>
                                <ul>
                                    <%--<li><a class="btn-floating red"><i class="material-icons">insert_chart</i></a></li>--%>
                                    <li><a class="btn-floating tooltipped yellow darken-1" data-position="top" data-delay="50" data-tooltip="导入Excel"><i class="material-icons">unarchive</i></a></li>
                                    <li><a class="btn-floating tooltipped green" href="<c:url value='/add-product-car' />" data-position="top" data-delay="50" data-tooltip="添加"><i class="material-icons">add</i></a></li>
                                    <li><a class="btn-floating tooltipped blue" href="<c:url value='/export-product-car' />" data-position="top" data-delay="50" data-tooltip="导出为Excel"><i class="material-icons">archive</i></a></li>
                                </ul>
                            </div>

                            <%--<a class="btn-floating btn-large teal lighten-1 right" href="<c:url value='/add-product-car' />">--%>
                                <%--<i class="large material-icons">add</i>--%>
                            <%--</a>--%>
                        </div>
                    </sec:authorize>

                </div>

                <br>
                <div class="row">
                    <div class="col s12">
                        <c:if test="${not empty productInsList}">
                            <table class="responsive-table striped bordered">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>承保公司地市</th>
                                    <th>产险销售人员姓名</th>
                                    <th>报价公司</th>
                                    <th>险种</th>
                                    <th>投保类型</th>
                                    <th>车主</th>
                                    <th>车牌号码</th>
                                    <th>报价时间</th>
                                    <th>车辆类型</th>
                                    <th>商业险</th>
                                    <th>交强险</th>
                                    <th>车船税</th>
                                    <th>保费合计</th>
                                    <sec:authorize access="hasRole('ADMIN')">
                                        <th></th>
                                        <th></th>
                                    </sec:authorize>
                                </tr>
                                </thead>

                                <tbody>

                                <%--<tr style="background-color: #00acc1">--%>
                                    <%--<th>序号</th>--%>
                                    <%--<th>承保公司地市</th>--%>
                                    <%--<th>产险销售人员姓名</th>--%>
                                    <%--<th>报价公司</th>--%>
                                    <%--<th>险种</th>--%>
                                    <%--<th>投保类型</th>--%>
                                    <%--<th>车主</th>--%>
                                    <%--<th>车牌号码</th>--%>
                                    <%--<th>报价时间</th>--%>
                                    <%--<th>车辆类型</th>--%>
                                    <%--<th>商业险</th>--%>
                                    <%--<th>交强险</th>--%>
                                    <%--<th>车船税</th>--%>
                                    <%--<th>保费合计</th>--%>
                                    <%--<sec:authorize access="hasRole('ADMIN')">--%>
                                        <%--<th></th>--%>
                                        <%--<th></th>--%>
                                    <%--</sec:authorize>--%>
                                <%--</tr>--%>

                                <c:forEach items="${productInsList}" var="productIns" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${productIns.company}</td>
                                        <td>${productIns.employee}</td>
                                        <td>${productIns.insCompany}</td>
                                        <td>${productIns.productType}</td>
                                        <td>${productIns.insIllustration}</td>
                                        <td>${productIns.insPerson}</td>
                                        <td>${productIns.carNumber}</td>
                                        <td>${productIns.insTime}</td>
                                        <td>${productIns.carType}</td>
                                        <td>${productIns.carBusinessMoney}</td>
                                        <td>${productIns.carMandatoryMoney}</td>
                                        <td>${productIns.carTaxMoney}</td>
                                        <td>${productIns.insMoney}</td>

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

                        <c:if test="${empty productInsList}">
                            <p>请添加订单</p>
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
        (document).ready(function(){
            $('.tooltipped').tooltip({delay: 50});
            $('.modal-trigger').leanModal();
        });
    </script>

</body>
</html>
