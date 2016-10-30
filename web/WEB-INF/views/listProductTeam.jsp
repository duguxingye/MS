<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html  lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
    <title>列表</title>
    <link rel="stylesheet" href="<c:url value='/static/css/material-icons.css' />" media="screen,projection" />
    <link rel="stylesheet" href="<c:url value='/static/css/ms-materialize.css' />" media="screen,projection" />
    <link rel="stylesheet"  href="<c:url value='/static/css/style.css' />" media="screen,projection" />
</head>
<body>
    <%@include file="header.jsp"%>

    <main>
        <div class="section">

            <h5 style="padding-left: 10px;">车险列表</h5>
            <div class="row">
                <div class="col s12">
                    <c:if test="${not empty productInsList}">
                        <table class="bordered">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>承保公司地市</th>
                                <th>产险销售人员姓名</th>
                                <th>报价公司</th>
                                <th>险种</th>
                                <th>投保类型</th>
                                <th>报价时间</th>
                                <th>车辆类型</th>
                                <th>保费合计</th>
                                <sec:authorize access="hasRole('ADMIN')">
                                    <th></th>
                                    <th></th>
                                </sec:authorize>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${productInsList}" var="productIns" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td>${productIns.company}</td>
                                    <td>${productIns.employee}</td>
                                    <td>${productIns.insCompany}</td>
                                    <td>${productIns.productType}</td>
                                    <td>${productIns.insIllustration}</td>
                                    <td>${productIns.insTime}</td>
                                    <td>${productIns.carType}</td>
                                    <td>${productIns.insMoney}</td>
                                    <sec:authorize access="hasRole('ADMIN')">
                                        <td>
                                            <a href="<c:url value='/edit-product-team-${productIns.id}' />" class="waves-effect waves-light btn">修改</a>
                                        </td>
                                        <td>
                                            <a href="#${productIns.id}" class="waves-effect waves-light btn modal-trigger">删除</a>
                                            <div id="${productIns.id}" class="modal">
                                                <div class="modal-content">
                                                    <h4>确认删除？</h4>
                                                    <p>一旦删除，无法撤销！确定想要删除？</p>
                                                </div>
                                                <div class="modal-footer">
                                                    <a href="#" class=" modal-action modal-close waves-effect waves-green btn-flat">取消</a>
                                                    <a href="<c:url value='/delete-product-team-${productIns.id}' />" class=" modal-action modal-close waves-effect waves-green btn">确认</a>
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

            <sec:authorize access="hasRole('AREA') or hasRole('GROUP') or hasRole('REGULAR')">
                <div style="position: relative; height: 70px;">
                    <div class="fixed-action-btn horizontal click-to-toggle">
                        <a class="btn-floating btn-large red">
                            <i class="material-icons">edit</i>
                        </a>
                        <ul>
                            <li><a class="btn-floating tooltipped green" href="<c:url value='/upload-product-team' />" data-position="top" data-delay="50" data-tooltip="导入Excel"><i class="material-icons">unarchive</i></a></li>
                            <li><a class="btn-floating tooltipped red" href="<c:url value='/add-product-team' />" data-position="top" data-delay="50" data-tooltip="添加"><i class="material-icons">add</i></a></li>
                            <li><a class="btn-floating tooltipped blue" href="<c:url value='/export-product-team' />" data-position="top" data-delay="50" data-tooltip="导出为Excel"><i class="material-icons">archive</i></a></li>
                        </ul>
                    </div>
                </div>
            </sec:authorize>

            <sec:authorize access="hasRole('ADMIN')">
                <div style="position: relative; height: 70px;">
                    <div class="fixed-action-btn">
                        <a class="btn-floating btn-large red" href="<c:url value='/export-product-team' />">
                            <i class="material-icons">archive</i>
                        </a>
                    </div>
                </div>
            </sec:authorize>

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
            $('.tooltipped').tooltip({delay: 50});
            $('.modal-trigger').leanModal();
        });
    </script>

</body>
</html>
