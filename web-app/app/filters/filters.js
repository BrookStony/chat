'use strict';

var AUTOREPLYRULE_STATUS_ENUMS = {
    ACTIVATE: '使用', STOP: '停止'
};

var AUTOREPLYRULE_TYPE_ENUMS = {
    KEYWORD: "关键词自动回复", MESSAGE: "消息自动回复", SUBSCRIBE: "被添加自动回复"
};

var SEVERITY_TYPE = {
    CLEARED: "无", WARNING: "低", MAJOR: "中", CRITICAL: "高"
};

var MENU_LEVEL = {
    1: "一级菜单", 2: "二级菜单", 3: "三级菜单", 4: "四级菜单"
};

var MESSAGE_TYPE = {
    TEXT: "文本", IMAGE: "图片", VOICE: "语言", VIDEO: "视频", MUSIC: "音乐", NEWS: "图文", TEMPALTE: "模板消息"
};

var MASS_MESSAGE_STAUTS = {CREATE: "创建完成", SENDING: "发送中", SENDED: "发送完毕", SEND_FAIL: "发送失败", SEND_SUCCESS: "发送成功", SEND_FAILURE: "发送失败", SEND_ERR: "发送错误"};

var CHAT_MESSAGE_STATUS = {
    0: "创建",
    1: "发生中",
    2: "已发送",
    3: "发送成功",
    4: "发送失败",
    5: "接收中",
    6: "接收成功",
    7: "接收失败"
};

var SEX_TYPE = {
    0: '未知', 1: '男', 2: '女'
};

var SEX_ENUMS = {
    UNKNOWN: '未知', MALE: '男', FEMALE: '女'
};

var ACCOUNT_TYPE = {
    0: '订阅号', 1: '服务号'
};

var ACCOUNT_ENUMS = {
    SUBSCRIBE: '订阅号', SERVICE: '服务号'
};

var STATUS_MAP = {0: '创建', 1: '发送', 2: '发送完', 3: '发送成功',
    4: '发送失败', 5: '接收', 6: '接收成功', 7: '接收失败'};

/* Filters */
(function (angular) {
    var module = angular.module('chat.filters', []);
    module.filter('nullData', function () {
        return function (data) {
            if (data == null || data == undefined) {
                return '-';
            } else {
                return data;
            }
        }
    });
    module.filter('autoReplyRuleTypeEnum', function () {
        return function (type) {
            return AUTOREPLYRULE_TYPE_ENUMS[type];
        }
    });
    module.filter('autoReplyRuleStatusEnum', function () {
        return function (status) {
            return AUTOREPLYRULE_STATUS_ENUMS[status];
        }
    });
    module.filter('sex', function () {
        return function (sex) {
            return SEX_TYPE[sex];
        }
    });
    module.filter('sexEnum', function () {
        return function (sex) {
            return SEX_ENUMS[sex];
        }
    });
    module.filter('accountType', function () {
        return function (type) {
            return ACCOUNT_TYPE[type];
        }
    });
    module.filter('accountEnum', function () {
        return function (type) {
            return ACCOUNT_ENUMS[type];
        }
    });
    module.filter('messageTypeEnum', function () {
        return function (type) {
            return MESSAGE_TYPE[type];
        }
    });
    module.filter('severity', function () {
        return function (severity) {
            return SEVERITY_TYPE[severity];
        }
    });
    module.filter('chatMessageStatus', function () {
        return function (status) {
            return CHAT_MESSAGE_STATUS[status];
        }
    });
    module.filter('massMessageStatus', function () {
        return function (status) {
            return MASS_MESSAGE_STAUTS[status];
        }
    });
    module.filter('menuLevel', function () {
        return function (level) {
            return MENU_LEVEL[level];
        }
    });
    module.filter('menuEnable', function () {
        return function (enable) {
            if(enable) {
                return "使用";
            }
            else {
                return "暂停";
            }
        }
    });
    module.filter('percent', function () {
        return function (percent) {
            if (percent != null && percent != undefined) {
                return '' + (percent * 100).toFixed(2) + "%"
            }
            return null;
        }
    });
    module.filter('fixed', function () {
        return function (data) {
            if (data != null) {
                return data.toFixed(2);
            }
            return data;
        }
    });
    module.filter('qualityToRating', function () {
        return function (quality) {
            if (quality <= 2) {
                return 1;
            } else if (quality == 3 || quality == 4) {
                return 2;
            }
            return 3;
        }
    });
    module.filter('shortName', function(){
        return function(pictureName){
            if(pictureName.length>9){
                return pictureName.substring(0,9)+"..."
            }
            return pictureName
        }
    });
    module.filter('messageStatus', function(){
        return function(status){
            if(status == 3){
                return ""
            }
            if(status == 6){
                return ""
            }
            return STATUS_MAP[status]
        }
    });
})(angular);