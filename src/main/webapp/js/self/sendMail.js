$(function() {
	$("#publishForm").validate({
		submitHandler: function(form) {
			sendMail();
		},
		rules: {
			userName: {
				required: true,
			},
			_title: {
				required: true
			},
			_content: {
				required: true
			},
		},
		errorPlacement: function(error, element) {
			error.appendTo(element.parent());
		},
	});

	var treeUrl = "/game/member/tree?originid=" + window.parent.$.cookie("uuid");
	$("[data-toggle='modal']").click(function() {
		var title;
		if (window.parent.language == "zh-cn")
			title = "选择账号";
		else if (window.parent.language == "en-us")
			title = "Choose Account";
		window.parent.showModal(title, treeUrl, true, callback)
	});
});

function callback() {
	var uuids = "";
	var usernames = "";
	$.each(window.parent.dialogMultiResult, function(i, data) {
		if (i == 0)
			uuids += data.uuid;
		else
			uuids += ";" + data.uuid
		usernames += data.username + ";";
	});
	$("#userName").val(usernames);
	$("#userName").data("uuid", uuids);
}

function sendMail() {
	var userId = $("#userName").data("uuid");
	if (userId == undefined) {
		alert(window.parent.errorLangConfig["1010"]);
		return;
	}
	var title = $("#_title").val();
	var content = $("#_content").val();
	var data = {
		userId: userId,
		title: title,
		content: content
	};
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "post",
		contentType: "application/json",
		data: JSON.stringify(data),
		url: "/game/internalMsg",
		success: function(data) {
			alert(window.parent.errorLangConfig["1009"]);
			$("#publishForm").get(0).reset();
			loadData();
		},
		error: function(XMLHttpRequest, textStatus, errorThrow) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}