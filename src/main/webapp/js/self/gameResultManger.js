/**
 * 描述：处理输赢报表
 * 
 * @author xiongyanan
 * @date 2014-8-11
 * 
 */

var allStatements = 0;
var numberPerPage = 10;
var currentPage = 1;
var allPages = 1;

$(function() {

	addMoneyStatementTableHead();
	initValidate();
	queryGameStatement();


	$("#selectlines").change(function() {
		numberPerPage = $("#selectlines").find("option:selected").text();
		numberPerPage = parseInt(numberPerPage);

		if (allStatements == numberPerPage) {
			currentPage = 1;
		} else if (allStatements < currentPage * numberPerPage) {
			currentPage = (allStatements - allStatements % numberPerPage) / numberPerPage + 1;
		}

		queryGameStatement();
	});
});

function addMoneyStatementTableHead() {
	$("#gameresulthead").find("tr>th").remove();

	var newth = $('<tr>');
	newth.append("<th><span class='language' data-word='userName'>用户名</th>")
		.append("<th><span class='language' data-word='parentAgent'>所属代理</span></th>")
		.append("<th><span class='language' data-word='winLossAmount'>输赢金额</span></th>")
		.append("<th><span class='language' data-word='betAmount'>投注金额</span></th>")
		.append("<th><span class='language' data-word='efficientMoney'>有效金额</span></th>")
		.append("<th><span class='language' data-word='agentGrossProfit'>代理毛盈利</span></th>")
		.append("<th><span class='language' data-word='moneyPayToSubordinate'>支付下级代理金额</span></th>")
		.append("<th><span class='language' data-word='agentPureProfit'>代理纯盈利</span></th>");
	$("#gameresulthead").find("tr").replaceWith(newth);
}

function queryGameStatement() {
	var userName = $("#userName").val();
	var beginTime = Date.parse($("#begindate input").val());
	var endTime = Date.parse($("#enddate input").val());
	if (isNaN(beginTime))
		beginTime = "";
	if (isNaN(endTime))
		endTime = "";
	
	showGameStatementByQueryCondition(userName, beginTime, endTime);
}

function showGameStatementByQueryCondition(userName, beginTime, endTime) {
	//var userId = curUserId == "" ? window.parent.$.cookie("uuid") : curUserId;
	
	//ajax 获取数据
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "get",
		dataType: "json",
		url: "/game/report/" + window.parent.$.cookie("uuid") + "/winlose?num=" + numberPerPage + "&page=" + currentPage + "&username=" + userName + "&startTime=" + beginTime + "&finishTime=" + endTime,
		success: function(data) {
			allStatements = data.count;
			addGameStatementToDataTable(data.count, data.list);
			window.parent.frameLoad();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}

 function addGameStatementToDataTable(count, list) {
	$("#gameresultbody").find("tr>td").remove();

	if (null == list || 0 == list.length || 0 == count)
		return;

	for (var index = 0; index < list.length; index++) {
		var rowID = index + 1;
		$("#row_" + rowID).remove(); //移除表格之前的行

		//var agentName = list[index].agent == "manager" ? $("#txtCompany").html() : list[index].agent;
		var newRow = $("<tr id=row_" + rowID + "></tr>");
		newRow.addClass((index & 1) ? "success odd" : "warning even");
		newRow.append("<td>" + list[index].username + "</td>")
			.append("<td><button type='button' class='btn btn-primary btnCheckAgent' onclick='lookUp(\"" + list[index].agentId + "\",\"" + list[index].agent + "\");'>" + list[index].agent + "</button></td>")
			.append("<td>" + list[index].winLose + "</td>")
			.append("<td>" + list[index].betAmount + "</td>")
			.append("<td>" + list[index].effectAmount + "</td>")
			.append("<td>" + list[index].agentProfit + "</td>")
			.append("<td>" + list[index].agentPay + "</td>")
			.append("<td>" + list[index].agentPureProfit + "</td>");
		//.append("<td>" + list[index].companyProfit + "</td>");

		$("#gameresultbody").find("tr:last").after(newRow);
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
	$("#gameresultbody").find("tr:last").after(newRow);
}

function initValidate() {
	$("#searchForm").validate({
		submitHandler: function(form) {
			queryGameStatement();
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

function jumpSelectedPage(linesPerPage, selectedPage, allPages) {
	if (selectedPage <= 0 || selectedPage > allPages) {
		return;
	}

	numberPerPage = linesPerPage;
	currentPage = selectedPage;

	queryGameStatement();
}