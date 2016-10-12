<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
  <title>泰允升网络科技有限公司</title>
  <link rel="stylesheet" href="<c:url value='/static/css/material-icons.css' />" media="screen,projection" />
  <link rel="stylesheet" href="<c:url value='/static/css/materialize.css' />" media="screen,projection" />
  <link rel="stylesheet"  href="<c:url value='/static/css/style.css' />" media="screen,projection" />
</head>

<body>
<head>
  <head>
    <nav class="blue lighten-1" role="navigation">
      <div class="nav-wrapper container">
        <a id="logo-container" href="index.html" class="brand-logo white-text">泰允升科技</a>

        <ul class="right hide-on-med-and-down">
          <li class="active"><a href="index.html" class="white-text">首页</a></li>
          <li><a href="#serv" class="white-text">服务</a></li>
          <li><a href="#about_us" class="white-text">关于我们</a></li>
          <li><a href="#connect_us" class="white-text">联系我们</a></li>
          <li><a href="/login" class="waves-effect waves-light btn blue lighten-1" style="border-radius:1.5em;">登录</a></li>
        </ul>

        <ul id="nav-mobile" class="side-nav">
          <li class="active"><a href="index.html">首页</a></li>
          <li class="no-padding">
            <ul class="collapsible collapsible-accordion">
              <li>
                <a class="collapsible-header">服务<i class="material-icons right">arrow_drop_down</i></a>
                <div class="collapsible-body">
                  <ul>
                    <li><a href="page/tech/tech_serv.html">技术服务</a></li>
                    <li><a href="page/car/car_serv.html">汽车服务</a></li>
                    <li><a href="page/mesg/mesg_serv.html">信息服务</a></li>
                  </ul>
                </div>
              </li>
              <li><a href="index.html#about_us">关于我们</a></li>
              <li><a href="index.html#connect_us">联系我们</a></li>
              <li><a href="/login">登录</a></li>
            </ul>
          </li>
        </ul>

        <a href="#" data-activates="nav-mobile" class="button-collapse white-text"><i class="material-icons">menu</i></a>
      </div>
    </nav>
  </head>

  <main>
    <div id="index-banner" class="parallax-container valign-wrapper">
      <div class="section no-pad-bot">
        <div class="container">
          <br><br>
          <h1 class="header center blue-text">Internet Plus</h1>
          <div class="row center">
            <h5 class="header col s12 light">互联网+时代，我们为你提供更优质服务</h5>
          </div>
          <div class="row center">
            <a href="#about_us" id="download-button" class="btn-large waves-effect waves-light blue">了解更多关于我们</a>
          </div>
          <br><br>

        </div>
      </div>
      <div class="parallax"><img src="static/image/index/sea.jpg" alt=""></div>
    </div>

    <div class="container">
      <div class="section">

        <div id="serv" class="row">
          <div class="col s12 m4">
            <div class="icon-block">
              <h2 class="center blue-text text-lighten-1"><i class="material-icons">memory</i></h2>
              <h5 class="center">技术服务</h5>
              <p class="light">包括网络技术开发、计算机技术开发服务、通信技术研究开发、软件开发等服务。</p>
              <div class="row center">
                <a href="page/tech/tech_serv.html" class="btn waves-effect waves-light blue lighten-1">了解更多</a>
              </div>
            </div>
          </div>

          <div class="col s12 m4">
            <div class="icon-block">
              <h2 class="center blue-text text-lighten-1"><i class="material-icons">drive_eta</i></h2>
              <h5 class="center">汽车服务</h5>

              <p class="light">包括代办汽车年审、过户手续、代缴汽车违章罚款、车船税手续等服务。</p>
              <div class="row center">
                <a href="page/car/car_serv.html" class="btn waves-effect waves-light blue lighten-1">了解更多</a>
              </div>
            </div>
          </div>

          <div class="col s12 m4">
            <div class="icon-block">
              <h2 class="center blue-text text-lighten-1"><i class="material-icons">polymer</i></h2>
              <h5 class="center">信息服务</h5>

              <p class="light">包括房地产信息、旅游信息、商品经济信息、室内外装饰工程设计、施工等咨询服务。</p>
              <div class="row center">
                <a href="page/mesg/mesg_serv.html" class="btn waves-effect waves-light blue lighten-1">了解更多</a>
              </div>
            </div>
          </div>
        </div>

      </div>
      <br><br>

      <div class="section">
        <div class="row" style="background: #ececec;">
          <div id="about_us" class="col l6 s12">
            <h5>公司介绍</h5>
            <p>泰允升网络科技公司，是紧贴“互联网 + ”时代的步伐，以创新和提供优质服务为宗旨，致力于打造多领域结合经营服务平台的企业。</p>
            <blockquote>
              公司业务主要有三大模块 <br>
              <strong>1.技术服务</strong> <!-- <em>包括网络技术开发、计算机技术开发服务、通信技术研究开发、软件开发等服务。</em> --><br>
              <strong>2.汽车服务</strong> <!-- <em>包括代办汽车年审、过户手续、代缴汽车违章罚款、车船税手续等服务。</em> --><br>
              <strong>3.信息服务</strong> <!-- <em>包括房地产信息、旅游信息、商品经济信息、室内外装饰工程设计、施工等咨询服务。</em> --><br>
            </blockquote>
            <p><span style="font-weight:500;">科技也能不冰冷</span> <br>
              生活节奏快，科技日新月异，来泰允升科技，让科技走进生活。</p>

          </div>
          <div class="col l6 s12 center">
            <img class="responsive-img" src="./image/index/tech.png">
          </div>
        </div>
      </div>

    </div>
  </main>

  <footer class="page-footer blue lighten-1">
    <div class="container">
      <div class="row">
        <div id="connect_us" class="col l6 s12 white-text">
          <h3><i class="mdi-content-send"></i></h3>
          <h4>联系我们</h4>
          <p class="left-align light"><i class="material-icons left">location_on</i>地址：广东省江门市新会区会城悦龙新村13座108</p>
          <p class="left-align light"><i class="material-icons left">email</i>邮箱：follow@taiyunsheng.com</p>
          <p class="left-align light"><i class="material-icons left">dialpad</i>邮编：529100</p>
        </div>
        <div class="col l6 s12">
          <h4 class="white-text">关注我们</h4>
          <img class="responsive-img center" style="width:125px; height:125px;" src="static/image/index/qrcode.jpg">
        </div>
      </div>
    </div>
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