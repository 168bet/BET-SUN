$(function() {
	$("#btnLoginCheckInfo").click(function() {
		var username = $("#userName").val();
		checkInfo(username);
	});

	$("#btnDrawCheckInfo").click(function() {
		var username = $("#drawUserName").val();
		checkInfo(username);
	});

	$("#changePasswordForm").validate({
		submitHandler: function(form) {
			var username = $("#userName").val();
			var newPassword = $("#newpassword").val();

			var data = {
				username: username,
				newPassword: newPassword,
			};

			$.ajax({
				headers: {
					"uuid": window.parent.$.cookie("uuid"),
					"role": window.parent.$.cookie("role"),
				},
				async: false,
				type: "put",
				url: "/game/manager/resetLoginPwd",
				data: JSON.stringify(data),
				success: function(data) {
					alert(window.parent.errorLangConfig["1004"]);
					$("#changePasswordForm").get(0).reset();
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
				}
			});
		},
		rules: {
			userName: {
				required: true,
				isAccount: true
			},
			newpassword: {
				required: true,
				isPassword: true
			},
			passwordagain: {
				required: true,
				isRepPassword: "#newpassword"
			},
		},
		errorPlacement: function(error, element) {
			error.appendTo(element.parent());
		},
	});

	$("#changeDrawPasswordForm").validate({
		submitHandler: function(form) {
			var username = $("#drawUserName").val();
			var newPassword = $("#newDrawPassword").val();

			var data = {
				username: username,
				newPassword: newPassword,
			};

			$.ajax({
				headers: {
					"uuid": window.parent.$.cookie("uuid"),
					"role": window.parent.$.cookie("role"),
				},
				async: false,
				type: "put",
				url: "/game/manager/resetMoneyPwd",
				data: JSON.stringify(data),
				success: function(data) {
					alert(window.parent.errorLangConfig["1004"]);
					$("#changeDrawPasswordForm").get(0).reset();
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
				}
			});
		},
		rules: {
			drawUserName: {
				required: true,
				isAccount: true
			},
			newDrawPassword: {
				required: true,
				isPassword: true
			},
			drawPasswordAgain: {
				required: true,
				isRepPassword: "#newDrawPassword"
			},
		},
		errorPlacement: function(error, element) {
			error.appendTo(element.parent());
		},
	});
});

function checkInfo(username) {
	if (username == "")
		return;
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "get",
		url: "/game/user/" + username,
		success: function(data) {
			window.parent.showCheckInfoModal(data);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}