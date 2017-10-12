$(document).ready(function() {
	getValidateCode();

	$("#imgValidate").click(function() {
		getValidateCode();
	});

	$("#loginForm").validate({
		submitHandler: function(form) {
			var data = {
				username: $("#txtName").val(),
				password: $("#txtPwd").val(),
				captcha: $("#txtValidate").val(),
			};
			$.ajax({
				async: false,
				type: "post",
				url: config.serverAddr + "/game/login",
				data: JSON.stringify(data),
				contentType: "application/json",
				success: function(data) {
					$.cookie("role", null, {
						path: "/"
					});
					$.cookie("uuid", null, {
						path: "/"
					});
					$.cookie("username", null, {
						path: "/"
					});
					$.cookie("role", data.role + "", {
						path: "/"
					});
					$.cookie("uuid", data.uuid, {
						path: "/"
					});
					$.cookie("username", data.username, {
						path: "/"
					});
					var lang;
					if(language == null)
						lang="zh-cn";
					else
						lang=language
					window.location.href = "/game/html/master-page.html?lang=" + lang;
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					alert(errorLangConfig[XMLHttpRequest.status]);
					getValidateCode();
				}
			});
		},
		rules: {
			txtName: {
				required: true,
				isAccount: true
			},
			txtPwd: {
				required: true,
				isPassword: true
			},
			txtValidate: {
				required: true,
				isValidateCode: true,
			},
		},
		errorPlacement: function(error, element) {
			if (element.attr("name") == "txtValidate")
				element.parent().after(error);
			else
				error.appendTo(element.parent());
		}
	});
});

function getValidateCode() {
	$.ajax({
		async: false,
		type: "get",
		contentType: "application/json",
		url: "/game/captcha",
		success: function(data) {
			$("#imgValidate").attr("src", data);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorLangConfig[XMLHttpRequest.status]);
		}
	});
}