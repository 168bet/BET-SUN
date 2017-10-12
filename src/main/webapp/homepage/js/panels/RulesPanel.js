if (!GLOBAL) {
	var GLOBAL = {};
}
if (!GLOBAL.Panels) {
	GLOBAL.Panels = {};
}

function initClick() {
	$("#btnFootball").click(function() {
		$(".rulesDetail").load("js/panels/RulePages/RuleFootball.html", ".exchangeContent");
	});
	$("#btnBasketball").click(function() {
		$(".rulesDetail").load("js/panels/RulePages/RuleBasketball.html", ".exchangeContent");
	});
	$("#btnBaseball").click(function() {
		$(".rulesDetail").load("js/panels/RulePages/RuleBaseball.html", ".exchangeContent");
	});
	$("#btnTennis").click(function() {
		$(".rulesDetail").load("js/panels/RulePages/RuleTennis.html", ".exchangeContent");
	});
	$("#btnVolleyball").click(function() {
		$(".rulesDetail").load("js/panels/RulePages/RuleVolleyball.html", ".exchangeContent");
	});
	$("#btnLottery").click(function() {
		$(".rulesDetail").load("js/panels/RulePages/RuleLottery.html", ".exchangeContent");
	});
}

function RulesPanel(argument) {
	var panel = new BasePanel();
	var AboutUsBox = $('<div>').addClass('fb-rfooter-box').appendTo(panel);

	panel.data('fb').load = function() {
		$('<div>').addClass('fb-content-box').append('<div class="rules-content">\
		<div class="rulesTopBg">\
			<div>\
				<a id="btnFootball">足球规则</a>\
			</div>\
			<div>\
				<a id="btnBasketball">篮球规则</a>\
			</div>\
			<div>\
				<a id="btnBaseball">棒球规则</a>\
			</div>\
			<div>\
				<a id="btnTennis">网球规则</a>\
			</div>\
			<div>\
				<a id="btnVolleyball">排球规则</a>\
			</div>\
			<div>\
				<a id="btnLottery">乐透规则</a>\
			</div>\
		</div>\
		<div class="rulesDetail">\
			<div class="exchangeContent">\
				<div class="ruleBg">一般规则说明</div>\
				<h4>警告声明</h4>\
				<p>\
					菲博国际娱乐城是英属维京群岛政府认证的合法互联网路交易公司，现警告有意与菲博国际娱乐城交易之客户，应注意其国家或居住地的相关法律规定，如有疑问应就相关问题，寻求当地法律见解。本公司将不接受任何客户因违反当地交易相关法令所引起之任何责任\
				</p>\
				<h4>一般规则说明</h4>\
				<p>·如果会员怀疑自己的资料被盗用，应立即通知本公司，并更改详细资料，以前的使用者名称及密码将全部无效。</p>\
				<p>·会员有责任确保自己的帐户及登入资料的保密性。以使用者名称及密码进行的任何网上交易将被视为有效。</p>\
				<p>·公布赔率时出现的任何打字错误或非故意人为失误，本公司保留改正错误和按正确赔率结算交易的权力。</p>\
				<p>·您居住所在地的法律有可能规定网路交易不合法；若此情况属实，本公司将不会批准您使用付帐卡进行交易。</p>\
				<p>·每次登入时会员都应该核对自己的帐户余额。如对余额有任何疑问，请在第一时间通知本公司。</p>\
				<p>·一旦交易被接受，则不得取消或修改。</p>\
				<p>·所有比赛的让球及赔率将不时浮动，交易时的赔率将以确认交易时之赔率为准。</p>\
				<p>·进行过关交易时，每笔交易不能在同一场赛事中拣选超过一个以上的选择。</p>\
				<p>\
					·每笔最高交易金额按不同[场次]及[交易项目]及[会员帐号]设定浮动。请各会员根据网站提示限额进行下注，任何\
  团体或个人不得注册或拥有多个帐号进行下注，如交易金额超过上述设定，本公司有权取消该团体或个人的所有交\
  易。\
				</p>\
				<p>·所有比赛都必须在预定时间进行否则交易无效。若比赛在赛前被延期或取消，所有交易都被视为无效。</p>\
				<p>·除滚球外，如果比赛提前举行，在比赛开打时间后所接受之交易均为无效。</p>\
				<p>\
					·如发现会员拥有多个帐户或使用非本人资料（包括亲属、友人个人资料），本公司有权利无条件取消所有注单并关\
  闭您的账户。本公司有权限制会员只保留其中一个帐户。每位会员，包括同一家庭，同一住址，同一电子邮箱地址\
  ，同一借记卡/信用卡，同一个银行账户、同一台电脑（大学、团体、学校、公共图书馆、办公室\
  等），同一IP，同一共享计算机（如学校，公共图书馆或工作场所）。\
				</p>\
				<h4>部分特殊条款</h4>\
				<p>\
					在菲博国际娱乐城，我们在处理支付交易方面，一直保持严谨及专业。为了保障我们会员的利益，避免在多种提款方法中遇到欺诈的情况，我们有可能对更换过绑定银行信息的会员要求提供身份证明，身份证明所需物件：\
				</p>\
				<p>·阁下需要提供有您照片的身份证，护照，户口簿或驾驶执照的副本给我们。</p>\
				<p>·副本上的照片必须清楚。</p>\
				<p>·阁下将只需提供您的身份证明一次给我们，您便能够从您的帐户不断提款。</p>\
			</div>\
		</div>\
	</div>').appendTo(AboutUsBox);
		panel.find('.fb-panel-mask').remove();
		initClick();
	};
	return panel;
}

GLOBAL.Panels["RulesPanel"] = RulesPanel;