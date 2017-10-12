$(function() {
	$("#agentDetailForm").validate({
		submitHandler: function(form) {
			var uuid = dataList[currentRow].uuid;
			var data = {};
			data.phone = $("#telephone").val();
			data.email = $("#email").val();
			data.commissionPer = $("#washcodepercent").val();
			data.rebate = $("#rebate").val();

			$.ajax({
				headers: {
					"uuid": window.parent.$.cookie("uuid"),
					"role": window.parent.$.cookie("role"),
				},
				async: false,
				type: "put",
				data: JSON.stringify(data),
				contentType: "application/json",
				url: "/game/agent/" + uuid,
				success: function(data) {
					alert(window.parent.errorLangConfig["1004"]);
					// $("#agentDetailForm").get(0).reset();
					getAllAgent(true);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
				}
			});
		},
		rules: {
			username: {
				required: true,
				isAccount: true
			},
			money: {
				required: true,
				isRealCount: true
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
		},
		errorPlacement: function(error, element) {
			if (element.attr("name") == "washcodepercent" || element.attr("name") == "rebate")
				element.parent().after(error);
			else
				error.appendTo(element.parent());
		},
	});

	$("#backagentmanager").bind("click", function() {
		$(".frontDiv").css("display", "block");
		$(".backDiv").css("display", "none");

		window.parent.frameLoad();
		window.parent.document.body.scrollTop = curScrollTop;
	});
});