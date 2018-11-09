<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<head>
    <title>账号绑定</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon-chat.ico')}">
    <link rel="stylesheet css" href="${resource(dir: 'lib/mui/css', file: 'mui.min.css')}">
    <script src="${resource(dir: 'lib/mui/js', file: 'mui.min.js')}"></script>
    <style>
    .area {
        margin: 20px auto 0px auto;
    }
    .mui-input-group:first-child {
        margin-top: 20px;
    }
    .mui-input-group label {
        width: 30%;
    }
    .mui-input-row label~input,
    .mui-input-row label~select,
    .mui-input-row label~textarea {
        width: 70%;
    }
    .mui-checkbox input[type=checkbox],
    .mui-radio input[type=radio] {
        top: 6px;
    }
    .mui-content-padded {
        margin-top: 25px;
    }
    .mui-btn {
        padding: 10px;
    }
    </style>
</head>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">账号绑定</h1>
</header>
<div class="mui-content">
    <form id="bindForm" class="mui-input-group" method="post" action="${createLink(controller: 'bindAccount', action: 'auth')}">
        <input type="hidden" name="accountId" value="${accountId}">
        <input type="hidden" name="openId" value="${openId}">
        <input type="hidden" name="timestamp" value="${timestamp}">
        <input type="hidden" name="signature" value="${signature}">
        <div class="mui-input-row">
            <label>用户名</label>
            <input id='username' name="username" type="text" class="mui-input-clear mui-input" placeholder="请输入用户名">
        </div>
        <div class="mui-input-row">
            <label>密&nbsp;&nbsp;&nbsp;码</label>
            <input id='password' name="password" type="password" class="mui-input-clear mui-input" placeholder="请输入密码">
        </div>
        <div class="mui-input-row">
            <label>手机号</label>
            <input id='phone' name="phone" type="text" class="mui-input-clear mui-input" placeholder="请输入手机号">
        </div>
        <div class="mui-input-row">
            <label>邮箱</label>
            <input id='email' name="email" type="email" class="mui-input-clear mui-input" placeholder="请输入邮箱">
        </div>
        <div class="mui-content-padded">
            <button id='reg' class="mui-btn mui-btn-block mui-btn-primary" type="submit">登录</button>
        </div>
        <br>
    </form>
    <div class="mui-content-padded">
        <p>请在此页面输入用户名和密码进行登录验证，验证成功后账号将与微信公众号绑定，轻松绑定即可通过微信公众号查询订单状态和与客服人员交流。</p>
    </div>
</div>
</body>
</html>