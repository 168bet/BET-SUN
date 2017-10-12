/**
 * 描述：处理日志
 *
 * @author xiongyanan
 * @date 2014-8-11
 *
 */

var logCount = 0;

var numberPerPage = 10;
var currentPage = 1;

$(function() {
	if (window.parent.$.cookie("role") == 0) {
		$.ajax({
			headers: {
				"uuid": window.parent.$.cookie("uuid"),
				"role": window.parent.$.cookie("role"),
			},
			type: "get",
			url: "/game/js/superAdmin/addDeleteButton.js",
			async: false,
			cache: false,
			dataType: "script",
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
			}
		});
	}

	initValidate();
	addLogHead();

	$("#selectlines").change(function() {
		numberPerPage = $("#selectlines").find("option:selected").text();
		numberPerPage = parseInt(numberPerPage);

		if (logCount == numberPerPage) {
			currentPage = 1;
		} else if (logCount < currentPage * numberPerPage) {
			currentPage = (logCount - logCount % numberPerPage) / numberPerPage + 1;
		}

		queryLog();
	});
});


function queryLog() {
	var userName = $("#userName").val();
	var beginTime = Date.parse($("#begindate input").val());
	var endTime = Date.parse($("#enddate input").val());
	if (isNaN(beginTime))
		beginTime = "";
	if (isNaN(endTime))
		endTime = "";

	showLogByQueryCondition(userName, beginTime, endTime);
}

function showLogByQueryCondition(userName, beginTime, endTime) {
	//ajax 获取数据
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "get",
		dataType: "json",
		url: "/game/userlog/all?num=" + numberPerPage + "&page=" + currentPage + "&username=" + userName + "&start=" + beginTime + "&end=" + endTime,
		success: function(data) {
			logCount = data.count;
			addLogToDataTable(logCount, data.list);
			window.parent.frameLoad();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}


function addLogHead() {
	$("#loghead").find("tr>th").remove();

	var newth = $('<tr>');
	newth.append("<th><span class='language' data-word='username'>用户名</th>")
		.append("<th><span class='language' data-word='authority'>权限</span></th>")
		.append("<th><span class='language' data-word='date'>时间</span></th>")
		.append("<th><span class='language' data-word='IP'>IP</span></th>")
		.append("<th><span class='language' data-word='operateContent'>操作内容</span></th>");
	$("#loghead").find("tr").replaceWith(newth);

	if (window.parent.$.cookie("role") == 0) {
		addDeleteHead("loghead");
	}
}

function addLogToDataTable(count, list) {
	$("#logbody").find("tr>td").remove();

	if (null == list || 0 == list.length || 0 == count) {
		return;
	}

	for (var index = 0; index < list.length; index++) {
		var rowID = index + 1;
		var date = new Date();
		date.setTime(list[index].time);
		date = date.Format("yyyy-MM-dd hh:mm:ss");
		var userTypes = ["超级管理员", "普通管理员", "客服管理员", "代理", "子帐号", "会员"];

		$("#row_" + rowID).remove(); //移除表格之前的行

		var newRow = $("<tr id=row_" + rowID + "></tr>");
		newRow.addClass((index & 1) ? "success odd" : "warning even");
		newRow.append("<td>" + list[index].username + "</td>")
			.append("<td>" + userTypes[list[index].role] + "</td>")
			.append("<td>" + date + "</td>")
			.append("<td>" + list[index].ip + "</td>")
			.append("<td>" + list[index].content + "</td>");
		$("#logbody").find("tr:last").after(newRow);

		if (window.parent.$.cookie("role") == 0) {
			addDeleteBody(rowID, list[index].id, currentPage, numberPerPage, "logbody");
		}
	}

	allPages = (count - count % numberPerPage) / numberPerPage + (count % numberPerPage == 0 ? 0 : 1);
	showPages(list.length, numberPerPage, currentPage, allPages);
}

function initValidate() {
	$("#searchForm").validate({
		submitHandler: function(form) {
			queryLog();
		},
		rules: {
			userName: {
				isAccount: true
			},
			txtEndDate: {
				isEndDate: "#txtBeginDate"
			},
		},
		errorPlacement: function(error, element) {
			if (element.attr("name") == "txtEndDate")
				element.parent().append(error);
			else
				error.appendTo(element.parent());
		},
	});
}

function deleteInfo(rowID, selectedPage, linesPerPage, uuid) {
	window.parent.showDeleteConfirmDialog(function() {
		uuid = parseInt(uuid);
		$.ajax({
			headers: {
				"uuid": window.parent.$.cookie("uuid"),
				"role": window.parent.$.cookie("role"),
			},
			async: true,
			type: "delete",
			url: "/game/userlog/" + uuid,
			success: function(data) {
				alert(window.parent.errorLangConfig["1005"]);
				currentPage = selectedPage;
				numberPerPage = linesPerPage;

				if (1 == rowID && 0 == $("#row_" + rowID).next().length) {
					currentPage--;
					if (0 == currentPage) {
						$("#row_" + rowID).remove();
						return;
					}
				}

				queryLog();
			}
		});
	});
}

function jumpSelectedPage(linesPerPage, selectedPage, allPages) {
	if (selectedPage <= 0 || selectedPage > allPages) {
		return;
	}

	numberPerPage = linesPerPage;
	currentPage = selectedPage;

	queryLog();
}