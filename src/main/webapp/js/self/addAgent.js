$(function() {
	$("#addAgentForm").validate({
		submitHandler: function(form) {
			var userName = $("#username").val();
			var password = $("#password").val();
			var phone = $("#telephone").val();
			var email = $("#email").val();
			var washCodePercent = $("#washcodepercent").val();
			var rebate = $("#rebate").val();
			var parentId = $("#agentname").val();
			var moneyPwd = $("#drawPassword").val();

			var data = {};
			data.username = userName;
			data.password = password;
			data.phone = phone;
			data.email = email;
			data.commissionPer = washCodePercent;
			data.rebate = rebate;
			data.parentId = $("#agentUserName").data("uuid");
			data.moneyPwd = moneyPwd;

			$.ajax({
				headers: {
					"uuid": window.parent.$.cookie("uuid"),
					"role": window.parent.$.cookie("role"),
				},
				async: false,
				type: "post",
				url: "/game/agent",
				data: JSON.stringify(data),
				contentType: "application/json",
				success: function(data) {
					alert(window.parent.errorLangConfig["1001"]);
					$("#addAgentForm").get(0).reset();
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
			washcodepercent: {
				required: true,
				isWashCode: true
			},
			rebate: {
				required: true,
				isRebate: true
			},
			agentUserName: {
				required: true
			},
			drawPassword: {
				required: true,
				isPassword: true,
			},
			drawAgainpassword: {
				required: true,
				isRepPassword: "#drawPassword"
			}
		},
		errorPlacement: function(error, element) {
			if (element.attr("name") == "washcodepercent" || element.attr("name") == "rebate" || element.attr("name") == "agentUserName")
				element.parent().after(error);
			else
				error.appendTo(element.parent());
		},
	});

	var treeUrl = "/game/agent/tree?originid=" + window.parent.$.cookie("uuid");
	$("[data-toggle='modal']").click(function() {
		var title;
		if (window.parent.language == "zh-cn")
			title = "选择代理";
		else if (window.parent.language == "en-us")
			title = "Choose Agent";
		window.parent.showModal(title, treeUrl, false, callback)
	});
});

function callback() {
	$("#agentUserName").data("uuid", window.parent.dialogResultId);
	$("#agentUserName").val(window.parent.dialogResultName);
}