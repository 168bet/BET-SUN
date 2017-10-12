/**
 * 描述：处理输赢报表
 *
 * @author xiongyanan
 * @date 2014-8-11
 *
 */

var dataList = null;

var allStatements = 0;
var numberPerPage = 10;
var currentPage = 1;
var allPages = 1;

var curUserId = "";

var isBtnCheckAgentEnabled = false;

$(function() {
	checkSpeAuthority();

	initValidate();
	searchData();

	$("#btnEmpty").click(function() {
		emptySearchData();
	});

	$("#selectlines").change(function() {
		numberPerPage = $("#selectlines").find("option:selected").text();
		numberPerPage = parseInt(numberPerPage);

		if (allStatements == numberPerPage) {
			currentPage = 1;
		} else if (allStatements < currentPage * numberPerPage) {
			currentPage = (allStatements - allStatements % numberPerPage) / numberPerPage + 1;
		}

		searchData();
	});
});

//公司直属、代理、返回按钮、多级菜单等控制
function checkSpeAuthority() {
	var role = window.parent.$.cookie("role");
	if (role == "0" || role == "1" || role == "2") {
		isBtnCheckAgentEnabled = true;
	}
	if (getUrlParam("isShowReturn") == "true") {
		$("#dgReturn").css("display", "block");
		$("#dgReturn").click(function() {
			history.go(-1);
		});
		var subMenu = getUrlParam("subMenu");
		$("#subMenu").html(subMenu);
		isBtnCheckAgentEnabled = false;
		curUserId = getUrlParam("uuid");
	}
	//表头控制
	if ((role != "0" && role != "1" && role != "2") || (curUserId != "manager" && curUserId != "")) {
		addAgentTableHead();
	} else {
		addManagerTableHead();
	}
}

function searchData() {
	var userName = $("#userName").val();
	var beginTime = Date.parse($("#begindate input").val());
	var endTime = Date.parse($("#enddate input").val());
	if (isNaN(beginTime))
		beginTime = "";
	if (isNaN(endTime))
		endTime = "";
	searchDataByCondition(userName, beginTime, endTime);
}

function emptySearchData() {
	$("#userName").val("");
	$("#begindate input").val("");
	$("#enddate input").val("");
	searchData();
}

