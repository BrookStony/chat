<%@ page import="grails.util.Environment" contentType="text/html;charset=UTF-8" %><!DOCTYPE html>
<html id="ng-app" ng-app="chat">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon-chat.ico')}">
    <!-- Custom Fonts -->
    <link href="font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <title>${message(code: 'base.title')}</title>
    <r:require modules="app-css,app-js"/>
    <r:layoutResources/>
    <r:script>
        $(document).ready(function () {
            $("html").niceScroll({cursorwidth: 10});
            $('#side-menu').metisMenu();
        });
        //Loads the correct sidebar on window load,
        //collapses the sidebar on window resize.
        // Sets the min-height of #page-wrapper to window size
//        $(function() {
//            $(window).bind("load resize", function() {
//                topOffset = 50;
//                width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
//                if (width < 768) {
//                    $('div.navbar-collapse').addClass('collapse')
//                    topOffset = 100; // 2-row-menu
//                } else {
//                    $('div.navbar-collapse').removeClass('collapse')
//                }
//
//                height = (this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height;
//                height = height - topOffset;
//                if (height < 1) height = 1;
//                if (height > topOffset) {
//                    $("#page-wrapper").css("min-height", (height) + "px");
//                }
//            })
//        });
    </r:script>
</head>

<body>

%{--<div ng-show="loading" class="top-loading">--}%
    %{--<div class="well well-sm">--}%
        %{--<img class="loading" src="images/spinner.gif"/>--}%
        %{--<label>处理中...</label>--}%
    %{--</div>--}%
%{--</div>--}%

<div id="wrapper">

<!-- Navigation -->
    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a id="brand" class="navbar-brand" href="#"><i class="fa fa-wechat"></i><b> Chat | 微信管理平台 v1.0</b></a>
    </div>
    <!-- /.navbar-header -->

    <ul class="nav navbar-top-links navbar-right">
    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
            <i class="fa fa-bell fa-fw"></i>告警<i class="fa fa-caret-down"></i>
        </a>
        <ul class="dropdown-menu dropdown-alerts">
            <li>
                <a href="#">
                    <div>
                        <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                        <span class="pull-right text-muted small">12 minutes ago</span>
                    </div>
                </a>
            </li>
            <li class="divider"></li>
            <li>
                <a class="text-center" href="#">
                    <strong>See All Alerts</strong>
                    <i class="fa fa-angle-right"></i>
                </a>
            </li>
        </ul>
        <!-- /.dropdown-alerts -->
    </li>
    <!-- /.dropdown -->
    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
            <i class="fa fa-tasks fa-fw"></i>任务<i class="fa fa-caret-down"></i>
        </a>
        <ul class="dropdown-menu dropdown-tasks">
            <li>
                <a href="#">
                    <div>
                        <p>
                            <strong>任务 1</strong>
                            <span class="pull-right text-muted">40% 已完成</span>
                        </p>
                        <div class="progress progress-striped active">
                            <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
                                <span class="sr-only">40% 已完成 (成功)</span>
                            </div>
                        </div>
                    </div>
                </a>
            </li>
            <li class="divider"></li>
            <li>
                <a class="text-center" href="#">
                    <strong>查看所有任务</strong>
                    <i class="fa fa-angle-right"></i>
                </a>
            </li>
        </ul>
        <!-- /.dropdown-tasks -->
    </li>
    <!-- /.dropdown -->
    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
            <i class="fa fa-envelope fa-fw"></i>消息<i class="fa fa-caret-down"></i>
        </a>
        <ul class="dropdown-menu dropdown-messages">
            <li>
                <a href="#">
                    <div>
                        <strong>模板消息功能上线</strong>
                        <span class="pull-right text-muted">
                            <em>昨天</em>
                        </span>
                    </div>
                    <div>模板消息功能上线...</div>
                </a>
            </li>
            <li class="divider"></li>
            <li>
                <a class="text-center" href="#">
                    <strong>阅读全部消息</strong>
                    <i class="fa fa-angle-right"></i>
                </a>
            </li>
        </ul>
        <!-- /.dropdown-messages -->
    </li>
    <!-- /.dropdown -->
    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
            <i class="fa fa-user fa-fw"></i>${currentUser.username}<i class="fa fa-caret-down"></i>
        </a>
        <ul class="dropdown-menu dropdown-user">
            <li><a href="#"><i class="fa fa-user fa-fw"></i>个人中心</a></li>
            <li><a href="#"><i class="fa fa-gear fa-fw"></i>帐号设置</a></li>
            <li class="divider"></li>
            <li><a href="${createLink(controller: 'logout')}"><i class="fa fa-sign-out fa-fw"></i>退出</a></li>
        </ul>
        <!-- /.dropdown-user -->
    </li>
    <!-- /.dropdown -->
    </ul>
    <!-- /.navbar-top-links -->

    <div class="navbar-default sidebar" role="navigation">
        <div class="sidebar-nav navbar-collapse">
            <div class="account-box" id="accountBox">
                <span title="微信公众号">
                    <i class="glyphicon glyphicon-qrcode"></i>&nbsp;
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">{{ account.name }} <i class="fa fa-caret-down"></i></a>
                    <ul class="dropdown-menu dropdown-user">
                        <li ng-repeat="a in accounts">
                            <a ng-click="selectAccount(a)" href="javascript:void(0)"><i class="glyphicon glyphicon-user"></i> {{ a.name }}</a>
                        </li>
                    </ul>
                </span>
            </div>
            <ul class="nav nav-menu" id="side-menu">
                <g:each var="menuMap" status="i" in="${menuTree}">
                    <li>
                        <g:if test="${0 == i}">
                            <a href="${menuMap.menu.url}"><i class="${menuMap.menu.cssicon}"></i> <b>${menuMap.menu.name}</b><span class="fa arrow"></span></a>
                        </g:if>
                        <g:else>
                            <a href="${menuMap.menu.url}"><i class="${menuMap.menu.cssicon}"></i> <b>${menuMap.menu.name}</b><span class="fa arrow"></span></a>
                        </g:else>
                        <g:if test="${menuMap.children.size() > 0}">
                            <ul class="nav nav-second-level">
                                <g:each var="menuitem" in="${menuMap.children}">
                                    <li>
                                        <a href="${menuitem.url}">${menuitem.name}</a>
                                    </li>
                                </g:each>
                            </ul>
                        </g:if>
                    </li>
                </g:each>
            </ul>
        </div>
        <!-- /.sidebar-collapse -->
    </div>
    <!-- /.navbar-static-side -->
    </nav>

    <div id="page-wrapper" ui-view>
        %{--<div class="container" ui-view></div>--}%
    </div>
    <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

<r:layoutResources/>
</body>
</html>