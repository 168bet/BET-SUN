$(document).ready(function() {
	var username = null;
	var role = window.parent.$.cookie("role");
	uuid = window.parent.$.cookie("uuid");

	if (role == 0 || role == 1 || role == 2) {
		username = "公司直属";
	} else {
		$.ajax({
			headers: {
				"uuid": window.parent.$.cookie("uuid"),
				"role": window.parent.$.cookie("role"),
			},
			type: "get",
			dataType: "json",
			contentType: "application/json",
			async: false,
			url: "/game/agent/" + window.parent.$.cookie("uuid"),
			success: function(data) {
				username = data.username;
			}
		});
	}

	$("#treeview").kendoTreeView({
		checkboxes: {
			checkChildren: false
		},

		check: onCheck,

		select: function(e) {
			getSubNodes(e);
		},

		dataSource: [{
			id: uuid,
			text: username,
			expanded: false
		}]
	});


	$("#addChildAccountForm").validate({
		submitHandler: function(form) {
			var data = {
				username: $("#username").val(),
				password: $("#password").val(),
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
				url: "/game/child",
				success: function(data) {
					alert(window.parent.errorLangConfig["1002"]);
					$("#addChildAccountForm").get(0).reset();
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

function checkedNodeId(nodes) {
	for (var index = 0; index < nodes.length; index++) {
		if (nodes[index].checked) {
			$("#treeview").parent().parent().css("display", "none");
			$("#parentagent").css("display", "block");
			$("#parentagent").val(nodes[index].text);
			$("#parentagent").attr("title", nodes[index].id);

			$("#parentagent").bind("click", function() {
				$("#treeview").parent().parent().css("display", "block");
				$("#parentagent").css("display", "none");
				//setNodesUncheck();
			});
			return;
		}

		if (nodes[index].hasChildren) {
			checkedNodeId(nodes[index].children.view());
		}
	}
}

function setNodesUncheck() {
	var treeView = $("#treeview").data("kendoTreeView");
	setNodesUncheckRecurse(treeView.dataSource.view());
}

function setNodesUncheckRecurse(nodes) {
	for (var index = 0; index < nodes.length; index++) {
		nodes[index].checked = false;
		if (nodes[index].hasChildren) {
			setNodesUncheckRecurse(nodes[index].children.view());
		}
	}
}
