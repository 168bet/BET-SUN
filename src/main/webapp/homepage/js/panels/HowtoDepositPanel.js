if (!GLOBAL) {
	var GLOBAL = {};
}
if (!GLOBAL.Panels) {
	GLOBAL.Panels = {};
}

function HowtoDepositPanel(argument) {
	var panel = new BasePanel();
	var DepositBox = $('<div>').addClass('fb-hfooter-box').appendTo(panel);

	panel.data('fb').load = function() {
		$('<div>').addClass('fb-content-box').append('<div id="direction">\
		<div id="articles">\
			<h3>存款帮助</h3>\
			<div>\
				<p> <strong>您现在可以透过以下方式存款：</strong>\
				</p>\
				<p> <strong>一、 公司入款 (银行卡划款)</strong>\
				</p>\
				<p> <b>您可以通过以下步骤汇款至菲博国际</b>\
				</p>\
				<!--StartFragment-->\
				<p>\
					1，登入您的会员游戏账号后，点击“线上存款”\
					<span class="color-span">\
						<strong>（在您第一次入款时，需先绑定您的出款银行账号）</strong>\
						。\
					</span>\
				</p>\
				<p>2，弹跳出新窗口后，选择“公司入款”接着根据提示进行下一步操作。</p>\
				<p>3，复制起“公司入款”的银行账号进行存款。</p>\
				<p>4，提交好所存的金额，财务确认到账后会第一时间为您添加游戏金额。</p>\
				<!--EndFragment-->\
				<p>\
					目前提供中国建设银行、中国工商银行、中国农业银行三家银行可自行选择，同行互转存款到帐时间约为三到五分钟，跨行划款到帐时间依据银行划款作业时间为基准，我们将于确认到帐后第一时间为您游戏帐户充值。\
				</p>\
				<p>\
					<strong>&nbsp;二\
						<strong>、</strong>\
						线上支付（第三方支付）</strong> \
				</p>\
				<p>\
					<!--StartFragment-->\
				</p>\
				<p>\
					1，会员登入后点选”线上存款”，然后点击“线上支付”\
					<span class="color-span">\
						<strong>（在您第一次入款时，需先绑定您的出款银行账号）</strong>\
					</span>\
					。\
				</p>\
				<p>2，选择入款额度，并请确实填写 ”联络电话”，如有任何问题，方便菲博国际客服第一时间与您联系。</p>\
				<p>3，选择”支付银行”。</p>\
				<p>4，支援借记卡：中国农业银行,中国银行,上海浦东发展银行,中国邮政,交通银行,北京银行,中国建设银行。</p>\
				<p>\
					5，确认送出后，将请您确认您的支付订单无误，并建议您记录您的支付订单号后，确认送出，并耐心等待加载网络银行页面，传输中已将您账户数据加密，请耐心等待。\
				</p>\
				<p>进入网络银行页面，请确实填写您银行账号信息，支付成功，额度将在10分钟内系统处理完成，立即加入您的菲博国际会员账户。</p>\
				<!--EndFragment-->\
				<p>\
					&nbsp;\
					<span>\
						<strong>存款需知:</strong>\
					</span>\
					<br>\
					1.菲博国际单笔最低存款为$100人民币，单笔最高存款上限为$200000人民币。\
					<br>\
					2.未开通网银的会员，请亲洽您的银行柜台办理。\
					<br>3.如有任何问题，请您联系24小时线上客服。</p>\
			</div>\
		</div>\
	</div>').appendTo(DepositBox);
		panel.find('.fb-panel-mask').remove();
	};
	return panel;
}

GLOBAL.Panels["HowtoDepositPanel"] = HowtoDepositPanel;