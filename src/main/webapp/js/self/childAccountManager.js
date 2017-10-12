/**
 * 描述：处理子帐号账户管理
 *
 * @author xiongyanan
 * @date 2014-8-12
 *
 */

var childAccountCount = 0;

var numberPerPage = 10;
var currentPage = 1;
var allPages = 1;
var pagedata;
var currentRow;

$(function() {
	loadAdminJS();
	if (window.parent.$.cookie("role") == "4")
		addChildTableHead();
	else
		addDataTableHead();
	getAllChildAccount();

	$("#selectlines").change(function() {
		numberPerPage = $("#selectlines").find("option:selected").text();
		numberPerPage = parseInt(numberPerPage);

		if (childAccountCount == numberPerPage) {
			currentPage = 1;
		} else if (childAccountCount < currentPage * numberPerPage) {
			currentPage = (childAccountCount - childAccountCount % numberPerPage) / numberPerPage + 1;
		}

		getAllChildAccount();
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

function addChildTableHead() {
	$("#childaccounthead").find("tr>th").remove();
	$("#childaccountbody").find("tr>td").remove();
	var newth = $('<tr>');
	newth.append("<th><span class='language' data-word='userName'>用户名</span></th>")
		.append("<th><span class='language' data-word='useState'>使用状态</span></th>")
		.append("<th><span class='language' data-word='lockStatus'>锁定状态</span></th>")
		.append("<th><span class='language' data-word='accountOpeningDate'>开户日期</span></th>");
	$("#childaccounthead").find("tr").replaceWith(newth);
}

function addDataTableHead() {
	$("#childaccounthead").find("tr>th").remove();
	$("#childaccountbody").find("tr>td").remove();
	var newth = $('<tr>');
	newth.append("<th><span class='language' data-word='detail'>详细</span></th>")
		.append("<th><span class='language' data-word='userName'>用户名</span></th>")
		.append("<th><span class='language' data-word='useState'>使用状态</span></th>")
		.append("<th><span class='language' data-word='lockStatus'>锁定状态</span></th>")
		.append("<th><span class='language' data-word='accountOpeningDate'>开户日期</span></th>")
		.append("<th><span class='language' data-word='enableOrDisable'>启用/停用</span></th>")
		.append("<th><span class='language' data-word='lockOrUnlock'>解锁/锁定</span></th>");
	$("#childaccounthead").find("tr").replaceWith(newth);
	if (window.parent.$.cookie("role") == 0) {
		addDeleteHead("childaccounthead");
	}
}

function getAllChildAccount(isResize) {
	var uuid = getUrlParam("uuid");
	if (null == uuid) {
		uuid = window.parent.$.cookie("uuid");
	}

	var url = "";
	if (window.parent.$.cookie("role") == 4 && getUrlParam("uuid") == null) {
		url = "/game/child/" + uuid + "/child?num=" + numberPerPage + "&page=" + currentPage;
	} else {
		url = "/game/user/" + uuid + "/child?num=" + numberPerPage + "&page=" + currentPage;
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
		//url: "/game/user/" + uuid + "/child?num=" + numberPerPage + "&page=" + currentPage,
		success: function(data) {
			pagedata = data.list;
			childAccountCount = data.count;
			addChildAccountToDataTable(data.count, data.list);
			if (isResize == null)
				window.parent.frameLoad();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}


function addChildAccountToDataTable(count, list) {
	$("#childaccountbody").find("tr>td").remove();

	if (null == list || 0 == list.length || 0 == count)
		return;

	for (var index = 0; index < list.length; index++) {
		var rowID = index + 1;
		var date = new Date();
		date.setTime(list[index].createdTime);
		// var d = date.toLocaleDateString();
		// var t = date.toLocaleTimeString();
		// date = d + t;
		date = date.Format("yyyy-MM-dd");

		var btnDetail = $("#txtDetail").html();

		var state = parseInt(list[index].state) == 0 ? $("#txtEnabled").html() : $("#txtDisabled").html();
		var btnState = parseInt(list[index].state) != 0 ? $("#txtEnable").html() : $("#txtDisable").html();

		var locked = (list[index].locked) == true ? $("#txtLocked").html() : $("#txtUnlocked").html();
		var btnLocked = (list[index].locked) != true ? $("#txtLock").html() : $("#txtUnlock").html();

		var stateColor = (list[index].state == 0) ? "blue" : "red";
		var lockedColor = (list[index].locked == true) ? "red" : "blue";

		$("#row_" + rowID).remove(); //移除表格之前的行

		var newRow = $("<tr id=row_" + rowID + "></tr>");
		newRow.addClass((index & 1) ? "success odd" : "warning even"); //通过判断末尾为1还是0来判断奇偶
		if (window.parent.$.cookie("role") == "4") {
			newRow.append("<td>" + list[index].username + "</td>")
				.append("<td id='isuse_" + rowID + "' style='color:" + stateColor + "'>" + state + "</td>")
				.append("<td id='islocked_" + rowID + "' style='color:" + lockedColor + "'>" + locked + "</td>")
				.append("<td>" + date + "</td>");
			$("#childaccountbody").find("tr:last").after(newRow);
		} else {
			newRow.append("<td><button type='button' class='btn btn-primary' onclick='lookUpChildAccountDetail(" + rowID + ");'>" + btnDetail + "</button></td>")
				.append("<td>" + list[index].username + "</td>")
				.append("<td id='isuse_" + rowID + "' style='color:" + stateColor + "'>" + state + "</td>")
				.append("<td id='islocked_" + rowID + "' style='color:" + lockedColor + "'>" + locked + "</td>")
				.append("<td>" + date + "</td>")
				.append("<td><button id='btnstate_" + rowID + "' type='button' class='btn btn-warning' onclick='startOrStopChildAccount(" + rowID + ",\"" + list[index].uuid + "\"," + list[index].state + ");'>" + btnState + "</button></td>")
				.append("<td><button id='btnlocked_" + rowID + "' type='button' class='btn btn-warning' onclick='lockOrUnlockChildAccount(" + rowID + ",\"" + list[index].uuid + "\"," + list[index].locked + ");'>" + btnLocked + "</button></td>");
			$("#childaccountbody").find("tr:last").after(newRow);
		}

		parseInt(list[index].state) == 0 ? $("#btnlocked_" + rowID).removeAttr("disabled").removeClass("btn-inverse") : $("#btnlocked_" + rowID).attr("disabled", "true").addClass("btn-inverse");

		if (window.parent.$.cookie("role") == 0) {
			addDeleteBody(rowID, list[index].uuid, currentPage, numberPerPage, "childaccountbody");
		}
	}

	allPages = (count - count % numberPerPage) / numberPerPage + (count % numberPerPage == 0 ? 0 : 1);
	showPages(list.length, numberPerPage, currentPage, allPages);
}

function lookUpChildAccountDetail(rowID) {
	currentRow = rowID - 1;
	var userName = pagedata[currentRow].username;
	var money = pagedata[currentRow].point;
	var winLoseMoney = pagedata[currentRow].profit;
	var maxWinMoney = pagedata[currentRow].upperLimit;
	var preferentialCount = pagedata[currentRow].privilegeNum;
	var telephone = pagedata[currentRow].phone;
	var email = pagedata[currentRow].email;
	$("#username").val(userName);
	$("#telephone").val(telephone);
	$("#email").val(email);
	$(".frontDiv").css("display", "none");
	$(".backDiv").css("display", "block");
	curScrollTop = window.parent.document.body.scrollTop;
	window.parent.document.body.scrollTop = 0;
	window.parent.frameLoad();
}

function startOrStopChildAccount(rowID, uuid, state) {
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
			// $("#btnstate_" + rowID).removeClass();
			$("#btnstate_" + rowID).html((0 == state ? $("#txtEnable").html() : $("#txtDisable").html()));
			// $("#btnstate_" + rowID).addClass((1 == state ? "btn btn-warning" : "btn btn-primary"));
			1 == state ? $("#btnlocked_" + rowID).removeAttr("disabled").removeClass("btn-inverse") : $("#btnlocked_" + rowID).attr("disabled", "true").addClass("btn-inverse");

			$("#btnstate_" + rowID).unbind();
			$("#btnstate_" + rowID).removeAttr("onclick");
			$("#btnstate_" + rowID).bind("click", function() {
				startOrStopChildAccount(rowID, uuid, (0 == state ? 1 : 0));
			});
		}
	});
}

function lockOrUnlockChildAccount(rowID, uuid, locked) {
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

			// $("#btnlocked_" + rowID).removeClass();
			$("#btnlocked_" + rowID).html((true == locked ? $("#txtLock").html() : $("#txtUnlock").html()));
			// $("#btnlocked_" + rowID).addClass((true == locked ? "btn btn-warning" : "btn btn-primary"));

			$("#btnlocked_" + rowID).unbind();
			$("#btnlocked_" + rowID).removeAttr("onclick");
			$("#btnlocked_" + rowID).bind("click", function() {
				lockOrUnlockChildAccount(rowID, uuid, !locked);
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

				getAllChildAccount();
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
	getAllChildAccount();
}