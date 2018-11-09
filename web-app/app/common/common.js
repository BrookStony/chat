/** common store constant **/
var CURRENT_ACCOUNT_KEY = "chat-current-account-v1";
var ACCOUNT_SORT_KEY = "chat-account-sort-v1";
var AUTOREPLYRULE_SORT_KEY = "chat-autoreply-sort-v1";
var MEMBER_SORT_KEY = "chat-member-sort-v1";
var USER_SORT_KEY = "chat-user-sort-v1";
var USERGROUP_SORT_KEY = "chat-usergroup-sort-v1";
var ROLE_SORT_KEY = "chat-role-sort-v1";
var MENU_SORT_KEY = "chat-menu-sort-v1";
var TASK_SORT_KEY = "chat-task-sort-v1";
var NOTIFICATION_SORT_KEY = "chat-notification-sort-v1";
var CHAT_MESSAGE_KEY = "chat-message-sort-v1";
var MASS_MESSAGE_KEY = "mass-message-sort-v1";
var TEMPLATE_MESSAGE_KEY = "template-message-sort-v1";
var MESSAGE_TEMPLATE_KEY = "message-template-sort-v1";
var BIND_ACCOUNT_SETTING_KEY = "bind-account-setting-sort-v1";
var CHAT_ARITICLE_KEY = "chat-article-sort-v1";
var TAG_SORT_KEY = "chat-tag-sort-v1";
var OPERLOG_SORT_KEY = "chat-operlog-sort-v1";

/** common module **/
var module = angular.module("chat.common", []);

/** dateRangePickerMixin **/
(function (module) {
    module.value('uiJqConfig', {
        shorten: {
            showChars: 15,
            ellipsesText: "...",
            moreText: "更多",
            lessText: "收起",
            errMsg: null
        }
    });
    module.constant("paginationConfig", {
        "maxSize": 10,
        "previousText": "上一页",
        "nextText": "下一页",
        "firstText": "首页",
        "lastText": "末页",
        "directionLinks": true,
        "boundaryLinks": true,
        "rotate": true
    });
    module.value('searchMixin', function ($scope) {
        $scope.selectedSearchField = function (f) {
            $scope.searchField = f.name;
            $scope.searchLabel = f.label;
        };
    });
    module.value('getFiltersValue', function (filters) {
        return JSON.stringify(_.chain(filters).map(function (v) {
            if (v.name == 'status') {
                return [ v.name, v.value];
            } else {
                return [ v.name, _.chain(v.values).filter(function (f) {
                    return f.val != null;
                }).map(function (cv) {
                    if (v.name == 'ctr') {
                        return {name: cv.name, val: cv.val / 100 }
                    } else {
                        return {name: cv.name, val: cv.val}
                    }
                }).value()];
            }
        }).object().value());
    });
    module.value('filtersMixin', function ($scope, status) {
        var numValues = [
            {name: 'eq', label: "=", val: null},
            {name: 'ne', label: "!=", val: null},
            {name: 'gt', label: ">", val: null},
            {name: 'ge', label: ">=", val: null},
            {name: 'lt', label: "<", val: null},
            {name: 'le', label: "<=", val: null}
        ];
        $scope.filters = [
            {name: "status", label: "状态", value: status[0], type: 'enum', values: status},
            {name: "click", label: "点击", type: 'number', values: angular.copy(numValues)},
            {name: "cost", label: "消费", type: 'number', values: angular.copy(numValues)},
            {name: "impression", label: "展现", type: 'number', values: angular.copy(numValues)},
            {name: "cpc", label: "平均点击价格", type: 'number', values: angular.copy(numValues)},
            {name: "ctr", label: "点击率", type: 'number', values: angular.copy(numValues)},
            {name: "conversion", label: "转化", type: 'number', values: angular.copy(numValues)}
        ];
        $scope.filtersCount = function () {
            var count = 0;
            _.each($scope.filters, function (v) {
                if (v.name == "status" && v.value.id != 0) {
                    count++
                } else {
                    count = count + _.filter(v.values,function (vx) {
                        return vx.val != null
                    }).length
                }
            });
            return count;
        };
        $scope.filterCount = function (i) {
            var v = $scope.filters[i];
            if (v.name == "status") {
                return v.value.id != 0 ? 1 : 0;
            } else {
                return _.filter(v.values,function (vx) {
                    return vx.val != null
                }).length;
            }
        };
        $scope.applyFilter = function () {
            $scope.refresh();
            $(document).trigger("click");
        };
        $scope.cleanFilter = function (v) {
            if (v.name == "status") {
                v.value = status[0];
                v.values = status;
            } else {
                v.values = angular.copy(numValues)
            }
        };
        $scope.clickFilter = function () {
            $(".popover").removeClass('in');
        };
        $scope.cleanAllFilter = function () {
            _.each($scope.filters, function (v) {
                if (v.name == "status") {
                    v.value = status[0];
                    v.values = status;
                } else {
                    v.values = angular.copy(numValues)
                }
            });
            $scope.refresh();
            $(document).trigger("click");
        }
    });

    module.value('dateRangePickerMixin', function ($scope) {
        var endDate = moment().subtract('days', 1);
        $scope.startDate = moment().subtract('days', 30);
        $scope.endDate = endDate;
        $scope.dateRangePickerOptions = {
            ranges: {
                '昨天': [endDate, endDate],
                '最近7天': [moment().subtract('days', 7), endDate],
                '最近30天': [moment().subtract('days', 30), endDate],
                '本月': [moment().startOf('month'), moment().endOf('month')],
                '上个月': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
            },
            opens: 'right',
            startDate: $scope.startDate,
            endDate: $scope.endDate,
            dateLimit: false
        };

        $scope.dateRangePickerCallback = function (start, end) {
            $('#dateRangePicker').find('span').html(start.format('YYYY-MM-DD') + ' 到 ' + end.format('YYYY-MM-DD'));
            $scope.$apply(function () {
                $scope.startDate = start;
                $scope.endDate = end;
            });
        };
        $('#dateRangePicker').find('span').html($scope.startDate.format('YYYY-MM-DD') + ' 到 ' + $scope.endDate.format('YYYY-MM-DD'));
    });
    module.value('dateRangeSelectMixin', function ($scope) {
        var endDate = moment();
        var yesterday = moment().subtract('days', 1);
        $scope.startDate = yesterday;
        $scope.endDate = endDate;
        $scope.dateRangeSelectOptions = {
            ranges: {
                '今天': [endDate, endDate],
                '昨天': [yesterday, yesterday],
                '最近3天': [moment().subtract('days', 3), endDate],
                '最近7天': [moment().subtract('days', 7), endDate],
                '最近30天': [moment().subtract('days', 30), endDate],
                '本月': [moment().startOf('month'), moment().endOf('month')]
            },
            opens: 'right',
            startDate: $scope.startDate,
            endDate: $scope.endDate,
            dateLimit: false
        };

        $scope.dateRangeSelectCallback = function (start, end) {
            $('#dateRangeSelect').find('span').html(start.format('YYYY-MM-DD') + ' 到 ' + end.format('YYYY-MM-DD'));
            $scope.$apply(function () {
                $scope.startDate = start;
                $scope.endDate = end;
            });
        };
        $('#dateRangeSelect').find('span').html($scope.startDate.format('YYYY-MM-DD') + ' 到 ' + $scope.endDate.format('YYYY-MM-DD'));
    });
})(module);

