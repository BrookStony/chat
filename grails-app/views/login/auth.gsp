<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon-chat.ico')}">
    <title>${message(code: 'base.title')}</title>
    <r:require module="bootstrap"/>
    <r:layoutResources/>
    <style type="text/css">
    body {
        padding-bottom: 40px;
        background-color: #f5f5f5;
        background: url(${resource(dir:'images',file:'bj.jpg')}) whiteSmoke;
    }

    .container {
        max-width: none !important;
        width: inherit;
    }

    .form-signin {
        max-width: 500px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
        -moz-border-radius: 5px;
        border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
        -moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
        box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
    }

    .form-signin .form-signin-heading,
    .form-signin .checkbox {
        margin-bottom: 10px;
    }

    .form-signin input[type="text"],
    .form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
    }

    a {
        color: #999999;
    }

    a:hover {
        color: #666666;
    }

    #footer {
        text-align: center;
        color: #999999
    }

    .login-container {
        margin-top: 10%;
        margin-bottom: 10%
    }

    </style>
</head>

<body>

<div class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"><i
                    class="glyphicon glyphicon-cloud-upload"></i> ${message(code: 'base.title')}</a>
        </div>
    </div>
</div>

<div class="login-container">
    <form id="loginForm" class="form-signin transparent" method="post" action="${postUrl}">
        <g:if test="${flash.message}">
            <div class="alert alert-warning">${flash.message}
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
        </g:if>
        <h3 class="form-signin-heading" style="color: #777;font-weight: bold">登录</h3>
        <hr/>
        <input class="form-control input-lg" type="text" name="j_username" value="${username}" placeholder="用户名">
        <input class="form-control input-lg" type="password" name="j_password" placeholder="密码">
        <label class="checkbox" style="font-weight: normal">
            <input type='checkbox' name='${rememberMeParameter}'
                   <g:if test='${hasCookie}'>checked='checked'</g:if>/> 记住我
        </label>
        <br/>
        <button class="btn btn-lg btn-primary" type="submit">登录</button>
    </form>
</div>

<div id="footer">
    <p>&copy; 2013-2014 Trunksoft, Inc. &middot; <a href="#">Privacy</a> &middot; <a href="#">Terms</a> V <g:meta
            name="app.version"/></p>
</div>
<r:layoutResources/>
<script type='text/javascript'>
    <!--
    (function () {
        document.forms['loginForm'].elements['j_username'].focus();
    })();
    // -->
</script>
<!--[if lt IE 8]>
<script type="text/javascript">
  var anti_ie_config = {
    cssPath: 'js/lib/anti-ie/',
    imgPath: 'js/lib/anti-ie/img/'
  };
</script>
 <script type="text/javascript" src="${resource(dir: 'js/lib/anti-ie', file: 'anti-ie.js')}"></script>
 <![endif]-->
</body>
</html>