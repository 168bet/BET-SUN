/**
 * 描述：修改密码
 *
 * @author xiongyanan
 * @date 2014-8-12
 *
 */

$(function() {
	checkIsAgent();

	$("#changePasswordForm").validate({
		submitHandler: function(form) {
			var oldPassword = $("#oldpassword").val();
			var newPassword = $("#newpassword").val();

			var data = {};
			data.prePassword = oldPassword;
			data.newPassword = newPassword;

			$.ajax({
				headers: {
					"uuid": window.parent.$.cookie("uuid"),
					"role": window.parent.$.cookie("role"),
				},
				async: false,
				type: "put",
				url: "/game/user/password",
				data: JSON.stringify(data),
				success: function(data) {
					alert(window.parent.errorLangConfig["1004"]);
					$("#changePasswordForm").get(0).reset();
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					alert(window.parent.errorLangConfig["1006"]);
				}
			});
		},
		rules: {
			oldpassword: {
				required: true,
				isPassword: true
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
	});

	$("#changeDrawPasswordForm").validate({
		submitHandler: function(form) {
			var oldPassword = $("#oldDrawPassword").val();
			var newPassword = $("#newDrawPassword").val();

			var data = {};
			data.preMoneyPswd = oldPassword;
			data.newMoneyPswd = newPassword;

			$.ajax({
				headers: {
					"uuid": window.parent.$.cookie("uuid"),
					"role": window.parent.$.cookie("role"),
				},
				async: false,
				type: "put",
				url: "/game/agent/moneypassword",
				data: JSON.stringify(data),
				success: function(data) {
					alert(window.parent.errorLangConfig["1004"]);
					$("#changeDrawPasswordForm").get(0).reset();
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					alert(window.parent.errorLangConfig["1006"]);
				}

			});
		},
		rules: {
			oldDrawPassword: {
				required: true,
				isPassword: true
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
	});
});

function checkIsAgent() {
	if (window.parent.$.cookie("role") == "3") {
		var content = $("<div>").html('<div id="breadcrumb" class="second-breadcrumb">\
			<a href="#"  style="cursor:default"class="current">\
				<span class="language" data-word="drawMoneyPassword">取款密码</span>\
			</a>\
		</div>\
		<div class="container-fluid">\
			<form id="changeDrawPasswordForm" class="form-horizontal" role="form">\
				<div class="form-group">\
					<label class="control-label">\
						<span class="language" data-word="oldPassword">旧密码</span>\
						：\
					</label>\
					<div class="controls">\
						<input type="password" class="input-large" id="oldDrawPassword" name="oldDrawPassword"/>\
					</div>\
				</div>\
				<div class="control-group">\
					<label class="control-label">\
						<span class="language" data-word="newPassword">新密码</span>\
						：\
					</label>\
					<div class="controls">\
						<input type="password" class="input-large" id="newDrawPassword" name="newDrawPassword"/>\
					</div>\
				</div>\
				<div class="control-group">\
					<label class="control-label">\
						<span class="language" data-word="confirmPassword">确认密码</span>\
						：\
					</label>\
					<div class="controls">\
						<input type="password" class="input-large" id="drawPasswordAgain" name="drawPasswordAgain"/>\
					</div>\
				</div>\
				<div class="control-group">\
					<label class="control-label"></label>\
					<div class="control-label">\
						<button type="submit" class="btn btn-warning" id="modifyDrawPassword">\
							<span class="language" data-word="commit">提交</span>\
						</button>\
					</div>\
				</div>\
			</form>\
		</div>');
		$("#in-content").append(content);
	}
}