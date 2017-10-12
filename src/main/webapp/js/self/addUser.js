$(document).ready(function() {
	var role = window.parent.$.cookie("role");
	uuid = window.parent.$.cookie("uuid");

	$("#addUserForm").validate({
		submitHandler: function(form) {
			var data = {
				username: $("#username").val(),
				name: $("#realname").val(),
				password: $("#password").val(),
				moneyPwd: $("#drawMoneyPassword").val(),
				phone: $("#telephone").val(),
				email: $("#email").val(),
				parentId: $("#agentUserName").data("uuid"),
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
				url: "/game/member",
				success: function(data) {
					alert(window.parent.errorLangConfig["1003"]);
					$("#addUserForm").get(0).reset();
				},
				error: function(XMLHttpRequest, textStatus, errorThrow) {
					alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
				}
			});
		},
		rules: {
			username: {
				required: true,
				isAccount: true
			},
			realname: {
				required: true,
				isRealName: true
			},
			password: {
				required: true,
				isPassword: true
			},
			againpassword: {
				required: true,
				isRepPassword: "#password"
			},
			drawMoneyPassword: {
				required: true,
				isPassword: true
			},
			drawMoneyPasswordAgainPassword: {
				required: true,
				isRepPassword: "#drawMoneyPassword"
			},
			telephone: {
				required: true,
				digits: true
			},
			email: {
				required: true,
				email: true
			},
		},
	});

	var treeUrl = "/game/agent/tree?originid=" + window.parent.$.cookie("uuid");
	$("[data-toggle='modal']").click(function() {
		window.parent.showModal("选择代理", treeUrl, false, callback)
	});
});

function callback() {
	$("#agentUserName").data("uuid", window.parent.dialogResultId);
	$("#agentUserName").val(window.parent.dialogResultName);
}

function onCheck() {
	var treeView = $("#treeview").data("kendoTreeView");
	checkedNodeId(treeView.dataSource.view());
}