/** RegionTarget **/
(function (module) {
    var RegionTarget = function (regionTargets) {
        this._init(regionTargets);
    };
    RegionTarget.prototype._init = function (regionTargets) {
        regionTargets = regionTargets || [];
        this.data = [
            {direction: "华北地区", regions: [
                {id: 1000, label: "北京"},
                {id: 3000, label: "天津"},
                {id: 13000, label: "河北"},
                {id: 22000, label: "内蒙古"},
                {id: 26000, label: "山西"}
            ]},
            {direction: "东北地区", regions: [
                {id: 15000, label: "黑龙江"},
                {id: 18000, label: "吉林"},
                {id: 21000, label: "辽宁"}
            ]},
            {direction: "华东地区", regions: [
                {id: 2000, label: "上海"},
                {id: 5000, label: "福建"},
                {id: 9000, label: "安徽"},
                {id: 19000, label: "江苏"},
                {id: 20000, label: "江西"},
                {id: 25000, label: "山东"},
                {id: 32000, label: "浙江"}
            ]},
            {direction: "华中地区", regions: [
                {id: 14000, label: "河南"},
                {id: 16000, label: "湖北"},
                {id: 17000, label: "湖南"}
            ]},
            {direction: "华南地区", regions: [
                {id: 4000, label: "广东"},
                {id: 8000, label: "海南"},
                {id: 12000, label: "广西"}
            ]},
            {direction: "西南地区", regions: [
                {id: 10000, label: "贵州"},
                {id: 28000, label: "四川"},
                {id: 29000, label: "西藏"},
                {id: 31000, label: "云南"},
                {id: 33000, label: "重庆"}
            ]},
            {direction: "西北地区", regions: [
                {id: 11000, label: "甘肃"},
                {id: 23000, label: "宁夏"},
                {id: 24000, label: "青海"},
                {id: 27000, label: "陕西"},
                {id: 30000, label: "新疆"}
            ]}
        ];
        _.each(this.data, function (v) {
            _.each(v.regions, function (r) {
                r.checked = _.contains(regionTargets, r.id);
            });
        });
    };
    RegionTarget.prototype.getData = function () {
        return this.data;
    };
    RegionTarget.prototype.toggle = function (direction) {
        var regions = _.find(this.data,function (v) {
            return v.direction == direction;
        }).regions;
        if (_.any(regions, function (r) {
            return r.checked == false;
        })) {
            _.each(regions, function (r) {
                r.checked = true;
            });
        } else {
            _.each(regions, function (r) {
                r.checked = false;
            });
        }
    };
    RegionTarget.prototype.isDirectionChecked = function (direction) {
        var regions = _.find(this.data,function (v) {
            return v.direction == direction;
        }).regions;
        return _.all(regions, function (r) {
            return r.checked;
        });
    };
    RegionTarget.prototype.getSelectedRegionIds = function () {
        return _.map(this.getSelectedRegions(), function (r) {
            return r.id;
        });
    };
    RegionTarget.prototype.getSelectedRegions = function () {
        var selectedRegions = [];
        _.each(this.data, function (v) {
            _.each(v.regions, function (r) {
                if (r.checked) {
                    selectedRegions.push(r);
                }
            });
        });
        return selectedRegions;
    };
    RegionTarget.prototype.isNoSelected = function () {
        return _.all(this.data, function (v) {
            return _.all(v.regions, function (r) {
                return r.checked == false;
            })
        });
    };
    RegionTarget.prototype.clearAll = function () {
        _.each(this.data, function (v) {
            _.each(v.regions, function (r) {
                r.checked = false;
            });
        });
    };
    module.value('initRegionTarget', function ($scope, src) {
        $scope.areaRadios = {
            isAllArea: "true"
        };
        var regionTarget = new RegionTarget(src);
        $scope.regionDatas = regionTarget.getData();
        $scope.areaRadios.isAllArea = regionTarget.isNoSelected() ? "true" : "false";
        $scope.toggle = function (direction) {
            regionTarget.toggle(direction);
        };
        $scope.isDirectionChecked = function (direction) {
            return regionTarget.isDirectionChecked(direction);
        };
        return regionTarget;
    });
})(module);

