/**
 * 描述：处理日期
 *
 * @author xiongyanan
 * @date 2014-8-9
 *
 */


$(function() {
	var beginNow = new Date().Format("yyyy-MM-dd 00");
	var endNow = new Date().Format("yyyy-MM-dd hh");

	$("#begindate").datetimepicker({
		format: "yyyy-mm-dd hh:ii",
		autoclose: true,
		todayHighlight: true,
		todayBtn: true,
		language: "zh-CN",
		minView: 0,
		initialDate: beginNow,
	});
	$("#enddate").datetimepicker({
		format: "yyyy-mm-dd hh:ii",
		autoclose: true,
		todayHighlight: true,
		todayBtn: true,
		language: "zh-CN",
		minView: 0,
		initialDate: endNow,
	});

	$("#selectdate").bind("change", function() {
		quickSelectDate();
	});

});

function quickSelectDate() {
	var selectedValue = $("#selectdate").find("option:selected").val();

	if ("0" == selectedValue) {
		$("#begindate input").val("");
		$("#enddate input").val("");
	} else if ("1" == selectedValue) {
		$("#begindate input").val(now.Format("yyyy-MM-dd 00:00"));
		$("#enddate input").val(now.Format("yyyy-MM-dd hh:mm"));
	} else if ("2" == selectedValue) {
		var lastDay = getYesterdayDate();
		$("#begindate input").val(lastDay.Format("yyyy-MM-dd 00:00"));
		$("#enddate input").val(now.Format("yyyy-MM-dd 00:00"));
	} else if ("3" == selectedValue) {
		var monday = getWeekStartDate();
		$("#begindate input").val(monday.Format("yyyy-MM-dd 00:00"));
		$("#enddate input").val(now.Format("yyyy-MM-dd hh:mm"));
	} else if ("4" == selectedValue) {
		var lastWeekBegin = getLastWeekStartDate();
		var monday = getWeekStartDate();
		$("#begindate input").val(lastWeekBegin.Format("yyyy-MM-dd 00:00"));
		$("#enddate input").val(monday.Format("yyyy-MM-dd 00:00"));
	} else if ("5" == selectedValue) {
		var monthStartDate = getMonthStartDate();
		$("#begindate input").val(monthStartDate.Format("yyyy-MM-dd 00:00"));
		$("#enddate input").val(now.Format("yyyy-MM-dd hh:mm"));
	} else if ("6" == selectedValue) {
		var lastMonthStartDate = getLastMonthStartDate();
		var monthStartDate = getMonthStartDate();
		$("#begindate input").val(lastMonthStartDate.Format("yyyy-MM-dd 00:00"));
		$("#enddate input").val(monthStartDate.Format("yyyy-MM-dd 00:00"));
	}
}

/**
 * 获取本周、本季度、本月、上月的开始日期、结束日期
 */
var now = new Date(); //当前日期   
var nowDayOfWeek = now.getDay(); //今天本周的第几天   
var nowDay = now.getDate(); //当前日   
var nowMonth = now.getMonth(); //当前月   
var nowYear = now.getYear(); //当前年   
nowYear += (nowYear < 2000) ? 1900 : 0;

var lastMonthDate = new Date(); //上月日期
lastMonthDate.setDate(1);
lastMonthDate.setMonth(lastMonthDate.getMonth() - 1);
var lastYear = lastMonthDate.getYear();
var lastMonth = lastMonthDate.getMonth();

//获得昨天日期
function getYesterdayDate() {
    yesterdayDate = new Date(now.getTime() - 1 * 24 * 60 * 60 * 1000);
    return yesterdayDate;
}

//获得本周的开始日期   
function getWeekStartDate() {
    var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek + 1);
    return weekStartDate
}

//获得本周的结束日期   
function getWeekEndDate() {
    var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek));
    return weekEndDate;
}

//获得上周的开始日期
function getLastWeekStartDate() {
    var lastWeekStartDate = new Date(now.getTime() - (now.getDay() == 0 ? (7 + 6) : (now.getDay() + 6) * 24 * 60 * 60 * 1000));
    return lastWeekStartDate;
}

//获得本月的开始日期   
function getMonthStartDate() {
    var monthStartDate = new Date(nowYear, nowMonth, 1);
    return monthStartDate;
}

//获得本月的结束日期   
function getMonthEndDate() {
    var monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth));
    return monthEndDate;
}

//获得上月开始时间
function getLastMonthStartDate() {
    var lastMonthStartDate = new Date(nowYear, lastMonth, 1);
    return lastMonthStartDate;
}

//获得上月结束时间
function getLastMonthEndDate() {
    var lastMonthEndDate = new Date(nowYear, lastMonth, getMonthDays(lastMonth));
    return lastMonthEndDate;
}

//获得本季度的开始日期   
function getQuarterStartDate() {
    var quarterStartDate = new Date(nowYear, getQuarterStartMonth(), 1);
    return quarterStartDate;
}

//获得本季度的结束日期   
function getQuarterEndDate() {
    var quarterEndMonth = getQuarterStartMonth() + 2;
    var quarterStartDate = new Date(nowYear, quarterEndMonth, getMonthDays(quarterEndMonth));
    return quarterStartDate;
}