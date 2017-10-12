if (!GLOBAL) {
	var GLOBAL = {};
}
if (!GLOBAL.Panels) {
	GLOBAL.Panels = {};
}

function DiscountPanel(argument) {
	var panel = new BasePanel();
	var discountBox = $('<div>').addClass('fb-dgame-box').appendTo(panel);

	panel.data('fb').load = function() {
		$('<div>').addClass('fb-content-box').html('<div id="ContentBlock">\
		<p>\
			<span>\
				您想玩大有心跳的感觉吗？请上【菲博国际】心跳玩法、实力巨献、单注最高50万、单笔提款500万。大额无忧、资金安全。每日提款无上限，存提全免手续费。\
				<br>\
				24小时在线存提款5分钟急速到账，体育投注、真人視訊、電子游艺、彩票游戏、多種玩法、款式齊全。多款真人游戏24小时娱乐不打烊。好玩又刺激、让您玩不完。\
				<br>\
				哪里优惠最多？请到菲博国际。全球信誉最高、提款速度最快。品牌信誉第一、服务全球玩家。多种彩票、款式齐全、任君选择、香港彩1赔48倍、快乐彩198水位。\
				<br>\
				博彩就到菲博国际，您将成为真正有钱人。开户超惊喜，有您更精彩。面向新客户、首存赢头彩、您是幸运儿、好运自然来。希望能够让您：满意而来、包您满载而归\
				<br>\
				最好选择、最佳理想。加入我们，给您最优惠的待遇。独家巨资打造最信赖最可靠的网上娱乐平台，让您感觉不一样的娱乐风采。性感美女客服24小时在线为您贴心服务。\
			</span>\
		</p>\
		<h2 class="color-span">活动一：会员存款更高彩金，更高返水</h2>\
		<p>\
			优惠活动期间，会员一次性成功存款5000元以下送奖励彩金100%，5000元以上送彩金120%，降低有效投注倍数并上调实时返水，预祝广大会员朋友尽情娱乐。\
		</p>\
		<table id="table1">\
			<tbody>\
				<tr>\
					<td>存款金额</td>\
					<td>彩金奖励金额</td>\
					<td>菲博体育有效投注倍数</td>\
					<td>实时返水</td>\
				</tr>\
				<tr>\
					<td>5000元以下</td>\
					<td>100%</td>\
					<td>20倍</td>\
					<td>1%</td>\
				</tr>\
				<tr>\
					<td>5000元以上</td>\
					<td>120%</td>\
					<td>20倍</td>\
					<td>1%</td>\
				</tr>\
			</tbody>\
		</table>\
		<p>如成功存款1000元，即可申请1000彩金，菲博体育有效投注20倍，实时返水升至1%</p>\
		<p>\
			备注\
			<br>\
			[ <strong>有效投注倍数</strong>\
			]:\
			<br>\
			如会员成功存款1000，联系客服申请奖励可获赠1000彩金，菲博体育有效投注金额达（1000元+1000元）×20=40000元，即可申请提款。\
			<br>无申请奖励的会员账号不限制投注金额即可随时在线取款，不限金额和次数。</p>\
		<p>注：申请奖励需正常投注菲博体育有效投注20倍，双边打水不能计算有效投注，请各位会员注意！</p>\
		<h2>活动二：回馈用户</h2>\
		<p>\
			活动期间，会员一次性充值存款到一定额度后，菲博国际为回馈广大会员朋友的支持与厚爱，按额度赠送iPad3、iPhone5、高端苹果电脑、15-20万轿车以示感谢！\
		</p>\
		<table id="table2">\
			<tbody>\
				<tr>\
					<td>消费金额</td>\
					<td>50000元</td>\
					<td>100000元</td>\
					<td>200000元</td>\
					<td>2000000元</td>\
				</tr>\
				<tr>\
					<td>好礼</td>\
					<td>ipad 4</td>\
					<td>iphone 5s</td>\
					<td>苹果笔记本电脑</td>\
					<td>15-20万轿车</td>\
				</tr>\
			</tbody>\
		</table>\
		<h2>活动三：银行卡划款可享受2%的返利</h2>\
		<p>\
			菲博国际的新老会员通过网银、ATM、手机银行转账或现金到柜台存入到公司指定\
			<br>账号进行游戏充值，并与客服联系，即可享受充值金额2%的返利。</p>\
		<p>计算方式：</p>\
		<p>\
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您充值￥：1000x2%=20 您这次充值账号加的金额是：1000+￥20=1020，\
			<br>银行卡划款优惠需有效投注额达1倍才可提款,</p>\
		<p>(例：会员入款￥1000=1020x1=1020,会员有效投注额达到￥1020即可申请提款）</p>\
		<p>注意：每次存款必须先与在线客服联系索取公司银行账号，确认该账号是否在使用。</p>\
		<h2>活动四：会员反水规则</h2>\
		<table id="table3">\
			<tbody>\
				<tr>\
					<td>会员级别</td>\
					<td>有效投注</td>\
					<td>菲博体育返点</td>\
					<td>电子类返点</td>\
					<td>彩票类返点</td>\
				</tr>\
				<tr>\
					<td>A</td>\
					<td>1元+</td>\
					<td>1%</td>\
					<td>0.5%</td>\
					<td>1%</td>\
				</tr>\
				<tr>\
					<td>B</td>\
					<td>100万+</td>\
					<td>1%</td>\
					<td>0.6%</td>\
					<td>1%</td>\
				</tr>\
				<tr>\
					<td>C</td>\
					<td>500万+</td>\
					<td>1%</td>\
					<td>0.7%</td>\
					<td>1%</td>\
				</tr>\
				<tr>\
					<td>D</td>\
					<td>1000万+</td>\
					<td>1%</td>\
					<td>0.8%</td>\
					<td>1%</td>\
				</tr>\
				<tr>\
					<td>E</td>\
					<td>2000万+</td>\
					<td>1%</td>\
					<td>1.2%</td>\
					<td>1%</td>\
				</tr>\
				<tr>\
					<td>F</td>\
					<td>5000万+</td>\
					<td>1%</td>\
					<td>1.6%</td>\
					<td>1%</td>\
				</tr>\
			</tbody>\
		</table>\
		<h2>活动五：vip申请方法及规则</h2>\
		<p> <strong>1：新老会员周存款(公司入款)总额达500万以上,且周有效投注额达700万,方可申请加入钻石VIP</strong>\
		</p>\
		<p>\
			<strong>2：每月从会员中抽选8名，享受vip待遇（除晋升奖励外）。</strong>\
		</p>\
		<p>\
			<strong>3：若连续2个月有效投注低于5000万,公司将有权取消会员VIP资格并不在享有VIP特权,直至会员下一次符合获得VIP身份.</strong> \
		</p>\
		<p>\
			<strong>4：符合申请VIP会员资格的会员请您发送邮件到fb0088@hotmail.com进行申请,发送内容如下\
				<br>\
				邮件主题:申请菲博国际VIP会员资格\
				<br>\
				邮件内容:会员帐号*****申请VIP资格\
				<br>\
				审核通过后,24小时内VIP专员将以电子邮件或者电话方式通知会员\
				<br>5：VIP会员需遵守&lt;菲博国际&gt;用户协议相关条款,如有违反,本公司将直接取消VIP资格。</strong>\
		</p>\
		<p>\
			<strong>6.本优惠活动与其它优惠可同时并用</strong>\
		</p>\
		<p>\
			<strong>7.菲博国际保留对VIP活动的最终解释权及终止或延长此活动的权力，并不作提前通知。</strong>\
		</p>\
		<p>\
			<strong>VIP特权</strong>\
		</p>\
		<p>\
			<strong>特权1：晋升钻石VIP即1188红利。</strong>\
		</p>\
		<p>\
			<strong>特权2：每月抽取30名钻石VIP,赠送888元到38888元不等幸运彩金,彩金无须流水。</strong>\
		</p>\
		<p>\
			<strong>特权3：公司入款1优惠无上限。</strong>\
		</p>\
		<p>\
			<strong>特权4：钻石VIP贵宾生日可享受生日彩金,可获生日彩金1888。</strong>\
		</p>\
		<p>\
			<strong>特权5：钻石VIP开放单注50万。</strong>\
		</p>\
		<p>\
			<strong>特权6：VIP周返水超过5000万,还可获得额外彩金最高11888元。</strong>\
		</p>\
		<p>\
			<strong>特权7：钻石VIP提款不限额度，不限次数，无须承担手续费</strong>\
		</p>\
		<p>\
			<br>\
			<strong>&nbsp;</strong>\
		</p>\
		<table id="table4">\
			<tbody>\
				<tr>\
					<td>\
						<strong>周有效投注额</strong>\
					</td>\
					<td>\
						<strong>可获得资金</strong>\
					</td>\
				</tr>\
				<tr>\
					<td>\
						<strong>周有效投注额1800万以上</strong>\
					</td>\
					<td>\
						<strong>3888</strong>\
					</td>\
				</tr>\
				<tr>\
					<td>\
						<strong>周有效投注额2600万以上</strong>\
					</td>\
					<td>\
						<strong>8888</strong>\
					</td>\
				</tr>\
				<tr>\
					<td>\
						<strong>周有效投注额5000万以上</strong>\
					</td>\
					<td>\
						<strong>11888</strong>\
					</td>\
				</tr>\
			</tbody>\
		</table>\
		<p>\
			* 本活动最终解释权归菲博国际\
			<a href="http://www.fb0088.com">http://www.fb0088.com</a>\
			所有,以此官方公告页面为准,不明之处请咨询在线客服\
		</p>\
	</div>').appendTo(discountBox);
		panel.find('.fb-panel-mask').remove();
	};
	return panel;
}

GLOBAL.Panels["DiscountPanel"] = DiscountPanel;