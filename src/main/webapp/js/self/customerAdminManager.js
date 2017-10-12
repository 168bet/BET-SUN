var allCustomerAdmin = 0;
var numberPerPage = 10;
var currentPage = 1;
var allPages = 1;
var currentRow;
var pagedata;

$(function() {
	loadAdminJS();
	addDataTableHead();
	loadData();

	$("#selectlines").change(function() {
		numberPerPage = $("#selectlines").find("option:selected").text();
		numberPerPage = parseInt(numberPerPage);

		if (allCustomerAdmin == numberPerPage) {
			currentPage = 1;
		} else if (allCustomerAdmin < currentPage * numberPerPage) {
			currentPage = (allCustomerAdmin - allCustomerAdmin % numberPerPage) / numberPerPage + 1;
		}

		loadData();
	});
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

function addDataTableHead() {
	$("#customeradminhead").find("tr>th").remove();
	$("#customeradminbody").find("tr>td").remove();
	var newth = $('<tr>');
	newth.append("<th><span class='language' data-word='detail'>详细</th>")
		.append("<th><span class='language' data-word='userName'>用户名</span></th>")
		.append("<th><span class='language' data-word='authority'>权限</span></th>")
		.append("<th><span class='language' data-word='useState'>使用状态</span></th>")
		.append("<th><span class='language' data-word='lockStatus'>锁定状态</span></th>")
		.append("<th><span class='language' data-word='registerDate'>注册日期</span></th>")
		.append("<th><span class='language' data-word='enableOrDisable'>启用/停用</span></th>")
		.append("<th><span class='language' data-word='lockOrUnlock'>解锁/锁定</span></th>");
	$("#customeradminhead").find("tr").replaceWith(newth);
	if (window.parent.$.cookie("role") == 0) {
		addDeleteHead("customeradminhead");
	}
}

function loadData(isResize) {
	var uuid = window.parent.$.cookie("uuid");
	var role = 2; //客服管理员
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "get",
		dataType: "json",
		url: "/game/manager?num=" + numberPerPage + "&page=" + currentPage + "&role=" + role,
		success: function(data) {
			pagedata = data.list;
			allCustomerAdmin = data.count;
			addCustomerAdminToDataTable(data.count, data.list);
			if (isResize == null)
				window.parent.frameLoad();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}

function addCustomerAdminToDataTable(count, list) {
	$("#customeradminbody").find("tr>td").remove();

	if (null == list || 0 == list.length || 0 == count)
		return;

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

		var stateColor = (list[index].state == 0) ? "blue" : "red";
		var lockedColor = (list[index].locked == true) ? "red" : "blue";

		var rolename = $("#txtAuthority").html();


		$("#row_" + rowID).remove(); //移除表格之前的行

		var newRow = $("<tr id=row_" + rowID + "></tr>");
		newRow.addClass((index & 1) ? "success odd" : "warning even"); //通过判断末尾为1还是0来判断奇偶
		newRow.append("<td><button type='button' class='btn btn-primary' onclick='lookUpCustomerAdminDetail(" + rowID + ");'>" + btnDetail + "</button></td>")
			.append("<td>" + pagedata[index].username + "</td>")
			.append("<td>" + rolename + "</td>")
			.append("<td id='isuse_" + rowID + "' style='color:" + stateColor + "'>" + state + "</td>")
			.append("<td id='islocked_" + rowID + "' style='color:" + lockedColor + "'>" + locked + "</td>")
			.append("<td>" + date + "</td>")
			.append("<td><button id='btnstate_" + rowID + "' type='button' class='btn btn-warning' onclick='startOrStopCustomerAdmin(" + rowID + ",\"" + list[index].uuid + "\"," + list[index].state + ");'>" + btnState + "</button></td>")
			.append("<td><button id='btnlocked_" + rowID + "' type='button' class='btn btn-warning' onclick='lockOrUnlockCustomerAdmin(" + rowID + ",\"" + list[index].uuid + "\"," + list[index].locked + ");'>" + btnLocked + "</button></td>");
		$("#customeradminbody").find("tr:last").after(newRow);

		parseInt(list[index].state) == 0 ? $("#btnlocked_" + rowID).removeAttr("disabled").removeClass("btn-inverse") : $("#btnlocked_" + rowID).attr("disabled", "true").addClass("btn-inverse");

		if (window.parent.$.cookie("role") == 0) {
			addDeleteBody(rowID, list[index].uuid, currentPage, numberPerPage, "customeradminbody");
		}

		allPages = (count - count % numberPerPage) / numberPerPage + (count % numberPerPage == 0 ? 0 : 1);
		showPages(list.length, numberPerPage, currentPage, allPages);
	}
}

function lookUpCustomerAdminDetail(rowID) {
	currentRow = rowID - 1;
	var userName = pagedata[currentRow].username;
	var telephone = pagedata[currentRow].phone;
	var email = pagedata[currentRow].email;
	var role = pagedata[currentRow].role;
	$("#username").val(userName);
	$("#telephone").val(telephone);
	$("#email").val(email);
	$("#selecttype").val(role);
	$(".frontDiv").css("display", "none");
	$(".backDiv").css("display", "block");

	curScrollTop = window.parent.document.body.scrollTop;
	window.parent.document.body.scrollTop = 0;
	window.parent.frameLoad();
}

function startOrStopCustomerAdmin(rowID, uuid, state) {
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
			$("#isuse_" + rowID).html((1 == state ? "启用" : "停用"));
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
				startOrStopCustomerAdmin(rowID, uuid, (0 == state ? 1 : 0));
			});
		}
	});
}

function lockOrUnlockCustomerAdmin(rowID, uuid, locked) {
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

			$("#islocked_" + rowID).html((false == locked ? "锁定" : "解锁"));
			$("#islocked_" + rowID).css({
				color: fontColor
			});

			// $("#btnlocked_" + rowID).removeClass();
			$("#btnlocked_" + rowID).html((true == locked ? $("#txtLock").html() : $("#txtUnlock").html()));
			// $("#btnlocked_" + rowID).addClass((true == locked ? "btn btn-warning" : "btn btn-primary"));

			$("#btnlocked_" + rowID).unbind();
			$("#btnlocked_" + rowID).removeAttr("onclick");
			$("#btnlocked_" + rowID).bind("click", function() {
				lockOrUnlockCustomerAdmin(rowID, uuid, !locked);
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
			url: "/game/manager/" + uuid,
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

				loadData();
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

	loadData();
}