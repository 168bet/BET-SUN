if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}


function WeiPaiCaiPanel(){
	var panel = new BasePanel();
	var menu = $('<div>').addClass('fb-weipaicai-menu').appendTo(panel);
	$('<div>').addClass('clickable').text("体育单式").appendTo(menu).click(function(){

	});
	$('<div>').addClass('clickable').text("体育串关").appendTo(menu).click(function(){

	});
	$('<div>').addClass('clickable').text("香港乐透").appendTo(menu).click(function(){

	});
	$('<div>').addClass('clickable').text("彩票游戏").appendTo(menu).click(function(){

	});
	var head = $('<div>').addClass('fb-weipaicai-head').appendTo(panel);
	$('<div>').css("border","0px").width(163).text('投注时间').appendTo(head);
	$('<div>').width(75).text('注单号/模式').appendTo(head);
	$('<div>').width(400).text('投注详细信息').appendTo(head);
	$('<div>').width(88).text('下注').appendTo(head);
	$('<div>').width(88).text('可赢').appendTo(head);


	var body = $('<div>').addClass('fb-weipaicai-body').appendTo(panel);
	var footer = $('<div>').addClass('fb-weipaicai-footer').appendTo(panel);

	panel.data('fb').load = function(){
		$.ajax({url:"/game/weipaicai/sportsLottery",
			beforesend:function(){
				panel.find('.fb-panel-mask').show();
			},
			error:function(response){
				var recodes = response.list;
				$('.fb-weipaicai-body').empty();
				if(!recodes || recodes.length == 0){
					$('<div>').addClass('fb-weipaicai-box').text("暂无记录").appendTo(body);
					$('.fb-weipaicai-footer').text("共 0 条记录，总投注金额：0 RMB，最高可赢金额：0 RMB");
				}
				for (var i = 0; recodes && i < recodes.length; i++) {
					var r = recodes[i];
					var box = $('<div>').addClass('fb-weipaicai-box').appendTo(body);
					$('<div>').width(150).text(r.time).appendTo(head);
					$('<div>').width(69).text(r.mode).appendTo(head);
					$('<div>').width(366).text(r.desc).appendTo(head);
					$('<div>').width(80).text(r.amount).appendTo(head);
					$('<div>').width(80).text(r.win).appendTo(head);
				}

				panel.find('.fb-panel-mask').hide();
			}
		});
	};
	return panel;
}

GLOBAL.Panels["WeiPaiCaiPanel"] = WeiPaiCaiPanel;