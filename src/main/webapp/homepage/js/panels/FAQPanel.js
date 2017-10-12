if (!GLOBAL) {
	var GLOBAL = {};
}
if (!GLOBAL.Panels) {
	GLOBAL.Panels = {};
}

function FAQPanel(argument) {
	var panel = new BasePanel();
	var AboutUsBox = $('<div>').addClass('fb-ffooter-box').appendTo(panel);

	panel.data('fb').load = function() {
		$('<div>').addClass('fb-content-box').append('<div id="direction">\
		<div id="articles">\
			<h3>常见问题</h3>\
			<div>\
				<p> <strong>一般常见问题</strong>\
				</p>\
				<p> <strong>Q1: 如何加入菲博国际？</strong>\
				</p>\
				<p>\
					A1: 您可以直接点选 "\
					<a>\
						<span class="color-span">立即加入</span>\
					</a>\
					"，确实填写资料后，可立即登记成为菲博国际会员。\
				</p>\
				<p>\
					<strong>Q2: 我可以直接在网络上存款吗？</strong>\
				</p>\
				<p>\
					A2: 可以，菲博国际提供多种线上存款选择，详情请参照 "\
					<a>\
						<span class="color-span">存款须知</span>\
					</a>\
					"\
				</p>\
				<p>\
					<strong>Q3: 我在哪里可以找到游戏规则？</strong>\
				</p>\
				<p>\
					A3: 在未登入前，您可以在游戏的最外层看到"游戏规则"选项，清楚告诉您游戏的玩法、规则及派彩方式。 在游戏视窗中,也有"规则"选项，让您在享受游戏乐趣的同时，可以弹跳视窗随时提醒您游戏规则。\
				</p>\
				<p>\
					<strong>Q4: 我可以只试玩而不下注吗？</strong>\
				</p>\
				<p>A4: 可以，只要您联系我们的24小时在线客服，索要试玩账号，即可免费试玩。</p>\
				<p>\
					<strong>Q5: 你们的游戏会用多少副牌？</strong>\
				</p>\
				<p>A5: 在百家乐我们会用8副牌，其他游戏则会根据其性质有所调整。</p>\
				<p>\
					<strong>Q6: 您们何时会洗牌?</strong>\
				</p>\
				<p>A6: 所有纸牌游戏，当红的洗牌记号出现或游戏因线路问题中断时便会进行重新洗牌。</p>\
				<p>\
					<strong>Q7: 我的注码的限制是多少？</strong>\
				</p>\
				<p>A7: 从最低注单5元人民币以上即可投注， 您的注码会根据您的存款有所不同，以及您挑选的游戏不同而有所区别.</p>\
				<p>\
					<strong>Q8：如果忘记密码怎么办？</strong>\
					<br>\
					A8：你可点击首页\
					<a>\
						<span class="color-span">忘记密码</span>\
					</a>\
					功能，填写你当初留下的邮箱，即可取回你当 初设定的密码。当你无法收取邮件时，你也可以联系24小时线上客服人员谘询协助取回你的帐号密码。\
				</p>\
				<p>\
					<span>\
						<strong>技术常见问题</strong>\
					</span>\
				</p>\
				<p>\
					<span>\
						<strong>Q: 最低的硬体系统要求是什么?</strong>\
					</span>\
				</p>\
				<p>\
					<span>1. 任何可以接上互联网的电脑。</span>\
				</p>\
				<p>\
					<span>2. SVGA显示卡–最少要1204x768像素256色彩以上。</span>\
				</p>\
				<p>\
					<span>3. 区域宽频。</span>\
				</p>\
				<p>\
					<span>4. Windows , Mac OS X , Linux作业系统。</span>\
				</p>\
				<p>\
					<span>\
						5. Internet Explorer浏览器v6.0 或以上 (版本7.0 或以上更好)，Mozilla Firefox (浏览器v3.0 或以上)，\
					</span>\
				</p>\
				<p>\
					<span>Opera (浏览器v8.0 或以上)，Chrome(浏览器v6.0 或以上)，Safari (浏览器v4.0 或以上)</span>\
				</p>\
				<p>\
					<span>\
						6. 要浏览线上娱乐城，你可以在 Macromedia网站下载Macromedia Flash Player浏览器附加程式\
					</span>\
				</p>\
				<p>\
					<span>(8.0或以上版本)。</span>\
				</p>\
				<p>\
					<span  class="color-span">如有任何不解可联系菲博国际24小时在线客服！我们将在第一时间为您服务.</span>\
				</p>\
				<p>&nbsp;</p>\
			</div>\
		</div>\
	</div>').appendTo(AboutUsBox);
		panel.find('.fb-panel-mask').remove();
	};
	return panel;
}

GLOBAL.Panels["FAQPanel"] = FAQPanel;