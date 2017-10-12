var dataList = null;
var allAgent = 0;

var currentPage = 1;
var numberPerPage = 10;
var allPages = 1;
var currentRow;

$(function() {
	loadAdminJS();
	if (window.parent.$.cookie("role") == "4")
		addChildTableHead();
	else
		addDataTableHead();
	getAllAgent();

	$("#selectlines").change(function() {
		numberPerPage = $("#selectlines").find("option:selected").text();
		numberPerPage = parseInt(numberPerPage);

		if (allAgent == numberPerPage) {
			currentPage = 1;
		} else if (allAgent < currentPage * numberPerPage) {
			currentPage = (allAgent - allAgent % numberPerPage) / numberPerPage + 1;
		}

		getAllAgent();
	});

	//返回按钮跟多级菜单
	if (getUrlParam("isShowReturn") == "true") {
		$("#dgReturn").css("display", "block");
		$("#dgReturn").click(function() {
			history.go(-1);
		});
		var subMenu = getUrlParam("subMenu");
		$("#subMenu").html(subMenu);
	}
});

function addChildTableHead() {
	$("#agenthead").find("tr>th").remove();
	var rowHead = $('<tr>');
	rowHead.append("<th><span class='language' data-word='userName'>用户名</span></th>")
		.append("<th><span class='language' data-word='childAgent'>子代理</span></th>")
		.append("<th><span class='language' data-word='member'>会员</span></th>")
		.append("<th><span class='language' data-word='childAccount'>子帐号</span></th>")
		.append("<th><span class='language' data-word='realCount'>真人点数</span></th>")
		.append("<th><span class='language' data-word='washCodeCommissionRate'>洗码佣金比例</span></th>")
		.append("<th><span class='language' data-word='rebate'>返点</span></th>")
		.append("<th><span class='language' data-word='useState'>使用状态</span></th>")
		.append("<th><span class='language' data-word='lockStatus'>锁定状态</span></th>")
		.append("<th><span class='language' data-word='accountOpeningDate'>开户日期</span></th>");
	$("#agenthead").find("tr").replaceWith(rowHead);
}

function addDataTableHead() {
	$("#agenthead").find("tr>th").remove();
	var rowHead = $('<tr>');
	rowHead.append("<th><span class='language' data-word='detail'>详细</span></th>")
		.append("<th><span class='language' data-word='userName'>用户名</span></th>")
		.append("<th><span class='language' data-word='childAgent'>子代理</span></th>")
		.append("<th><span class='language' data-word='member'>会员</span></th>")
		.append("<th><span class='language' data-word='childAccount'>子账号</span></th>")
		.append("<th><span class='language' data-word='realCount'>真人点数</span></th>")
		.append("<th><span class='language' data-word='washCodeCommissionRate'>洗码佣金比例</span></th>")
		.append("<th><span class='language' data-word='rebate'>返点</span></th>")
		.append("<th><span class='language' data-word='useState'>使用状态</span></th>")
		.append("<th><span class='language' data-word='lockStatus'>锁定状态</span></th>")
	// .append("<th><span class='language' data-word='onlyCheckAccounts'>只查账</span></th>")
	.append("<th><span class='language' data-word='accountOpeningDate'>开户日期</span></th>")
		.append("<th><span class='language' data-word='enableOrDisable'>启用/停用</span></th>")
		.append("<th><span class='language' data-word='lockOrUnlock'>解锁/锁定</span></th>");
	// .append("<th><span class='language' data-word='onlyCheckAccounts'>只查账</span></th>");
	$("#agenthead").find("tr").replaceWith(rowHead);

	if (window.parent.$.cookie("role") == 0) {
		addDeleteHead("agenthead");
	}
}

function loadAdminJS() {
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
}

