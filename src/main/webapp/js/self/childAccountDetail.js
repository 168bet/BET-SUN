/**
 * 描述：子帐号详细信息
 *
 * @author xiongyanan
 * @date 2014-8-17
 *
 */

$(function() {
	$("#childAccountDetailForm").validate({
		submitHandler: function(form) {
			var uuid = pagedata[currentRow].uuid;
			var data = {
				phone: $("#telephone").val(),
				email: $("#email").val(),
			};
			$.ajax({
				headers: {
					"uuid": window.parent.$.cookie("uuid"),
					"role": window.parent.$.cookie("role"),
				},
				type: "put",
				data: JSON.stringify(data),
				contentType: "application/json",
				url: "/game/child/" + uuid,
				success: function(data) {
					alert(window.parent.errorLangConfig["1004"]);
					// $("#childAccountDetailForm").get(0).reset();
					getAllChildAccount(true);
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

	// $("#username").val(getUrlParam("userName"));
	// $("#telephone").val(getUrlParam("telephone"));
	// $("#email").val(getUrlParam("email"));

	// inputEvent("telephone");
	// inputEvent("email");

	// onlyInputIntNum("telephone");

	// $("#changeselfaccountinfo").bind("click", function() {
	// 	var uuid = pagedata[currentRow].uuid;
	// 	var data = {
	// 		phone: $("#telephone").val(),
	// 		email: $("#email").val(),
	// 	};
	// 	$.ajax({
	// 		headers: {
	// 			"uuid": window.parent.$.cookie("uuid"),
	// 			"role": window.parent.$.cookie("role"),
	// 		},
	// 		type: "put",
	// 		data: JSON.stringify(data),
	// 		contentType: "application/json",
	// 		url: "/game/child/" + uuid,
	// 		success: function(data) {
	// 			alert("修改成功！");
	// 			$("#changeselfaccountinfo").get(0).disabled = true;
	// 			getAllChildAccount();
	// 		},
	// 		error: function(XMLHttpRequest, textStatus, errorThrow) {
	// 			alert(XMLHttpRequest.status);
	// 		}
	// 	});
	// });

	$("#btnBack").click(function() {
		$(".frontDiv").css("display", "block");
		$(".backDiv").css("display", "none");

		window.parent.frameLoad();
		window.parent.document.body.scrollTop = curScrollTop;
	});
});

// function inputEvent(inputID) {
// 	$("#" + inputID).focus(function() {
// 		$("#" + inputID + "helpinline").text("");
// 	});

// 	$("#" + inputID).bind("input propertychange", function() {
// 		enabledButton();
// 	});

// 	$("#" + inputID).blur(function() {
// 		inputBlur(inputID, inputID + "helpinline");
// 	});
// }

// function getUrlParam(name) {
// 	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
// 	var r = window.location.search.substr(1).match(reg);
// 	if (r != null) {
// 		return unescape(r[2]);
// 	} else {
// 		return null;
// 	}
// }

// function enabledButton() {
// 	var telephone = $("#telephone").val();
// 	var email = $("#email").val();

// 	if (null != telephone && null != email) {
// 		if (0 != telephone.length && 0 != email.length) {
// 			if (isANumber(telephone)) { // false == judgeEmail(email)  有问题???
// 				$("#changeselfaccountinfo").removeAttr("disabled");
// 				$("#changeselfaccountinfo").removeClass("btn-warning").addClass("btn-success");
// 				return;
// 			}
// 		}
// 	}
// 	$("#changeselfaccountinfo").attr("disabled", "true");
// 	$("#changeselfaccountinfo").removeClass("btn-success").addClass("btn-warning");
// }

// function onlyInputIntNum(inputID) {
// 	$("#" + inputID).keypress(function(event) {
// 		var keyCode = event.which;
// 		if (keyCode == 8 || (keyCode >= 48 && keyCode <= 57)) { //8为退格
// 			return true;
// 		}
// 		return false;
// 	}).focus(function() {
// 		imeMode有四种形式，分别是：active 代表输入法为中文; inactive 代表输入法为英文; auto 代表打开输入法 (默认);disable 代表关闭输入法
// 		this.style.imeMode = "disabled";
// 	});
// }

// function inputFloatNum(inputID) {
// 	$("#" + inputID).keypress(function(event) {
// 		var keyCode = event.which;
// 		if (keyCode == 46 || keyCode == 8 || (keyCode >= 48 && keyCode <= 57)) {
// 			return true;
// 		}
// 		return false;
// 	}).focus(function() {
// 		/*imeMode有四种形式，分别是：active 代表输入法为中文; inactive 代表输入法为英文; auto 代表打开输入法 (默认);disable 代表关闭输入法*/
// 		this.style.imeMode = "disabled";
// 	});
// }

// function isANumber(inputValue) {
// 	if (isNaN(inputValue)) {
// 		return false;
// 	}
// 	return true;
// }

// function judgeEmail(inputID) {
// 	var email = $("#" + inputID).val();
// 	var reg = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/;
// 	if (reg.test(email)) {
// 		return true;
// 	}
// 	return false;
// }

// function inputBlur(inputID, spanID) {
// 	var inputValue = getInputValue(inputID);
// 	if (null != inputValue && 0 == inputValue.length) {
// 		displayHelpInline(spanID);
// 		$("#" + spanID).text("输入不能为空！");
// 	} else if (inputID == "email") {
// 		if (!judgeEmail(inputID)) {
// 			displayHelpInline(spanID);
// 			$("#" + spanID).text("邮箱格式错误！")
// 		}
// 	}
// }

// function getInputValue(inputID) {
// 	var inputValue = $("#" + inputID).val();
// 	return $.trim(inputValue);
// }

// function displayHelpInline(spanID) {
// 	$("#" + spanID).css("display", "block");
// 	$("#" + spanID).css("color", "#ff0000");
// }