function searchDataByCondition(userName, beginTime, endTime) {
	var userId = curUserId == "" ? window.parent.$.cookie("uuid") : curUserId;
	//ajax 获取数据
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: true,
		type: "get",
		dataType: "json",
		url: "/game/report/" + userId + "/winlose?num=" + numberPerPage + "&page=" + currentPage + "&username=" + userName + "&startTime=" + beginTime + "&finishTime=" + endTime,
		success: function(data) {
			dataList = data.list;
			allStatements = data.count;
			loadData(data.count, data.list);
			window.parent.frameLoad();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}

function addManagerTableHead() {
	$("#winresulthead").find("tr>th").remove();
	$("#winresultbody").find("tr>td").remove();

	var newth = $('<tr>');
	newth.append("<th><span class='language' data-word='userName'>用户名</th>")
		.append("<th><span class='language' data-word='parentAgent'>所属代理</span></th>")
		.append("<th><span class='language' data-word='winLossAmount'>输赢金额</span></th>")
		.append("<th><span class='language' data-word='betAmount'>投注金额</span></th>")
		.append("<th><span class='language' data-word='efficientMoney'>有效金额</span></th>")
		.append("<th><span class='language' data-word='companyGrossProfit'>公司毛盈利</span></th>")
		.append("<th><span class='language' data-word='moneyPayToSubordinate'>支付下级代理金额</span></th>")
		.append("<th><span class='language' data-word='companyPureProfit'>公司纯盈利</span></th>");
	$("#winresulthead").find("tr").replaceWith(newth);
}

function addAgentTableHead() {
	$("#winresulthead").find("tr>th").remove();
	$("#winresultbody").find("tr>td").remove();

	var newth = $('<tr>');
	newth.append("<th><span class='language' data-word='userName'>用户名</th>")
		.append("<th><span class='language' data-word='parentAgent'>所属代理</span></th>")
		.append("<th><span class='language' data-word='winLossAmount'>输赢金额</span></th>")
		.append("<th><span class='language' data-word='betAmount'>投注金额</span></th>")
		.append("<th><span class='language' data-word='efficientMoney'>有效金额</span></th>")
		.append("<th><span class='language' data-word='agentGrossProfit'>代理毛盈利</span></th>")
		.append("<th><span class='language' data-word='moneyPayToSubordinate'>支付下级代理金额</span></th>")
		.append("<th><span class='language' data-word='agentPureProfit'>代理纯盈利</span></th>");
	$("#winresulthead").find("tr").replaceWith(newth);
}

function loadData(count, list) {
	$("#winresultbody").find("tr>td").remove();

	if (null == list || 0 == list.length || 0 == count)
		return;

	for (var index = 0; index < list.length; index++) {
		var rowID = index + 1;

		$("#row_" + rowID).remove(); //移除表格之前的行
		var agentName = list[index].agent == "manager" ? $("#txtCompany").html() : list[index].agent;
		var newRow = $("<tr id=row_" + rowID + "></tr>");
		newRow.addClass((index & 1) ? "success odd" : "warning even");
		newRow.append("<td>" + list[index].username + "</td>")
			.append("<td><button type='button' class='btn btn-primary btnCheckAgent' onclick='lookUp(\"" + list[index].agentId + "\",\"" + list[index].agent + "\");'>" + agentName + "</button></td>")
			.append("<td>" + list[index].winLose + "</td>")
			.append("<td>" + list[index].betAmount + "</td>")
			.append("<td>" + list[index].effectAmount + "</td>")
			.append("<td>" + list[index].agentProfit + "</td>")
			.append("<td>" + list[index].agentPay + "</td>")
			.append("<td>" + list[index].agentPureProfit + "</td>");
		//.append("<td>" + list[index].companyProfit + "</td>");

		$("#winresultbody").find("tr:last").after(newRow);
	}

	if (isBtnCheckAgentEnabled == false) {
		$(".btnCheckAgent").removeAttr("onclick");
	}

	loadSumData(list);

	allPages = (count - count % numberPerPage) / numberPerPage + (count % numberPerPage == 0 ? 0 : 1);
	showPages(list.length, numberPerPage, currentPage, allPages);
}

function loadSumData(list) {
	var sumWinLose = 0.0;
	var sumBetAmount = 0.0;
	var sumEffectAmount = 0.0;
	var sumAgentProfit = 0.0;
	var sumAgentPay = 0.0;
	var sumAgentPureProfit = 0.0;
	for (var i = 0; i < list.length; i++) {
		sumWinLose += parseFloat(list[i].winLose);
		sumBetAmount += parseFloat(list[i].betAmount);
		sumEffectAmount += parseFloat(list[i].effectAmount);
		sumAgentProfit += parseFloat(list[i].agentProfit);
		sumAgentPay += parseFloat(list[i].agentPay);
		sumAgentPureProfit += parseFloat(list[i].agentPureProfit);
	}
	var newRow = $("<tr class='tr-sum'></tr>");
	newRow.append("<td></td>")
		.append("<td></td>")
		.append("<td>" + sumWinLose + "</td>")
		.append("<td>" + sumBetAmount + "</td>")
		.append("<td>" + sumEffectAmount + "</td>")
		.append("<td>" + sumAgentProfit + "</td>")
		.append("<td>" + sumAgentPay + "</td>")
		.append("<td>" + sumAgentPureProfit + "</td>");
	$("#winresultbody").find("tr:last").after(newRow);
}

function lookUp(userId, uname) {
	if (uname == "manager")
		return;
	var params = {
		uuid: userId,
		currentPage: "1",
		numberPerPage: "10",
		isShowReturn: true,
		subMenu: $("#subMenu").html() + "<img src='/game/img/breadcrumb.png'><span>" + uname + "</span>",
	};
	var url = "win-loss-form.html";
	window.parent.redirect(url, params);
}

function jumpSelectedPage(linesPerPage, selectedPage, allPages) {
	if (selectedPage <= 0 || selectedPage > allPages) {
		return;
	}

	numberPerPage = linesPerPage;
	currentPage = selectedPage;

	searchData();
}

function initValidate() {
	$("#searchForm").validate({
		submitHandler: function(form) {
			searchData();
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