function getAllAgent(isResize) {
	var uuid = getUrlParam("uuid");
	if (null == uuid) {
		uuid = window.parent.$.cookie("uuid");
	}

	var url = "";
	if (window.parent.$.cookie("role") == 4 && getUrlParam("uuid") == null) {
		url = "/game/child/" + uuid + "/agent?page=" + currentPage + "&num=" + numberPerPage;
	} else {
		url = "/game/user/" + uuid + "/agent?page=" + currentPage + "&num=" + numberPerPage;
	}

	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: true,
		type: "get",
		dataType: "json",
		url: url,
		//url: "/game/user/" + uuid + "/agent?page=" + currentPage + "&num=" + numberPerPage,
		success: function(data) {
			dataList = data.list;
			allAgent = data.count;
			addAgentToDataTable(data.count, data.list);
			if (isResize == null)
				window.parent.frameLoad();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}

function addAgentToDataTable(count, list) {
	$("#agentbody").find("tr>td").remove();

	if (null == list) {
		return;
	} else if (0 == count || 0 == list.length) {
		return;
	}

	for (var index = 0; index < list.length; index++) {
		var rowID = index + 1;
		var date = new Date();
		date.setTime(list[index].createdTime);
		date = date.Format("yyyy-MM-dd");

		var btnDetail = $("#txtDetail").html();

		var state = parseInt(list[index].state) == 0 ? $("#txtEnabled").html() : $("#txtDisabled").html();
		var btnState = parseInt(list[index].state) != 0 ? $("#txtEnable").html() : $("#txtDisable").html();

		var locked = (list[index].locked) == true ? $("#txtLocked").html() : $("#txtUnlocked").html();
		var btnLocked = (list[index].locked) != true ? $("#txtLock").html() : $("#txtUnlock").html();

		var checkAccounts = (true == list[index].onlyRead ? $("#txtYes").html() : $("#txtNo").html());
		var btnCheckAccounts = (true != list[index].onlyRead ? $("#txtYes").html() : $("#txtNo").html());

		var stateColor = (list[index].state == 0) ? "blue" : "red";
		var lockedColor = (list[index].locked == true) ? "red" : "blue";
		var checkAccountsColor = (false == list[index].onlyRead ? "blue" : "red");

		var subAgentNum = getSubAgentNum(list[index].uuid);
		var subUserNum = getSubUserNum(list[index].uuid);
		var subChildAccountNum = getSubChildAccountNum(list[index].uuid);

		$("#row_" + rowID).remove(); //移除表格之前的行

		var newRow = $("<tr id=row_" + rowID + " styles='text-align:center'></tr>");
		newRow.addClass((index & 1) ? "success odd" : "warning even");
		if (window.parent.$.cookie("role") == "4") {
			newRow.append("<td>" + list[index].username + "</td>")
				.append("<td><button id='lookchildagent_" + rowID + "' type='button' class='btn btn-primary' onclick='lookUpChildAgent(\"" + list[index].uuid + "\",\"" + list[index].username + "\");'>" + subAgentNum + "</button></td>")
				.append("<td><button id='lookchilduser_" + rowID + "' type='button' class='btn btn-primary' onclick='lookUpChildUser(\"" + list[index].uuid + "\",\"" + list[index].username + "\");'>" + subUserNum + "</button></td>")
				.append("<td><button id='lookchildaccount_" + rowID + "' type='button' class='btn btn-primary' onclick='lookUpChildAccount(\"" + list[index].uuid + "\",\"" + list[index].username + "\");'>" + subChildAccountNum + "</button></td>")
				.append("<td>" + list[index].point + "</td>")
				.append("<td>" + list[index].commissionPer + "%</td>")
				.append("<td>" + list[index].rebate + "%</td>")
				.append("<td id='isuse_" + rowID + "' style='color:" + stateColor + "'>" + state + "</td>")
				.append("<td id='islocked_" + rowID + "' style='color:" + lockedColor + "'>" + locked + "</td>")
			// .append("<td id='isonlyread_" + rowID + "' style='color:" + checkAccountsColor + "'>" + checkAccounts + "</td>")
			.append("<td>" + date + "</td>");
			// .append("<td><button id='startorstopagent_" + rowID + "' type='button' class='btn btn-warning' onclick='startOrStopAgent(" + rowID + ",\"" + list[index].uuid + "\"," + list[index].state + ");'>" + btnState + "</button></td>")
			// .append("<td><button id='lockorunlockagent_" + rowID + "' type='button' class='btn btn-warning' onclick='lockOrUnlockAgent(" + rowID + ",\"" + list[index].uuid + "\"," + list[index].locked + ");'>" + btnLocked + "</button></td>")
			// .append("<td><button id='isonlycheckaccount_" + rowID + "' type='button' class='btn btn-warning' onclick='isOnlyCheckAccount(" + rowID + ",\"" + list[index].uuid + "\"," + list[index].onlyRead + ");'>" + btnCheckAccounts + "</button></td>");
			$("#agentbody").find("tr:last").after(newRow);
		} else {
			newRow.append("<td><button id='agentdetail_" + rowID + "' type='button' class='btn btn-primary' onclick='lookUpAgentDetail(" + rowID + ",\"" + list[index].uuid + "\");'>" + btnDetail + "</button></td>")
				.append("<td>" + list[index].username + "</td>")
				.append("<td><button id='lookchildagent_" + rowID + "' type='button' class='btn btn-primary' onclick='lookUpChildAgent(\"" + list[index].uuid + "\",\"" + list[index].username + "\");'>" + subAgentNum + "</button></td>")
				.append("<td><button id='lookchilduser_" + rowID + "' type='button' class='btn btn-primary' onclick='lookUpChildUser(\"" + list[index].uuid + "\",\"" + list[index].username + "\");'>" + subUserNum + "</button></td>")
				.append("<td><button id='lookchildaccount_" + rowID + "' type='button' class='btn btn-primary' onclick='lookUpChildAccount(\"" + list[index].uuid + "\",\"" + list[index].username + "\");'>" + subChildAccountNum + "</button></td>")
				.append("<td>" + list[index].point + "</td>")
				.append("<td>" + list[index].commissionPer + "%</td>")
				.append("<td>" + list[index].rebate + "%</td>")
				.append("<td id='isuse_" + rowID + "' style='color:" + stateColor + "'>" + state + "</td>")
				.append("<td id='islocked_" + rowID + "' style='color:" + lockedColor + "'>" + locked + "</td>")
			// .append("<td id='isonlyread_" + rowID + "' style='color:" + checkAccountsColor + "'>" + checkAccounts + "</td>")
			.append("<td>" + date + "</td>")
				.append("<td><button id='startorstopagent_" + rowID + "' type='button' class='btn btn-warning' onclick='startOrStopAgent(" + rowID + ",\"" + list[index].uuid + "\"," + list[index].state + ");'>" + btnState + "</button></td>")
				.append("<td><button id='lockorunlockagent_" + rowID + "' type='button' class='btn btn-warning' onclick='lockOrUnlockAgent(" + rowID + ",\"" + list[index].uuid + "\"," + list[index].locked + ");'>" + btnLocked + "</button></td>");
			// .append("<td><button id='isonlycheckaccount_" + rowID + "' type='button' class='btn btn-warning' onclick='isOnlyCheckAccount(" + rowID + ",\"" + list[index].uuid + "\"," + list[index].onlyRead + ");'>" + btnCheckAccounts + "</button></td>");
			$("#agentbody").find("tr:last").after(newRow);
		}

		if (0 == subAgentNum) {
			$("#lookchildagent_" + rowID).attr("disabled", true);
		} else {
			$("#lookchildagent_" + rowID).removeAttr("disabled");
		}

		if (0 == subUserNum) {
			$("#lookchilduser_" + rowID).attr("disabled", true);
		} else {
			$("#lookchilduser_" + rowID).removeAttr("disabled");
		}

		if (0 == subChildAccountNum) {
			$("#lookchildaccount_" + rowID).attr("disabled", true);
		} else {
			$("#lookchildaccount_" + rowID).removeAttr("disabled");
		}

		if (window.parent.$.cookie("role") == 0) {
			addDeleteBody(rowID, list[index].uuid, currentPage, numberPerPage, "agentbody");
		}

		parseInt(list[index].state) == 0 ? $("#lockorunlockagent_" + rowID).removeAttr("disabled").removeClass("btn-inverse") : $("#lockorunlockagent_" + rowID).attr("disabled", "true").addClass("btn-inverse");
		parseInt(list[index].state) == 0 ? $("#isonlycheckaccount_" + rowID).removeAttr("disabled").removeClass("btn-inverse") : $("#isonlycheckaccount_" + rowID).attr("disabled", "true").addClass("btn-inverse");
	}

	allPages = (count - count % numberPerPage) / numberPerPage + (count % numberPerPage == 0 ? 0 : 1);
	showPages(list.length, numberPerPage, currentPage, allPages);
}

function getSubAgentNum(uuid) {
	var subAgentNum = 0;
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "get",
		dataType: "json",
		url: "/game/user/" + uuid + "/agent?page=" + currentPage + "&num=" + numberPerPage,
		success: function(data) {
			subAgentNum = data.count;
		},
		error: function(XMLHttpRequest, textStatus, errorThrow) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
	return subAgentNum;
}

function getSubUserNum(uuid) {
	var subUserNum = 0;
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "get",
		dataType: "json",
		url: "/game/user/" + uuid + "/member?num=" + numberPerPage + "&page=" + currentPage,
		success: function(data) {
			subUserNum = data.count;
		},
		error: function(XMLHttpRequest, textStatus, errorThrow) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
	return subUserNum;
}

function getSubChildAccountNum(uuid) {
	var subChildAccountNum = 0;
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "get",
		dataType: "json",
		url: "/game/user/" + uuid + "/child?num=" + numberPerPage + "&page=" + currentPage,
		success: function(data) {
			subChildAccountNum = data.count;
		},
		error: function(XMLHttpRequest, textStatus, errorThrow) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
	return subChildAccountNum;
}

function lookUpAgentDetail(rowID, uuid) {
	currentRow = rowID - 1;

	if (null != dataList) {
		$("#username").val(dataList[rowID - 1].username);
		$("#money").val(dataList[rowID - 1].point);
		$("#telephone").val(dataList[rowID - 1].phone);
		$("#email").val(dataList[rowID - 1].email);
		$("#washcodepercent").val(dataList[rowID - 1].commissionPer);
		$("#rebate").val(dataList[rowID - 1].rebate);
	}

	$(".frontDiv").css("display", "none");
	$(".backDiv").css("display", "block");

	curScrollTop = window.parent.document.body.scrollTop;
	window.parent.document.body.scrollTop = 0;
	window.parent.frameLoad();
}

function lookUpChildAgent(uuid, uname) {
	var params = {
		uuid: uuid,
		currentPage: "1",
		numberPerPage: "10",
		isShowReturn: true,
		subMenu: $("#subMenu").html() + "<img src='/game/img/breadcrumb.png'><span>" + uname + "</span>",
	};
	var url = "underling-manage.html";
	window.parent.redirect(url, params);
}

function lookUpChildUser(uuid, uname) {
	var params = {
		uuid: uuid,
		currentPage: "1",
		numberPerPage: "10",
		isShowReturn: true,
		subMenu: $("#subMenu").html() + "<img src='/game/img/breadcrumb.png'><span>" + uname + "</span>",
	};
	var url = "user-manage.html";
	window.parent.redirect(url, params);
}

function lookUpChildAccount(uuid, uname) {
	var params = {
		uuid: uuid,
		currentPage: "1",
		numberPerPage: "10",
		isShowReturn: true,
		subMenu: $("#subMenu").html() + "<img src='/game/img/breadcrumb.png'><span>" + uname + "</span>",
	};
	var url = "child-account-manage.html";
	window.parent.redirect(url, params);
}

function startOrStopAgent(rowID, uuid, state) {
	var data = {};
	data.state = (0 == state ? 1 : 0);

	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "put",
		url: "/game/user/" + uuid + "/state",
		data: JSON.stringify(data),
		contentType: "application/json",
		success: function(data) {
			var fontColor = (1 == state ? "blue" : "red");

			$("#isuse_" + rowID).html((1 == state ? $("#txtEnabled").html() : $("#txtDisabled").html()));
			$("#isuse_" + rowID).css({
				color: fontColor
			});

			// $("#startorstopagent_" + rowID).removeClass();
			$("#startorstopagent_" + rowID).html((0 == state ? $("#txtEnable").html() : $("#txtDisable").html()));
			// $("#startorstopagent_" + rowID).addClass(1 == state ? "btn btn-warning" : "btn btn-primary");
			1 == state ? $("#lockorunlockagent_" + rowID).removeAttr("disabled").removeClass("btn-inverse") : $("#lockorunlockagent_" + rowID).attr("disabled", "true").addClass("btn-inverse");
			1 == state ? $("#isonlycheckaccount_" + rowID).removeAttr("disabled").removeClass("btn-inverse") : $("#isonlycheckaccount_" + rowID).attr("disabled", "true").addClass("btn-inverse");

			$("#startorstopagent_" + rowID).unbind();
			$("#startorstopagent_" + rowID).removeAttr("onclick");
			$("#startorstopagent_" + rowID).bind("click", function() {
				startOrStopAgent(rowID, uuid, (0 == state ? 1 : 0));
			});
		}
	});
}

