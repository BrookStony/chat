<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="utf-8">
    <title>微信大屏幕</title>
    <script src="${createLinkTo(dir: 'js', file: 'lib/jquery/jquery.min.js')}"></script>
    <script src="${createLinkTo(dir: 'js', file: 'lib/bootstrap/js/bootstrap.min.js')}"></script>
    <link href="${createLinkTo(dir: 'js', file: 'lib/bootstrap/css/bootstrap.min.css')}" rel="stylesheet" type="text/css">
    <link href="${createLinkTo(dir: 'css', file: 'icomoon/style.css')}" rel="stylesheet" type="text/css">
    <link href="${createLinkTo(dir: 'css', file: 'screen/screen.css')}" rel="stylesheet" type="text/css">
</head>

<body>
<div id="screen_wrap"
     style="background: #000080 url('${createLinkTo(dir: 'images', file: 'screen/blue/bg.jpg')}') center 0 no-repeat;">
    <div id="screen">
        <div id="header" class="clearfix">
            <a href="http://wxscreen.com" target="_blank">
                <span class="logo left">
                    <img class="logoScreenLeft" src="http://demo.wxscreen.com/images/common/logo_v6.png"
                         style="position: relative; width: 210px; height: 52px; left: 5px; top: 22px;">
                </span>
            </a>

            <div class="word-scroll left">
                <div class="clearfix">
                    <div class="scrollbox left">
                        <ul class="word-list wordList" style="visibility: visible;">
                            <li class="themeBox" style="display: none;">微信大屏幕官方演示页面<br>点击下方对应功能按钮可查看功能演示</li>

                            <li class="themeBox" style="display: list-item;">微信号：hudongdapingmu<br>关注后发送内容即可上墙</li>


                            <li class="themeBox" style="display: none;">编辑短信内容#微信墙#+您想说的话 <br>到 1069-0133-0101-1350 即可上墙
                            </li>

                        </ul>

                        <ul class="word-list checkinWordList" style="visibility: hidden; margin-top: -96px;">
                            <li class="themeBox" style="display: list-item;">微信大屏幕官方演示页面<br>点击下方对应功能按钮可查看功能演示</li>

                            <li class="themeBox" style="display: none;">关注微信号：hudongdapingmu<br>发送含有“签到”两字的内容即可签到</li>
                        </ul>

                    </div>

                    <div class="num-t left">
                        <p><em class="messageTotal">73</em><span>条信息</span></p>
                    </div>
                </div>
            </div>
            <span class="reserved right showIntroBtn">
                <img src="http://s8.wxscreen.com/static/upload/slave2_store2/2014/01/03/20140103185421000000_1_50479_7439.png"
                     style="max-width:126px;max-height:96px;">
            </span>
        </div>

        <div id="container">
            <div class="inner">
                <div class="user_list">
                    <div class="user_item_box">
                        <div class="user_info left">
                            <div class="user_name">
                                <span>名称</span>
                            </div>
                            <div class="user_head">
                                <a href="javascript:void(0);" class="user_head_img">
                                    <img src="http://wx.qlogo.cn/mmopen/h13HtqxcFlQIlHC2ricSyu8KpwySGY6qd8CT2gfdCQgiaA1ONavTpic71qGriaGibXdBjDiaFibB1DbCr8zysqS73CibJkuy7KRnouSB/0" width="90" height="90" alt="">
                                </a>
                            </div>
                        </div>
                        <div class="user_separation left"></div>
                        <div class="user_word">
                            <p class="text">#上墙#关注后发送内容即可上墙</p>
                        </div>
                    </div>
                </div>
                <div class="user_item_box">
                    <div class="user_info left">
                        <div class="user_name">
                            <span>名称sdsd</span>
                        </div>
                        <div class="user_head">
                            <a href="javascript:void(0);" class="user_head_img">
                                <img src="http://wx.qlogo.cn/mmopen/h13HtqxcFlQIlHC2ricSyu8KpwySGY6qd8CT2gfdCQgiaA1ONavTpic71qGriaGibXdBjDiaFibB1DbCr8zysqS73CibJkuy7KRnouSB/0" width="90" height="90" alt="">
                            </a>
                        </div>
                    </div>
                    <div class="user_separation left"></div>
                    <div class="user_word">
                        <p class="text">#上墙#关注后发送内容即可上墙, 微信号：hudongdapingmu</p>
                    </div>
                </div>
                <div class="user_item_box">
                    <div class="user_info left">
                        <div class="user_name">
                            <span>名称sdsd</span>
                        </div>
                        <div class="user_head">
                            <a href="javascript:void(0);" class="user_head_img">
                                <img src="http://wx.qlogo.cn/mmopen/h13HtqxcFlQIlHC2ricSyu8KpwySGY6qd8CT2gfdCQgiaA1ONavTpic71qGriaGibXdBjDiaFibB1DbCr8zysqS73CibJkuy7KRnouSB/0" width="90" height="90" alt="">
                            </a>
                        </div>
                    </div>
                    <div class="user_separation left"></div>
                    <div class="user_word">
                        <p class="text">#上墙#关注后发送内容即可上墙, 微信号：hudongdapingmu</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="footer" class="clearfix">
        %{--<div class="scroll-word clearfix" style="display:none;">--}%
        %{--<!-- <span class="wtit left">轮播公告：</span> -->--}%
        %{--<div class="words-bottom left">--}%
        %{--<div id="wordScroll" class="word-scroll2">--}%
        %{--<ul class="wb-list clearfix">--}%
        %{--<li>欢迎参加康泰纳仕2014年会！</li>--}%
        %{--<li>请关注VOGUEVIP微信账号（微信号搜索：voguevipclub），进行年会互动。</li>--}%
        %{--<!-- <li>康泰纳什棒棒的3！！</li> -->--}%

        %{--</ul>--}%
        %{--</div>--}%
        %{--</div>--}%
        %{--</div>--}%

        %{--<div class="left-bottom left">--}%
        %{--<span class="logo-b logo-sun"></span>--}%

        %{--</div>--}%

        %{--<div class="prodres-box">--}%
        %{--<canvas id="cirProgress" width="50" height="50"></canvas>--}%
        %{--</div>--}%

        <div class="screen_toolbar right">
            <a id="btnStar" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-bubble3"></span></div>
            </a>
            <a id="checkIn" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-pen2"></span></div>
            </a>
            <a id="skinSelected" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-t-shirt"></span></div>
            </a>
            <a id="btnLottery" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-gift"></span></div>
            </a>
            <a id="btnVote" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-stats-bars"></span></div>
            </a>
            <a id="btnDanmu" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-rocket"></span></div>
            </a>
            <a id="btnFullScreen" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-enlarge"></span></div>
            </a>
            <a id="btnOldest" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-backward"></span></div>
            </a>
            <a id="btnPrev" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-previous"></span></div>
            </a>
            <a id="btnPause" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-pause"></span></div>
            </a>
            <a id="btnNext" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-next"></span></div>
            </a>
            <a id="btnNewest" class="screen_btn" href="javascript:void(0);">
                <div class="chat_glyph chat_fs1"><span class="icon-forward2"></span></div>
            </a>
        </div>
    </div>
</div>
</div>
</body>
</html>