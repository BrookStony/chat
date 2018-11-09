modules = {
    application {
        resource url: 'js/application.js'
    }
    "jquery-latest" {
        resource url: 'js/lib/jquery/jquery.min.js', nominify: true
    }
    underscore {
        resource url: 'js/lib/underscore/underscore.js'
    }
    html5shiv {
        resource url: 'js/lib/html5shiv/html5shiv.js', disposition: 'head'
    }
    angular {
        resource url: 'js/lib/angular/angular.min.js', nominify: true
        resource url: 'js/lib/angular/angular-resource.min.js', nominify: true
        resource url: 'js/lib/angular/i18n/angular-locale_zh-cn.js'
    }
    bootstrap {
        dependsOn('jquery-latest')
        resource url: 'js/lib/bootstrap/css/bootstrap.min.css', nominify: true
        resource url: 'js/lib/bootstrap/js/bootstrap.min.js', nominify: true
    }
    select2 {
        resource url: 'js/lib/select2/select2.css'
        resource url: 'js/lib/select2/select2-bootstrap.css'
        resource url: 'js/lib/select2/select2.min.js', nominify: true
        resource url: 'js/lib/select2/select2_locale_zh-CN.js'
    }
    tablesorter {
        dependsOn('jquery-latest')
        resource url: 'js/lib/tablesorter/theme.bootstrap.css'
        resource url: 'js/lib/tablesorter/jquery.tablesorter.min.js', nominify: true
        resource url: 'js/lib/tablesorter/jquery.tablesorter.widgets.min.js', nominify: true
    }
    moment {
        resource url: 'js/lib/moment/moment.js'
        resource url: 'js/lib/moment/lang/zh-cn.js'
    }
    'bootstrap-daterangepicker' {
        dependsOn('jquery-latest', 'bootstrap', 'moment')
        resource url: 'js/lib/bootstrap-daterangepicker/daterangepicker-bs3.css'
        resource url: 'js/lib/bootstrap-daterangepicker/daterangepicker.js'
    }
    zTree {
        dependsOn('jquery-latest')
        resource url: 'js/lib/zTree/css/zTreeStyle/zTreeStyle.css'
        resource url: 'js/lib/zTree/js/jquery.ztree.core-3.5.min.js', nominify: true
        resource url: 'js/lib/zTree/js/jquery.ztree.excheck-3.5.min.js', nominify: true
    }
    "jquery-metis-menu" {
        dependsOn('jquery-latest')
        resource('js/lib/jquery-metis-menu/metisMenu.min.css')
        resource('js/lib/jquery-metis-menu/metisMenu.min.js')
    }
    "bootstrap-fileinput" {
        dependsOn('jquery-latest','bootstrap')
        resource url: 'js/lib/bootstrap-fileinput-master/js/fileinput.js'
        resource url: 'js/lib/bootstrap-fileinput-master/css/fileinput.css'
    }
    "jquery-fileupload" {
        dependsOn('jquery-latest', 'jquery-ui', 'angular')
        resource('js/lib/jquery-file-upload/css/jquery.fileupload-ui.css')
        resource('js/lib/jquery-file-upload/jquery.iframe-transport.js')
        resource('js/lib/jquery-file-upload/jquery.fileupload.js')
    }
    "jquery-ajaxfileupload" {
        dependsOn('jquery-latest')
        resource('js/lib/jquery-ajaxfileupload/ajaxfileupload.js')
    }
    "jquery-chinamap" {
        dependsOn('jquery-latest', 'raphael')
        resource('js/lib/jquery-chinamap/jquery.chinamap.js')
    }
    "jquery-shorten" {
        dependsOn('jquery-latest')
        resource('js/lib/jquery-shorten/jquery.shorten.js')
    }
    "jquery-nicescroll" {
        dependsOn('jquery-latest')
        resource('js/lib/jquery-nicescroll/jquery.nicescroll.js')
    }
    json2 {
        resource('js/lib/json2/json2.js')
    }
    highcharts {
//        resource url: 'lib/highcharts/highcharts.js', nominify: true
        resource url: 'js/lib/highcharts/highstock.js', nominify: true
        resource url: 'js/lib/highcharts/highcharts-more.js', nominify: true
    }
    highmaps {
        resource url: 'js/lib/highmaps/highmaps.js', nominify: true
        resource url: 'js/lib/highmaps/module/data.js', nominify: true
        resource url: 'js/lib/highmaps/module/exporting.js', nominify: true
        resource url: 'js/lib/highmaps/mapdata/countries/china/cn-all-sar-taiwan.js', nominify: true
    }
    raphael {
        resource url: 'js/lib/raphael/raphael-min.js', nominify: true
    }
//    "font-awesome" {
//        resource url: 'font-awesome-4.1.0/css/font-awesome.min.css', nominify: true
//    }
    "jquery-ui" {
        dependsOn('jquery-latest')
        resource url: 'js/lib/jquery-ui/jquery-ui-1.10.3.custom.min.js', nominify: true
    }
    "angular-ui" {
        dependsOn('angular', 'select2', 'jquery-ui')
        resource url: 'js/lib/angular-ui/ui-utils.js'
        resource url: 'js/lib/angular-ui/ui-utils-ieshiv.js'
        resource url: 'js/lib/angular-ui/select2.js'
        resource url: 'js/lib/angular-ui/sortable.js'
    }
    "angular-ui-bootstrap" {
        dependsOn('angular')
        resource url: 'js/lib/angular-ui-bootstrap/ui-bootstrap-tpls-0.10.0.min.js'
    }
    "angular-ui-router" {
        dependsOn('angular')
        resource url: 'js/lib/angular-ui-router/angular-ui-router.min.js', nominify: true
    }
    "angular-strap" {
        dependsOn('angular')
        resource url: 'js/lib/angular-strap/popover.js'
    }
    "store" {
        resource url: 'js/lib/store/store-json2.min.js', nominify: true
    }
    "umeditor" {
        dependsOn('jquery-latest')
        resource url: 'js/lib/umeditor/themes/default/css/umeditor.min.css'
        resource url: 'js/lib/umeditor/umeditor.config.js', nominify: true
        resource url: 'js/lib/umeditor/umeditor.min.js', nominify: true
        resource url: 'js/lib/umeditor/lang/zh-cn/zh-cn.js', nominify: true
    }
    "chat-util" {
        resource url: 'js/chatutil.js'
    }
    "jquery-masonry" {
        dependsOn("jquery-latest")
        resource url: 'js/lib/jquery-masonry/masonry.pkgd.min.js', nominfy: true
    }
    "angular-masonry" {
        dependsOn("jquery-latest")
        dependsOn("jquery-masonry")
        resource url: 'js/lib/angular-masonry/angular-masonry.js', nominfy: true
    }
    "app-css" {
        resource url: 'css/wechat-menu.css'
        resource url: 'css/material.css'
        resource url: 'css/mass-send.css'
        resource url: 'css/single-send.css'
        resource url: 'css/chatmsg.css'
        resource url: 'css/statistics.css'
        resource url: 'css/app.css'
        resource url: 'css/picture.css'
    }
    "app-js" {
        dependsOn('underscore', 'bootstrap', 'angular-ui-bootstrap', 'angular-ui-router', 'angular-ui', 'angular-strap',
                'zTree', 'tablesorter', 'bootstrap-daterangepicker', 'highcharts', 'highmaps', 'jquery-metis-menu',
                'jquery-nicescroll', 'jquery-ajaxfileupload','bootstrap-fileinput', 'umeditor', /*'jquery-fileupload', 'jquery-chinamap',*/
                'jquery-shorten', 'store', 'chat-util','angular-masonry')
        //directives,filters,services
        resource url: 'app/directives/directives.js'
        resource url: 'app/filters/filters.js'
        resource url: 'app/services/services.js'
        //dashboard
        resource url: 'app/dashboard/route.js'
        resource url: 'app/dashboard/dashboard.js'
        //common
        resource url: 'app/common/common.js'
        resource url: 'app/common/performanceTrend.js'
        resource url: 'app/common/structureTreeFilters.js'

        //function
        resource url: 'app/weChatMenu/route.js'
        resource url: 'app/weChatMenu/weChatMenu.js'

        //member
        resource url: 'app/member/route.js'
        resource url: 'app/member/member.js'

        //message
        resource url: 'app/message/route.js'
        resource url: 'app/message/chatMessage.js'
        resource url: 'app/message/templateMessage.js'
        resource url: 'app/message/messageTemplate.js'

        //alert
        resource url: 'app/alert/route.js'
        resource url: 'app/alert/alert.js'

        //massSend
        resource url: 'app/massSend/route.js'
        resource url: 'app/massSend/massSend.js'

        //singleSend
        resource url: 'app/singleSend/route.js'
        resource url: 'app/singleSend/singleSend.js'

        //material
        resource url: 'app/material/route.js'
        resource url: 'app/material/article.js'
        resource url: 'app/material/audio.js'
        resource url: 'app/material/picture.js'
        resource url: 'app/material/video.js'

        //module
        resource url: 'app/module/autoreply/route.js'
        resource url: 'app/module/autoreply/autoReplyRule.js'

        resource url: 'app/module/bindAccount/route.js'
        resource url: 'app/module/bindAccount/bindAccountSetting.js'

        //statistics
        resource url: 'app/statistics/route.js'
        resource url: 'app/statistics/memberStat.js'

        //tools
//        resource url: 'app/tools/route.js'

        //setting
        resource url: 'app/setting/route.js'
        resource url: 'app/setting/account.js'

        //setting
        resource url: 'app/task/route.js'
        resource url: 'app/task/task.js'

        //system
        resource url: 'app/system/route.js'
        resource url: 'app/system/system.js'
        resource url: 'app/system/personal.js'
        resource url: 'app/system/user.js'
        resource url: 'app/system/role.js'
        resource url: 'app/system/userGroup.js'
        resource url: 'app/system/menu.js'
        resource url: 'app/system/operlog.js'
        resource url: 'app/system/notification.js'

        //app
        resource url: 'app/app.js'
    }

    "screen-js" {
        dependsOn('underscore', 'bootstrap', 'angular-ui-bootstrap', 'angular-ui-router', 'angular-ui', 'angular-strap',
             'store')
        //message
        resource url: 'module/screen/message/message.js'
        resource url: 'module/screen/message/route.js'

        //screen
        resource url: 'module/screen/screen.js'
    }
}