/** Schedule **/
(function (module) {
    var Schedule = function () {
        this.original = [];
        this._init();
    };

    Schedule.prototype.getData = function () {
        return this.data;
    };

    Schedule.prototype.getOriginal = function () {
        return this.original;
    };

    Schedule.prototype.set = function (row, col, value) {
        this.data[row][col] = value;
    };

    Schedule.prototype.get = function (row, col) {
        return this.data[row][col];
    };

    Schedule.prototype.toggleCell = function (row, col) {
        this.set(row, col, !this.get(row, col));
    };

    Schedule.prototype.toggleRow = function (row) {
        var toggleValue = true;
        if (_.any(this.data[row], function (v) {
            return v == true;
        })) {
            toggleValue = false;
        }
        for (var j = 0; j < 24; j++) {
            this.set(row, j, toggleValue);
        }
    };

    Schedule.prototype.isSelectRow = function (row) {
        return _.all(this.data[row], function (v) {
            return v == false;
        });
    };

    Schedule.prototype.isSelectCol = function (col) {
        var that = this;
        return _.all(_.range(0, 7), function (row) {
            return that.get(row, col) == false;
        })
    };

    Schedule.prototype.toggleCol = function (col) {
        var toggleValue = true;
        var that = this;
        if (_.any(_.range(0, 7), function (row) {
            return that.get(row, col) == true;
        })) {
            toggleValue = false;
        }
        for (var i = 0; i < 7; i++) {
            this.set(i, col, toggleValue);
        }
    };

    Schedule.prototype.all = function (value) {
        for (var i = 0; i < 7; i++) {
            for (var j = 0; j < 24; j++) {
                this.data[i][j] = value;
            }
        }
    };

    Schedule.prototype.workday = function () {
        this.all(true);
        for (var i = 0; i < 5; i++) {
            for (var j = 0; j < 24; j++) {
                this.data[i][j] = false;
            }
        }
    };

    Schedule.prototype.weekend = function () {
        this.all(true);
        for (var i = 5; i < 7; i++) {
            for (var j = 0; j < 24; j++) {
                this.data[i][j] = false;
            }
        }
    };

    Schedule.prototype.save = function () {
        this.original = angular.copy(this.data);
    };

    Schedule.prototype.reset = function () {
        this.data = angular.copy(this.original);
    };

    Schedule.prototype.addSchedule = function (weekDay, startHour, endHour) {
        for (var i = startHour; i < endHour; i++) {
            this.set(weekDay, i, true);
        }
    };

    Schedule.prototype.getSchedules = function () {
        var result = [];
        if (this.isAllTrue() == true) {
            return result;
        }
        var now = [];
        for (var i = 0; i < 7; i++) {
            for (var j = 0; j < 24; j++) {
                var cellValue = this.get(i, j);
                if (cellValue && j < 23) {
                    now.push(j);
                } else {
                    if (cellValue && j == 23) {
                        now.push(j);
                    }
                    if (now.length > 0) {
                        result.push({weekDay: i, startHour: now[0], endHour: now[now.length - 1] + 1});
                    }
                    now = [];
                }
            }
        }
        return result;
    };

    Schedule.prototype.isAllTrue = function () {
        var result = true;
        for (var i = 0; i < 7; i++) {
            for (var j = 0; j < 24; j++) {
                if (!this.get(i, j)) {
                    result = false;
                    break
                }
            }
        }
        return result;
    };

    Schedule.prototype._init = function () {
        this.data = [];
        for (var i = 0; i < 7; i++) {
            var array = [];
            for (var j = 0; j < 24; j++) {
                array[j] = false;
            }
            this.data[i] = array
        }
    };
    module.value('Schedule', Schedule);
})(module);