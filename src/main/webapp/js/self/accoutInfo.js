/**
 * 描述：帐号信息
 *
 * @author xiongyanan
 * @date 2014-9-6
 *
 */

$(function() {
	getAccountInfo();
});

function getAccountInfo() {
	var uuid = window.parent.$.cookie("uuid");
	var role = window.parent.$.cookie("role");
	var url;
	if (role == "0" || role == "1" || role == "2" || role == "4") {
		$("#realCount").parent().parent().hide();
		$("#washCodeCommissionRate").parent().parent().hide();
		$("#rebate").parent().parent().hide();
		if (role == "4") {
			$("#realName").parent().parent().hide();
			url = "/game/child/" + uuid;
		} else {
			$("#realName").parent().parent().hide();
			url = "/game/manager/" + uuid;
		}
	} else if (role == "3") {
		$("#realName").parent().parent().hide();
		url = "/game/agent/" + uuid;
	}
	loadData(url);
}

function loadData(url) {
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "get",
		dataType: "json",
		contentType: "application/json",
		url: url,
		success: function(data) {
			var state;
			var locked;
			if (window.parent.language == "zh-cn") {
				state = parseInt(data.state) == 0 ? "启用" : "停用";
				locked = data.locked == true ? "锁定" : "解锁";
			} else if (window.parent.language == "en-us") {
				state = parseInt(data.state) == 0 ? "Enabled" : "Disabled";
				locked = data.locked == true ? "Locked" : "Unlocked";
			}
			if (parseInt(data.state) == 0) {
				$("#accountStatus").attr("data-word", "enabled");
			} else {
				$("#accountStatus").attr("data-word", "disabled");
			}
			if (data.locked == true) {
				$("#lockStatus").attr("data-word", "locked");
			} else {
				$("#lockStatus").attr("data-word", "unlocked");
			}
			$("#account").val(data.username);
			$("#realName").val(data.name == null ? "" : data.name);
			$("#accountStatus").val(state);
			$("#lockStatus").val(locked);
			$("#realCount").val(data.point);
			$("#washCodeCommissionRate").val(data.commissionPer + " %");
			$("#rebate").val(data.rebate + " %");
			$("#phone").val(data.phone);
			$("#email").val(data.email);
		},
		error: function(XMLHttpRequest, textStatus, errorThrow) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}