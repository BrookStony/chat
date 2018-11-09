'use strict';

function getBaiduStringLength(str) {
    var newStr = str.replace(new RegExp('[/^{}]', "gm"), "");
    var len = newStr.length;
    var reLen = 0;
    for (var i = 0; i < len; i++) {
        if (newStr.charCodeAt(i) < 27 || newStr.charCodeAt(i) > 126) {
            reLen += 2;
        } else {
            reLen++;
        }
    }
    return reLen;
}

/* Directives */
(function (angular) {
    var module = angular.module('chat.directives', []);
    module.directive('contenteditable', function () {
        return {
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                // view -> model
                elm.bind('blur', function () {
                    scope.$apply(function () {
                        ctrl.$setViewValue(elm.text());
                    });
                });
                elm.bind('keydown', function (event) {
                    if (event.keyCode == 13) {
                        elm.blur();
                    }
                });
                // model -> view
                ctrl.$render = function () {
                    elm.text(ctrl.$viewValue);
                };
            }
        };
    });
    module.directive('tablesorter', function () {
        return {
            restrict: "C",
            link: function (scope, elm, attrs, ctrl) {
                elm.tablesorter({
                    theme: "bootstrap",
                    widgets: ["stickyHeaders"],
                    widgetOptions: {
                        stickyHeaders: 'tablesorter-stickyHeader'
                    }
                });
            }
        };
    });
    module.directive('ztree', function () {
        return {
            restrict: "C",
            link: function (scope, elm, attrs, ctrl) {
                var async = scope.$eval(attrs.async);
                if(async) {
                    var setting = scope.$eval(attrs.setting);
                    var tree = jQuery.fn.zTree.init(elm, setting);
                    scope.$on('reAsyncChildNodes', function (event, parentNode, reloadType, isSilent) {
                        tree.reAsyncChildNodes(parentNode, reloadType, isSilent);
                    });
                }
            }
        };
    });
    module.directive('progressTracker', function () {
        return {
            restrict: "C",
            template: '<ol class="aui-progress-tracker">' +
                    '<li ng-repeat="step in model.data" class="aui-progress-tracker-step" ng-class="{\'aui-progress-tracker-step-current\':model.currentIndex==$index}">' +
                    '<span><a href="javascript:void(0)">{{step}}</a></span>' +
                    '</li>' +
                    '</ol>',
            replace: true,
            scope: {model: '=ngModel'},
            link: function (scope, elm, attrs, ctrl) {
            }
        };
    });
    module.directive('trend', function () {
        return {
            restrict: "C",
            template: '<i ng-class="{\'icon-arrow-up\':model==\'RISE\',\'icon-arrow-down\':model==\'FALL\'}"></i> ',
            replace: true,
            scope: {model: '=ngModel'},
            link: function (scope, elm, attrs, ctrl) {
            }
        };
    });
    module.directive('sorting', function () {
        function clearCls(elm) {
            elm.parent().find('th').removeClass('asc');
            elm.parent().find('th').removeClass('desc');
        }

        return {
            restrict: "A",
            link: function (scope, elm, attrs, ctrl) {
                var column = attrs.column;
                var sorting = scope.$eval(attrs.sorting);
                if (!sorting)return;
                elm.addClass('sorting');
                if (sorting.sort == column) {
                    if (sorting.order == 'asc') {
                        elm.addClass('asc');
                    } else {
                        elm.addClass('desc');
                    }
                }
                elm.click(function () {
                    scope.$apply(function () {
                        if (sorting.sort == column) {
                            if (sorting.order == 'asc') {
                                clearCls(elm);
                                elm.removeClass('asc');
                                elm.addClass('desc');
                                sorting.order = 'desc';
                            } else {
                                clearCls(elm);
                                elm.removeClass('desc');
                                elm.addClass('asc');
                                sorting.order = 'asc';
                            }
                        } else {
                            clearCls(elm);
                            sorting.sort = column;
                            sorting.order = 'desc';
                            elm.addClass('desc');
                        }
                    });
                });
            }
        };
    });
    module.directive('dateRangePicker', function () {
        return {
            restrict: "C",
            link: function (scope, elm, attrs, ctrl) {
                var options = scope.$eval(attrs.options);
                var callback = scope.$eval(attrs.callback);
                options = options || {};
                options.format = 'YYYY-MM-DD';
                options.separator = ' 到 ';
                options.showWeekNumbers = true;
                options.locale = {
                    applyLabel: '确定',
                    cancelLabel: '取消',
                    clearLabel: "清除",
                    fromLabel: '从',
                    toLabel: '到',
                    weekLabel: '周',
                    customRangeLabel: '自定义时间范围',
                    daysOfWeek: moment()._lang._weekdaysMin,
                    monthNames: moment()._lang._monthsShort,
                    firstDay: 0
                };
                elm.daterangepicker(options, callback);
            }
        };
    });
    module.directive('highcharts', function () {
        return {
            restrict: "C",
            link: function (scope, elm, attrs, ctrl) {
                scope.$watch(attrs.ngModel, function () {
                    var ngModel = scope.$eval(attrs.ngModel);
                    ngModel = ngModel || {};
                    var highcharts = elm.highcharts();
                    if (highcharts) {
                        highcharts.destroy();
                    }
                    elm.highcharts(ngModel);
                    $("tspan:contains('Highcharts.com')", elm).text('');
                });
            }
        }
    });
    module.directive('highstock', function () {
        return {
            restrict: "C",
            link: function (scope, elm, attrs, ctrl) {
                scope.$watch(attrs.ngModel, function () {
                    var ngModel = scope.$eval(attrs.ngModel);
                    ngModel = ngModel || {};
                    var highcharts = elm.highcharts();
                    if (highcharts) {
                        highcharts.destroy();
                    }
                    elm.highcharts("StockChart", ngModel);
                    $("tspan:contains('Highcharts.com')", elm).text('');
                });
            }
        }
    });
    module.directive('highmaps', function () {
        return {
            restrict: "C",
            link: function (scope, elm, attrs, ctrl) {
                scope.$watch(attrs.ngModel, function () {
                    var ngModel = scope.$eval(attrs.ngModel);
                    ngModel = ngModel || {};
                    var highmaps = elm.highcharts();
                    if (highmaps) {
                        highmaps.destroy();
                    }
                    elm.highcharts('Map', ngModel);
                    $("tspan:contains('Highcharts.com')", elm).text('');
                });
            }
        }
    });

    module.directive('trendRateHelp', ['$filter', function ($filter) {
        return {
            priority: 0,
            restrict: "C",
            link: function (scope, elm, attrs, ctrl) {
                scope.$watch(attrs.itemvalue, function () {
                    var itemLabel = attrs.itemlabel,
                            itemValue = scope.$eval(attrs.itemvalue),
                            chainValue = scope.$eval(attrs.chainvalue),
                            title = itemLabel + " " + (itemLabel == "点击率" ? (itemValue * 100).toFixed(2) + "%" : $filter('number')(itemValue)) + "，环比" + (chainValue >= 0 ? "增长" : "下降") + Math.abs(chainValue) + "%";
                    elm.attr("title", title);
                    elm.addClass("pull-right glyphicon glyphicon-question-sign");
                    elm.css({cursor: "help"});
                });
            }
        }
    }]);

    module.directive('charLimit', function () {
        return {
            restrict: 'A',
            link: function ($scope, $element, $attributes) {
                var limit = $attributes.charLimit;

                $element.bind('keyup', function (event) {
                    var element = $element.parent().parent();
                    var length = getBaiduStringLength($element.val());
                    element.toggleClass('warning', limit - length <= 10);
                    element.toggleClass('error', length > limit);
                });
                $element.bind('keypress', function (event) {
                    // Once the limit has been met or exceeded, prevent all keypresses from working
                    if (getBaiduStringLength($element.val()) >= limit) {
                        // Except backspace
                        if (event.keyCode != 8) {
                            event.preventDefault();
                        }
                    }
                });
            }
        };
    });
    module.directive('baiduCharLimit', function () {
        return {
            restrict: 'C',
            link: function ($scope, $element, $attributes) {
                var max = $attributes.max;
                if (!$element.prev('label.control-label').size()) {
                    $element.before('<label class="control-label"></label>')
                }

                $element.bind('keyup', function (event) {
                    var parent = $element.parent();
                    var label = $element.prev('label.control-label');
                    var length = getBaiduStringLength($element.val());
                    if (length > max) {
                        label.text("最多可以输入" + max + "个字符");
                    } else {
                        label.text("还可以输入" + (max - length) + "字符");
                    }
                    parent.toggleClass('has-warning', max - length <= 10);
                    parent.toggleClass('has-error', length > max);
                    parent.toggleClass('has-success', max - length > 10);
                });
                $element.bind('keypress', function (event) {
                    // Once the limit has been met or exceeded, prevent all keypresses from working
                    if (getBaiduStringLength($element.val()) >= max) {
                        // Except backspace
                        if (event.keyCode != 8) {
                            event.preventDefault();
                        }
                    }
                });
            }
        };
    });
    module.directive('uiBlur', function () {
        return function (scope, elem, attrs) {
            elem.on('blur', function (event) {
                scope.$apply(attrs.uiBlur);
            });
            elem.on('keydown', function (event) {
                if (event.keyCode == 13) {
                    elem.blur();
                }
            });
        };
    });
    module.directive('uiAffix', function () {
        return function (scope, elem, attrs) {
            elem.affix({offset: {top: attrs.offsetTop || 100}});
            var $affix = elem, $parent = $affix.parent(),
                    resize = function () {
                        $affix.width($parent.width());
                    };
            $(window).resize(resize);
            resize();
        }
    });
    module.directive("umEditor", function(){
        return {
            restrict: "AE",
            require: '?ngModel',
            link: function(scope, element, attr, ngModel){
                var option = {
                    toolbar:[ 'bold italic underline | insertorderedlist insertunorderedlist |  image | removeformat forecolor backcolor | ',
                        ' cleardoc']
                };
                var id = attr.id;
                UM.delEditor(id);
                var um = UM .getEditor(id,option);
                //Model数据更新时，更新百度umeditor
                if(ngModel){
                    ngModel.$render = function(){
                        um.setContent(ngModel.$viewValue)
                    };
                }
                //百度umeditor数据更新时，更新Model
                um.addListener('contentChange',function(){
                    setTimeout(function () {
                        scope.$apply(function () {
                            ngModel.$setViewValue(um.getContent());
                        })
                    }, 0);
                });
            }
        }
    });
    module.directive("whenScrolled",function(){
        return function(scope, elm, attr){
            var raw = elm[0];
            elm.bind('scroll',function(){
                if(raw.scrollTop + raw.offsetHeight >= raw.scrollHeight){
                    scope.$apply(attr.whenScrolled);
                }
            });
        }
    });
})(angular);
