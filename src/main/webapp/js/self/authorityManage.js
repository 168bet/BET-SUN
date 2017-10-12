$(function() {
	var uuid = $.cookie("uuid");
	var role = $.cookie("role");

	if (role == 3) { //代理
		$.ajax({
			headers: {
				"uuid": uuid,
				"role": role,
			},
			type: "get",
			url: "/game/js/agent/addAgentMenu.js",
			async: false,
			cache: false,
			dataType: "script",
			success: function(data) {
				$("#accountInfoNum").text("3");
				addAgentMenu();
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorLangConfig[XMLHttpRequest.status]);
			}
		});
	}

	if (role == 2) { //客服管理员
		$.ajax({
			headers: {
				"uuid": uuid,
				"role": role,
			},
			async: false,
			type: "get",
			url: "/game/js/customerAdmin/addCustomerAdminMenu.js",
			async: false,
			cache: false,
			dataType: "script",
			success: function(data) {
				addCountInOutMenu();
				addLogManageMenu();
				addConfigManagerMenu();
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorLangConfig[XMLHttpRequest.status]);
			}
		});
	}

	if (role == 1) { //普通管理员
		$.ajax({
			headers: {
				"uuid": uuid,
				"role": role,
			},
			type: "get",
			url: "/game/js/customerAdmin/addCustomerAdminMenu.js",
			async: false,
			cache: false,
			dataType: "script",
			success: function(data) {
				addCountInOutMenu();
				addLogManageMenu();
				addConfigManagerMenu();

				$.ajax({
					headers: {
						"uuid": uuid,
						"role": role,
					},
					type: "get",
					url: "/game/js/commonAdmin/addCommonAdminMenu.js",
					async: false,
					cache: false,
					dataType: "script",
					success: function(data) {
						$("#accountManagerNum").text("7");
						addCommonAdminMenu();
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						alert(errorLangConfig[XMLHttpRequest.status]);
					}
				});
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorLangConfig[XMLHttpRequest.status]);
			}
		});
	}

	if (role == 0) { //超级管理员
		$.ajax({
			headers: {
				"uuid": uuid,
				"role": role,
			},
			type: "get",
			url: "/game/js/customerAdmin/addCustomerAdminMenu.js",
			async: false,
			cache: false,
			dataType: "script",
			success: function(data) {
				addCountInOutMenu();
				addLogManageMenu();
				addConfigManagerMenu();
				$.ajax({
					headers: {
						"uuid": uuid,
						"role": role,
					},
					type: "get",
					url: "/game/js/commonAdmin/addCommonAdminMenu.js",
					async: false,
					cache: false,
					dataType: "script",
					success: function(data) {
						addCommonAdminMenu();

						$.ajax({
							headers: {
								"uuid": uuid,
								"role": role,
							},
							type: "get",
							url: "/game/js/superAdmin/addSuperAdminMenu.js",
							async: false,
							cache: false,
							dataType: "script",
							success: function(data) {
								$("#accountManagerNum").text("9");
								addSuperAdminMenu();
							},
							error: function(XMLHttpRequest, textStatus, errorThrown) {
								alert(errorLangConfig[XMLHttpRequest.status]);
							}
						});
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						alert(errorLangConfig[XMLHttpRequest.status]);
					}
				});
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorLangConfig[XMLHttpRequest.status]);
			}
		});
	}

	//消息管理和修改密码菜单
	if (role == 0 || role == 1 || role == 2) { //管理员
		$("#parentMenu").append('<li class="submenu" id="noticeManage"></li>');
		$("#noticeManage").append('<a href="#"><i class="icon icon-th-list"></i><span class="language" data-word="msgManage">消息管理</span><span class="label">3</span></a>');
		var noticeRow = $("<ul>").html('<li><a href="#" id="btnNoticeManage" class="menuBtnItem"><span class="language" data-word="noticeManage">公告管理</span></a></li>');
		var newsRow = $("<ul>").html('<li><a href="#" id="btnNewsManage" class="menuBtnItem"><span class="language" data-word="newsManage">新闻管理</span></a></li>');
		var mailRow = $("<ul>").html('<li><a href="#" id="btnMailManage" class="menuBtnItem"><span class="language" data-word="mailManage">站内信管理</span></a></li>');
		$("#noticeManage").append(noticeRow);
		$("#noticeManage").append(newsRow);
		$("#noticeManage").append(mailRow);

		var changePasswordRow = $("<li>").html('<a href="#" id="btnPasswordManage" class="menuBtnItem"><span class="language" data-word="passwordManage">密码管理</span></a>');
		$("#accountManage > ul").append(changePasswordRow);
	}

	//站内信
	if (role == 3) { //代理
		$("#btnShowMail").click(function() {
			$("#ifmContent").attr("src", "checkMail.html?lang=" + language);
		});
		$("#txtNewMail").show();
		checkNewMail();
	}

	//子账号去掉新增代理、会员、子账号菜单
	if (role == 4) {
		$("#sidebar #btnAddAgent,#sidebar #btnAddUser,#sidebar #btnAddChildAccount").parent().remove();
	}

	// === Load Page related === //
	$(".menuBtnItem").click(function() {
		if ($(this).attr("id") == "btnAddAgent") {
			$("#ifmContent").attr("src", "add-agent.html?lang=" + language);
		} else if ($(this).attr("id") == "btnAddChildAccount") {
			$("#ifmContent").attr("src", "add-child-account.html?lang=" + language);
		} else if ($(this).attr("id") == "btnAddUser") {
			$("#ifmContent").attr("src", "add-user.html?lang=" + language);
		} else if ($(this).attr("id") == "btnUnderlingManage") {
			$("#ifmContent").attr("src", "underling-manage.html?lang=" + language);
		} else if ($(this).attr("id") == "btnChildAccountManage") {
			$("#ifmContent").attr("src", "child-account-manage.html?lang=" + language);
		} else if ($(this).attr("id") == "btnSearchLog") {
			$("#ifmContent").attr("src", "search-log.html?lang=" + language);
		} else if ($(this).attr("id") == "btnCountIn") {
			$("#ifmContent").attr("src", "count-in.html?lang=" + language);
		} else if ($(this).attr("id") == "btnCountOut") {
			$("#ifmContent").attr("src", "count-out.html?lang=" + language);
		} else if ($(this).attr("id") == "btnWinLossForm") {
			$("#ifmContent").attr("src", "win-loss-form.html?lang=" + language);
		} else if ($(this).attr("id") == "btnUserManage") {
			$("#ifmContent").attr("src", "user-manage.html?lang=" + language);
		} else if ($(this).attr("id") == "btnUserDealForm") {
			$("#ifmContent").attr("src", "user-deal-form.html?lang=" + language);
		} else if ($(this).attr("id") == "btnGameResultForm") {
			$("#ifmContent").attr("src", "game-result-form.html?lang=" + language);
		} else if ($(this).attr("id") == "btnCountRecordForm") {
			$("#ifmContent").attr("src", "count-record-form.html?lang=" + language);
		} else if ($(this).attr("id") == "btnAddAdmin") {
			$("#ifmContent").attr("src", "add-admin.html?lang=" + language);
		} else if ($(this).attr("id") == "btnCommonAdminManage") {
			$("#ifmContent").attr("src", "common-admin-manage.html?lang=" + language);
		} else if ($(this).attr("id") == "btnChangePassword") {
			$("#ifmContent").attr("src", "change-password.html?lang=" + language);
		} else if ($(this).attr("id") == "btnCSAdminManage") {
			$("#ifmContent").attr("src", "customer-admin-manage.html?lang=" + language);
		} else if ($(this).attr("id") == "btnUserInfo") {
			$("#ifmContent").attr("src", "user-info.html?lang=" + language);
		} else if ($(this).attr("id") == "btnRealGameConfig") {
			$("#ifmContent").attr("src", "real-game-config.html?lang=" + language);
		} else if ($(this).attr("id") == "btnVideoGameConfig") {
			$("#ifmContent").attr("src", "video-game-config.html?lang=" + language);
		} else if ($(this).attr("id") == "btnLotteryGameConfig") {
			$("#ifmContent").attr("src", "lottery-game-config.html?lang=" + language);
		} else if ($(this).attr("id") == "btnDrawMoney") {
			$("#ifmContent").attr("src", "agent-count-out.html?lang=" + language);
		} else if ($(this).attr("id") == "btnNoticeManage") {
			$("#ifmContent").attr("src", "notice-manage.html?lang=" + language);
		} else if ($(this).attr("id") == "btnNewsManage") {
			$("#ifmContent").attr("src", "news-manage.html?lang=" + language);
		} else if ($(this).attr("id") == "btnMailManage") {
			$("#ifmContent").attr("src", "mail-manage.html?lang=" + language);
		} else if ($(this).attr("id") == "btnPasswordManage") {
			$("#ifmContent").attr("src", "password-manage.html?lang=" + language);
		}
	});
});

function checkNewMail() {
	$.ajax({
		headers: {
			"uuid": $.cookie("uuid"),
			"role": $.cookie("role"),
		},
		url: "/game/internalMsg/" + $.cookie("uuid") + "/new",
		async: false,
		success: function(data) {
			if (data == 0) {
				$("#txtNewMailNum").hide();
			} else {
				$("#txtNewMailNum").text("(" + data + ")");
				$("#txtNewMailNum").show();
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorLangConfig[XMLHttpRequest.status]);
		}
	});
}