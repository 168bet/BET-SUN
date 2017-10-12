/**
 * 描述：代理取钱
 *
 * @author xiongyanan
 * @date 2014-9-24
 *
 */

$(function() {
	getValidateCode();
	createDrawInfoModal();

	$("#imgCode").click(function() {
		getValidateCode();
	});

	$("#drawInfo").bind("click", function() {
		if ($("#drawInfo").attr("checked") == "checked") {
			$('#drawInfoDialog').modal('show');
		}
	});

	$("#agentCountOutForm").validate({
		submitHandler: function(form) {
			var moneyPwd = $("#moneyPwd").val();
			var accountName = $("#accountName").val();
			var cardType = $("#cardType").val();
			var cardNum = $("#cardNum").val();
			var cardArea = $("#cardArea").val();
			var cardCity = $("#cardCity").val();
			var cardSite = $("#cardSite").val();
			var money = $("#money").val();
			var captcha = $("#idCode").val();

			var data = {};
			data.password = moneyPwd;
			data.realName = accountName;
			data.cardType = parseInt(cardType);
			data.cardNum = cardNum;
			data.cardArea = parseInt(cardArea);
			data.cardCity = cardCity;
			data.cardSite = cardSite;
			data.point = money;
			data.captcha = captcha;
			window.console.log(data);
			$.ajax({
				headers: {
					"uuid": window.parent.$.cookie("uuid"),
					"role": window.parent.$.cookie("role"),
				},
				type: "post",
				data: JSON.stringify(data),
				contentType: "application/json",
				url: "/game/withdrawals",
				success: function(data) {
					$("#moneyPwd").val("");
					$("#accountName").val("");
					$("#cardNum").val("");
					$("#cardCity").val("");
					$("#cardSite").val("");
					$("#money").val("");
					alert(window.parent.errorLangConfig["1011"]);
					$("#agentCountOutForm").get(0).reset();
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
				}
			});
		},
		rules: {
			moneyPwd: {
				required: true,
				isPassword: true
			},
			accountName: {
				required: true,
				isRealName: true
			},
			cardNum: {
				required: true,
				isCardNum: true
			},
			cardCity: {
				required: true,
				isCardCity: true
			},
			cardSite: {
				required: true,
				isCardSite: true
			},
			money: {
				required: true,
				isMoney: true
			},
			drawInfo: {
				isChecked: true
			},
		},
		errorPlacement: function(error, element) {
			if (element.attr("name") == "drawInfo")
				element.parent().append(error);
			else
				error.appendTo(element.parent());
		},
	});
});

function getValidateCode() {
	$.ajax({
		async: false,
		type: "get",
		contentType: "application/json",
		url: "/game/captcha",
		success: function(data) {
			$("#imgCode").attr("src", data);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}

function createDrawInfoModal() {
	$('body').append('<div id="drawInfoDialog" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">\
        <div class="modal-dialog modal-lg">\
            <div class="modal-content">\
                <div class="modal-header"></div>\
                <div class="modal-body">\
					<div>\
						<h4>提款须知：</h4>\
						<p>1 、银行账户持有人姓名必须与在菲博国际输入的姓名一致，否则无法申请提款。</p>\
						<p>2 、大陆各银行帐户均可申请提款。</p>\
						<p>3 、每个会员账户（北京时间）24小时只提供一次提款。</p>\
						<p>4 、买彩后未经全额投注提彩申请不予受理。</p>\
						<p>5 、每位客户只可以使用一张银行卡进行提款,如需要更换银行卡请与客服人员联系.否则提款将被拒绝。</p>\
						<p>6 、为保障客户资金安全，菲博国际有可能需要用户提供电话单，银行对账单或其它资料验证，以确保客户资金不会被冒领。</p>\
						<h4>到账时间：</h4>\
						<p>使用中国工商银行任何时间均可随意申请提款，5分钟至30分钟到账。</p>\
					</div>\
                </div>\
                <div class="modal-footer">\
                    <button class="btn btn-primary" data-dismiss="modal" id="confirmdrawInfo">确定</button>\
                </div>\
            </div>\
        </div>\
    </div>');
}