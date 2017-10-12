if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}

function LotteryGamePanel (argument) {
	var panel = new BasePanel();
	var desc = $('<div>').addClass('fb-lgame-desc').appendTo(panel);
	desc.text('　　彩票游戏玩法简单、赔率多元，只要依据彩票获胜组合且选择想要下注的数字，\
		开彩后即可获知中奖相关资讯，包括中国本国3D彩、体彩排列三、上海时时乐、重庆时\
		时乐、北京快乐8、广东快乐10分、北京赛车PK拾及香港特区六合彩，所/有彩票游戏公\
		正公开，皆参照各地官方开奖结果为依据，连线同步即时开彩。想要让自己一夜致富的\
		梦想成真吗?不要错过机会，下一个千万富翁很可能就是您，赶紧登入帐号即可随时参与\
		其中。');

	panel.data('fb').load = function(param){
		$.ajax({url:"/game/game/lotteryGame/enable",
			success:function(response){

				var gameBox = $('<div>').addClass('fb-lgame-box').appendTo(panel);
				var g_lgames = response.list;
				for (var i = 0; g_lgames && i < g_lgames.length; i++) {
					var lgame = g_lgames[i];
					var block = $('<div>').addClass('fb-lgame-patch').appendTo(gameBox);
					$('<img>').attr({"src":"../" + lgame.image}).appendTo(block);
					$('<p>').text(lgame.name).appendTo(block);
					$('<p>').i18ntext('ruleDescription').appendTo(block);
				}
				panel.find('.fb-panel-mask').remove();
			}
		});
	};

	return panel;
}

GLOBAL.Panels["LotteryGamePanel"] = LotteryGamePanel;