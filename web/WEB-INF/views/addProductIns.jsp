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
    <title>产品管理</title>
    <link rel="stylesheet" href="<c:url value='/static/css/material-icons.css' />" media="screen,projection" />
    <link rel="stylesheet" href="<c:url value='/static/css/ms-materialize.css' />" media="screen,projection" />
    <link rel="stylesheet"  href="<c:url value='/static/css/style.css' />" media="screen,projection" />
</head>
<body>
    <%@include file="header.jsp"%>

    <main>
        <div class="container">
            <div class="section">

                <h2 class="header">添加订单</h2>
                <mvc:form method="POST" modelAttribute="productIns">

                    <mvc:input type="hidden" path="id" id="id"/>

                    <div class="row">
                        <div class="input-field col s12 m6">
                            <mvc:input placeholder="请输入承保公司地市" type="text" path="company" id="company" class="validate" />
                            <label for="company">承保公司地市</label>
                        </div>
                        <div class="col s12 m6">
                            <mvc:errors path="company" class="input-field red"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="input-field col s12 m6">
                            <mvc:input placeholder="请输入产险销售人员姓名" type="text" path="employee" id="employee" class="validate" />
                            <label for="employee">产险销售人员姓名</label>
                        </div>
                        <div class="col s12 m6">
                            <mvc:errors path="employee" class="input-field red"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="input-field col s12 m6">
                            <mvc:input placeholder="请输入产险销售人员工号" type="text" path="employeeId" id="employeeId" class="validate" />
                            <label for="employeeId">产险销售人员工号</label>
                        </div>
                        <div class="col s12 m6">
                            <mvc:errors path="employeeId" class="input-field red"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="input-field col s12 m6">
                            <mvc:input placeholder="请输入报价公司" type="text" path="insCompany" id="insCompany" class="validate"/>
                            <label for="insCompany">报价公司</label>
                        </div>
                        <div class="col s12 m6">
                            <mvc:errors path="insCompany" class="input-field red"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="input-field col s12 m6">
                            <mvc:input placeholder="请输入投保类型" type="text" path="insIllustration" id="insIllustration" class="validate" />
                            <label for="insIllustration">投保类型</label>
                        </div>
                        <div class="col s12 m6">
                            <mvc:errors path="insIllustration" class="input-field red"/>
                        </div>
                    </div>

                    <c:if test="${person}">
                        <mvc:input type="hidden" value="person" path="insType" id="insType"/>
                        <mvc:input type="hidden" value="寿险" path="productType" id="productType" />
                    </c:if>

                    <c:if test="${card}">
                        <mvc:input type="hidden" value="card" path="insType" id="insType"/>
                        <mvc:input type="hidden" value="卡保险" path="productType" id="productType" />
                    </c:if>

                    <c:if test="${team}">
                        <mvc:input type="hidden" value="team" path="insType" id="insType"/>
                        <mvc:input type="hidden" value="团险" path="productType" id="productType" />
                    </c:if>


                    <c:if test="${car}">

                        <mvc:input type="hidden" value="car" path="insType" id="insType"/>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请输入车主姓名" type="text" path="insPerson" id="insPerson" class="validate" />
                                <label for="insPerson">车主姓名</label>
                            </div>
                                <div class="col s12 m6">
                                <mvc:errors path="insPerson" class="input-field red"/>
                                </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请输入车牌号码" type="text" path="carNumber" id="carNumber" class="validate" />
                                <label for="carNumber">车牌号码</label>
                            </div>
                            <div class="col s12 m6">
                                <mvc:errors path="carNumber" class="input-field red"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请输入车辆类型" type="text" path="carType" id="carType" class="validate" />
                                <label for="carType">车辆类型</label>
                            </div>
                            <div class="col s12 m6">
                                <mvc:errors path="carType" class="input-field red"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请输入商业性金额" type="text" path="carBusinessMoney" id="carBusinessMoney" class="validate" />
                                <label for="carBusinessMoney">商业性金额</label>
                            </div>
                            <div class="col s12 m6">
                                <mvc:errors path="carBusinessMoney" class="input-field red"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请输入交强险金额" type="text" path="carMandatoryMoney" id="carMandatoryMoney" class="validate" />
                                <label for="carMandatoryMoney">交强险金额</label>
                            </div>
                            <div class="col s12 m6">
                                <mvc:errors path="carMandatoryMoney" class="input-field red"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="input-field col s12 m6">
                                <mvc:input placeholder="请输入车船税金额" type="text" path="carTaxMoney" id="carTaxMoney" class="validate" />
                                <label for="carTaxMoney">车船税金额</label>
                            </div>
                            <div class="col s12 m6">
                                <mvc:errors path="carTaxMoney" class="input-field red"/>
                            </div>
                        </div>
                    </c:if>

                    <div class="row">
                        <div class="input-field col s12 m6">
                            <mvc:input placeholder="请输入保费合计金额" type="text"  path="insMoney" id="insMoney" class="validate required" />
                            <label for="insMoney">保费合计金额</label>
                        </div>
                        <div class="col s12 m6">
                            <mvc:errors path="insMoney" class="input-field red"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col s12 m6">
                            <label for="insTime" class="active">报价时间</label>
                            <mvc:input type="date"  path="insTime" id="insTime" class="datepicker" />
                        </div>
                        <div class="col s12 m6">
                            <mvc:errors path="insTime" class="input-field red"/>
                        </div>
                    </div>

                    <br>
                    <button class="btn waves-effect waves-light" type="submit" name="action">确定</button>
                    <a style="padding-left: 50px;" href="#">取消</a>
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
    <script>
        $('.datepicker').pickadate({
            selectMonths: true,
            selectYears: 16
        });
    </script>

</body>
</html>