function lockOrUnlockAgent(rowID, uuid, locked) {
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "put",
		url: "/game/user/" + uuid + "/locked",
		contentType: "application/json",
		success: function(data) {
			var fontColor = (true == locked ? "blue" : "red");

			$("#islocked_" + rowID).html((false == locked ? $("#txtLocked").html() : $("#txtUnlocked").html()));
			$("#islocked_" + rowID).css({
				color: fontColor
			});

			// $("#lockorunlockagent_" + rowID).removeClass();
			$("#lockorunlockagent_" + rowID).html((true == locked ? $("#txtLock").html() : $("#txtUnlock").html()));
			// $("#lockorunlockagent_" + rowID).addClass((true == locked ? "btn btn-warning" : "btn btn-primary"));

			$("#lockorunlockagent_" + rowID).unbind();
			$("#lockorunlockagent_" + rowID).removeAttr("onclick");
			$("#lockorunlockagent_" + rowID).bind("click", function() {
				lockOrUnlockAgent(rowID, uuid, !locked);
			});
		}
	});
}

function isOnlyCheckAccount(rowID, uuid, onlyRead) {
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "put",
		url: "/game/agent/" + uuid + "/onlyRead",
		contentType: "application/json",
		success: function(data) {
			var fontColor = (true == onlyRead ? "blue" : "red");

			$("#isonlyread_" + rowID).html((false == onlyRead ? $("#txtYes").html() : $("#txtNo").html()));
			$("#isonlyread_" + rowID).css({
				color: fontColor
			});

			// $("#isonlycheckaccount_" + rowID).removeClass();
			$("#isonlycheckaccount_" + rowID).html((true == onlyRead ? $("#txtYes").html() : $("#txtNo").html()));
			// $("#isonlycheckaccount_" + rowID).addClass(true == onlyRead ? "btn btn-warning" : "btn btn-primary");

			$("#isonlycheckaccount_" + rowID).unbind();
			$("#isonlycheckaccount_" + rowID).removeAttr("onclick");
			$("#isonlycheckaccount_" + rowID).bind("click", function() {
				isOnlyCheckAccount(rowID, uuid, !onlyRead);
			});
		}
	});
}

function deleteInfo(rowID, selectedPage, linesPerPage, uuid) {
	window.parent.showDeleteConfirmDialog(function() {
		$.ajax({
			headers: {
				"uuid": window.parent.$.cookie("uuid"),
				"role": window.parent.$.cookie("role"),
			},
			async: true,
			type: "delete",
			url: "/game/agent/" + uuid,
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

				getAllAgent();
			}
		});
	})
}

function jumpSelectedPage(linesPerPage, selectedPage, allPages) {
	if (selectedPage <= 0 || selectedPage > allPages) {
		return;
	}

	numberPerPage = linesPerPage;
	currentPage = selectedPage;

	getAllAgent();
}