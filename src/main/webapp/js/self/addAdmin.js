$(document).ready(function() {
	if (window.parent.$.cookie("role") == 1) {
		$("#admintype").attr("disabled", "true");
	}
	$("#addAdminForm").validate({
		submitHandler: function(form) {
			var roleValue = $("#admintype").val();
			var data = {
				username: $("#username").val(),
				password: $("#password").val(),
				phone: $("#telephone").val(),
				email: $("#email").val(),
				role: roleValue,
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
				url: "/game/manager",
				success: function(data) {
					alert(window.parent.errorLangConfig["1000"]);
					$("#addAdminForm").get(0).reset();
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
			password: {
				required: true,
				isPassword: true
			},
			againpassword: {
				required: true,
				isRepPassword: "#password"
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
});