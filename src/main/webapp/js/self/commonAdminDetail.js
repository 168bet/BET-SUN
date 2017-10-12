$(function() {
	$("#commonAdminDetailForm").validate({
		submitHandler: function(form) {
			var uuid = pagedata[currentRow].uuid;
			var data = {
				phone: $("#telephone").val(),
				email: $("#email").val(),
				role: $("#selecttype").val(),
			};
			$.ajax({
				headers: {
					"uuid": window.parent.$.cookie("uuid"),
					"role": window.parent.$.cookie("role"),
				},
				async: false,
				type: "put",
				data: JSON.stringify(data),
				contentType: "application/json",
				url: "/game/manager/" + uuid,
				success: function(data) {
					alert(window.parent.errorLangConfig["1004"]);
					$("#commonAdminDetailForm").get(0).reset();
					loadData(true);
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

	$("#btnBack").click(function() {
		$(".frontDiv").css("display", "block");
		$(".backDiv").css("display", "none");

		window.parent.frameLoad();
		window.parent.document.body.scrollTop = curScrollTop;
	});

});