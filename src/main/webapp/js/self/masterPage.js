$(document).ready(function() {
	$("#ifmContent").css("height", $(window).height() - 101 + "px");
	$("#sidebar").css("height", $(window).height() - 101 + "px");

	var uuid = $.cookie("uuid");
	var role = $.cookie("role");

	if (uuid == "null" || role == "null" || uuid == null || role == null)
		window.location.href = "login-page.html";

	// === 初始化用户信息界面 === //

	$("#txtUserName").text($.cookie("username"));

	// var url;
	// if (role == "0" || role == "1" || role == "2") {
	// 	url = "/game/manager/" + uuid;
	// } else if (role == "3") {
	// 	url = "/game/agent/" + uuid;
	// } else if (role == "4") {
	// 	url = "/game/member/" + uuid;
	// }
	// loadWelcomeInfo(url);

	// === 公告 === //

	$.ajax({
		headers: {
			"uuid": $.cookie("uuid"),
			"role": $.cookie("role"),
		},
		type: "get",
		dataType: "json",
		// async: false,
		url: "/game/publicMsg/notice/latest",
		success: function(data) {
			if (data != null) {
				var notice = data.content;
				$("#noticeBar").text(notice);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorLangConfig[XMLHttpRequest.status]);
		}
	});

	$("#btnExit").click(function() {
		$.cookie("role", null, {
			path: "/"
		});
		$.cookie("uuid", null, {
			path: "/"
		});
		$.cookie("username", null, {
			path: "/"
		});
		window.location.href = "login-page.html"
	});
})

// === frame和菜单高度调整=== //
function frameLoad() {
	var ifm = $("#ifmContent");
	ifm.css("height", $(window).height() - 101 + "px");
	var subWeb = document.frames ? document.frames["ifmContent"].document : window.frames["ifmContent"].document;
	if (ifm != null && subWeb != null) {
		ifm.css("height", subWeb.body.scrollHeight + "px");
	}
	var sidebar = $("#sidebar");
	sidebar.css("height", subWeb.body.scrollHeight + "px");
}